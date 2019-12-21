package com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry;

import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import com.github.sejoslaw.vanillamagic.api.event.EventQuarry;
import com.github.sejoslaw.vanillamagic.api.exception.NotInventoryException;
import com.github.sejoslaw.vanillamagic.api.inventory.InventoryWrapper;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.IQuarry;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.IQuarryUpgradeHelper;
import com.github.sejoslaw.vanillamagic.core.VanillaMagic;
import com.github.sejoslaw.vanillamagic.inventory.InventoryHelper;
import com.github.sejoslaw.vanillamagic.tileentity.machine.TileMachine;
import com.github.sejoslaw.vanillamagic.util.BlockPosUtil;
import com.github.sejoslaw.vanillamagic.util.EventUtil;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TileQuarry extends TileMachine implements IQuarry {
	public static final String REGISTRY_NAME = TileQuarry.class.getName();

	private IQuarryUpgradeHelper upgradeHelper = new QuarryUpgradeHelper(this);

	private BlockPos diamondBlockPos;
	private BlockPos redstoneBlockPos;

	/**
	 * It is not final, but should never be changed. <br>
	 * It is set in the checkSurroundings().
	 */
	private Direction startPosFacing;
	private Direction diamondFacing;
	private Direction redstoneFacing;

	/**
	 * Forces Quarry to stop working.
	 */
	private boolean forceStop = false;

	/**
	 * It's (size)x(size) but (size-2)x(size-2) is for digging BlocksInChunk Only
	 * multiply this
	 */
	private int quarrySize = QuarrySizeHelper.BASIC_SIZE;
	private int diamondBlocks = 0;
	private int redstoneBlocks = 0;

	public int getQuarrySize() {
		return this.quarrySize;
	}

	public Direction getStartPosDirection() {
		return this.startPosFacing;
	}

	public void forceQuarryStop() {
		this.forceStop = true;
	}

	public void forceQuarryStart() {
		this.forceStop = false;
	}

	public IQuarryUpgradeHelper getQuarryUpgradeHelper() {
		return this.upgradeHelper;
	}

	/**
	 * Method for checking DiamondBlock and RedstoneBlock
	 */
	public boolean checkSurroundings() {
		if (((this.diamondBlockPos != null)
				&& !BlockUtil.areEqual(Blocks.DIAMOND_BLOCK, world.getBlockState(this.diamondBlockPos).getBlock()))
				|| ((this.redstoneBlockPos != null) && !BlockUtil.areEqual(Blocks.REDSTONE_BLOCK,
						world.getBlockState(this.redstoneBlockPos).getBlock()))) {
			return false;
		}

		if (this.redstoneBlockPos != null || this.diamondBlockPos != null) {
			return true;
		}

		for (Direction redstoneFacing : directions()) {
			this.redstoneBlockPos = pos.offset(redstoneFacing);

			if (BlockUtil.areEqual(world.getBlockState(this.redstoneBlockPos).getBlock(), Blocks.REDSTONE_BLOCK)) {
				Direction diamondFacing = this.redstoneFacing.rotateY();
				this.diamondBlockPos = pos.offset(diamondFacing);

				if (BlockUtil.areEqual(world.getBlockState(this.diamondBlockPos).getBlock(), Blocks.DIAMOND_BLOCK)) {
					if (startPos == null) {
						this.redstoneFacing = redstoneFacing;
						this.diamondFacing = diamondFacing;

						this.startPosFacing = this.diamondFacing.rotateY();
						this.restartDefaultStartPos();

						this.chestPosInput = new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
						this.chestPosOutput = pos.offset(this.getOutputDirection());

						try {
							this.inventoryInput = new InventoryWrapper(world, this.chestPosInput);
							this.inventoryOutput = new InventoryWrapper(world, this.chestPosOutput);

							EventUtil.postEvent(new EventQuarry.BuildCorrectly((IQuarry) this, world, pos));
						} catch (NotInventoryException e) {
							VanillaMagic.log(Level.ERROR, this.getClass().getSimpleName()
									+ " - error when converting to IInventory at position: " + e.position.toString());
						}
					}

					return true;
				}
			}
		}

		return false;
	}

	public void restartDefaultStartPos() {
		startPos = pos.offset(this.startPosFacing);
		workingPos = pos.offset(this.startPosFacing);
	}

	public Direction[] directions() {
		return Direction.Plane.HORIZONTAL.facings();
	}

	public boolean canDig() {
		return ticks >= oneOperationCost;
	}

	public boolean inventoryOutputHasSpace() {
		return !InventoryHelper.isInventoryFull(getOutputInventory().getInventory(), getOutputDirection());
	}

	public void spawnDigged(ItemStack digged) {
		Block.spawnAsEntity(world, this.pos.offset(Direction.UP, 2), digged);
	}

	public BlockPos moveWorkingPosToNextPos() {
		return workingPos.offset(Direction.DOWN);
	}

	/**
	 * Here I want to check for the number of IQuarryUpgrades next to the Quarry.
	 * <br>
	 * Count them and change the Quarry size.
	 */
	protected void performAdditionalOperations() {
		BlockPos cauldronPos = BlockPosUtil.copyPos(this.getMachinePos());
		cauldronPos = cauldronPos.offset(this.diamondFacing);
		this.diamondBlocks = 0;
		IBlockState checkingBlock = world.getBlockState(cauldronPos);

		while ((BlockUtil.areEqual(checkingBlock.getBlock(), Blocks.DIAMOND_BLOCK))
				|| (QuarryUpgradeRegistry.isUpgradeBlock(checkingBlock.getBlock()))) {
			if (BlockUtil.areEqual(checkingBlock.getBlock(), Blocks.DIAMOND_BLOCK)) {
				this.diamondBlocks++;
			} else {
				this.upgradeHelper.addUpgradeFromBlock(checkingBlock.getBlock(), cauldronPos);
			}

			cauldronPos = cauldronPos.offset(this.diamondFacing);
			checkingBlock = world.getBlockState(cauldronPos);
		}

		this.upgradeHelper.modifyQuarry(this);
		this.quarrySize = QuarrySizeHelper.getSize(this.diamondBlocks);
	}

	public List<String> getAdditionalInfo() {
		List<String> baseInfo = super.getAdditionalInfo();
		baseInfo.add("Quarry Size: " + this.quarrySize + " blocks");

		this.performAdditionalOperations();

		List<String> withUpgrades = this.upgradeHelper.addAdditionalInfo(baseInfo);

		this.upgradeHelper.clearUpgrades();
		this.countRedstoneBlocks();

		baseInfo.add("Mining blocks per tick: " + this.redstoneBlocks);

		return withUpgrades;
	}

	/**
	 * Returns the list of the drops from the block.
	 */
	public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos,
			IBlockState workingPosState) {
		return this.upgradeHelper.getDrops(blockToDig, world, workingPos, workingPosState);
	}

	public void doWork() {
		if (this.forceStop) {
			return;
		}

		this.countRedstoneBlocks();

		for (int i = 0; i < this.redstoneBlocks; ++i) {
			performOneOperation();
		}

		EventUtil.postEvent(new EventQuarry.Work((IQuarry) this, world, pos));
		this.upgradeHelper.clearUpgrades();
	}

	public void countRedstoneBlocks() {
		BlockPos cauldronPos = BlockPosUtil.copyPos(this.getMachinePos());
		cauldronPos = cauldronPos.offset(this.redstoneFacing);

		this.redstoneBlocks = 0;
		IBlockState checkingBlock = world.getBlockState(cauldronPos);

		while (BlockUtil.areEqual(checkingBlock.getBlock(), Blocks.REDSTONE_BLOCK)) {
			this.redstoneBlocks++;

			cauldronPos = cauldronPos.offset(this.redstoneFacing);
			checkingBlock = world.getBlockState(cauldronPos);
		}
	}

	public void performOneOperation() {
		if (!canDig()) {
			return;
		}

		if (world.isAirBlock(workingPos)) {
			while (!world.isAirBlock(workingPos)) {
				workingPos = this.moveWorkingPosToNextPos();
			}
		} else if (BlockUtil.areEqual(world.getBlockState(workingPos).getBlock(), Blocks.BEDROCK)) {
			this.goToNextPosAfterHitBedrock();
		} else {
			boolean hasChest = this.isNextToOutput();
			IBlockState stateToDig = world.getBlockState(workingPos);
			Block blockToDig = stateToDig.getBlock();
			List<ItemStack> drops = this.getDrops(blockToDig, world, workingPos, stateToDig);

			if (blockToDig instanceof IInventory) {
				IInventory inv = (IInventory) blockToDig;

				for (int i = 0; i < inv.getSizeInventory(); ++i) {
					ItemStack stack = inv.getStackInSlot(i);

					if (!ItemStackUtil.isNullStack(stack)) {
						drops.add(stack);
					}
				}
			}

			for (ItemStack stack : drops) {
				if (!hasChest) {
					this.spawnDigged(stack);
				} else if (hasChest) {
					ItemStack leftItems = InventoryHelper
							.putStackInInventoryAllSlots(getOutputInventory().getInventory(), stack, getOutputDirection());

					if (ItemStackUtil.isNullStack(leftItems)) {
						break;
					}

					if (ItemStackUtil.getStackSize(leftItems) > 0) {
						this.spawnDigged(leftItems);
					}
				}
			}

			world.setBlockToAir(workingPos);
			this.decreaseTicks();
		}

		workingPos = this.moveWorkingPosToNextPos();
	}

	/**
	 * Rotates the given facing the number of given times and returns this facing
	 * after rotation. <br>
	 * This will only rotate Horizontally.
	 * 
	 * @see {@link net.minecraft.util.Direction.Plane#HORIZONTAL}
	 */
	public Direction rotateY(Direction startFace, int times) {
		for (int i = 0; i < times; ++i) {
			startFace = startFace.rotateY();
		}

		return startFace;
	}

	public void goToNextPosAfterHitBedrock() {
		workingPos = new BlockPos(workingPos.getX(), startPos.getY(), workingPos.getZ()).offset(this.startPosFacing);

		if (BlockPosUtil.distanceInLine(workingPos, startPos) > this.quarrySize) {
			startPos = startPos.offset(rotateY(this.startPosFacing, 3));
			workingPos = BlockPosUtil.copyPos(startPos);
		}
	}

	public Direction getOutputDirection() {
		return this.startPosFacing.rotateY();
	}

	public void forceChunkLoading(Ticket ticket) {
		super.forceChunkLoading(ticket);

		ChunkPos startChunk = new ChunkPos(startPos.getX() >> 4, startPos.getZ() >> 4);
		ForgeChunkManager.forceChunk(ticket, startChunk);
		ChunkPos workingChunk = new ChunkPos(workingPos.getX() >> 4, workingPos.getZ() >> 4);
		ForgeChunkManager.forceChunk(ticket, workingChunk);
	}

	public boolean prepareCustomTileEntity() {
		return this.checkSurroundings();
	}
}