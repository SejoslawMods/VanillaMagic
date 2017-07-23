package seia.vanillamagic.tileentity.machine.quarry;

import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import seia.vanillamagic.api.event.EventQuarry;
import seia.vanillamagic.api.exception.NotInventoryException;
import seia.vanillamagic.api.inventory.InventoryWrapper;
import seia.vanillamagic.api.tileentity.machine.IQuarry;
import seia.vanillamagic.api.tileentity.machine.IQuarryUpgradeHelper;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.inventory.InventoryHelper;
import seia.vanillamagic.tileentity.machine.TileMachine;
import seia.vanillamagic.util.BlockPosUtil;
import seia.vanillamagic.util.ItemStackUtil;

/**
 * Quarry logic
 */
public class TileQuarry extends TileMachine implements IQuarry
{
	public static final String REGISTRY_NAME = TileQuarry.class.getName();
	
	private IQuarryUpgradeHelper _upgradeHelper = new QuarryUpgradeHelper(this);
	
	private BlockPos _diamondBlockPos;
	private BlockPos _redstoneBlockPos;
	
	/**
	 * It is not final, but should never be changed. <br>
	 * It is set in the checkSurroundings().
	 */
	private EnumFacing _startPosFacing;
	private EnumFacing _diamondFacing;
	private EnumFacing _redstoneFacing;
	
	/**
	 * Forces Quarry to stop working.
	 */
	private boolean _forceStop = false;
	
	/**
	 * It's (size)x(size) but (size-2)x(size-2) is for digging
	 * BlocksInChunk
	 * Only multiply this
	 */
	private int _quarrySize = QuarrySizeHelper.BASIC_SIZE;
	private int _diamondBlocks = 0;
	private int _redstoneBlocks = 0;
	
	public int getQuarrySize()
	{
		return _quarrySize;
	}
	
	public EnumFacing getStartPosFacing()
	{
		return _startPosFacing;
	}
	
	public void forceQuarryStop()
	{
		_forceStop = true;
	}
	
	public void forceQuarryStart()
	{
		_forceStop = false;
	}
	
	public IQuarryUpgradeHelper getQuarryUpgradeHelper()
	{
		return _upgradeHelper;
	}
	
	/**
	 * Method for checking DiamondBlock and RedstoneBlock
	 */
	public boolean checkSurroundings()
	{
		if (_diamondBlockPos != null)
			if (!Block.isEqualTo(Blocks.DIAMOND_BLOCK, world.getBlockState(_diamondBlockPos).getBlock()))
				return false;
		
		if (_redstoneBlockPos != null)
			if (!Block.isEqualTo(Blocks.REDSTONE_BLOCK, world.getBlockState(_redstoneBlockPos).getBlock()))
				return false;
		
		if (_redstoneBlockPos == null || _diamondBlockPos == null)
		{
			for (EnumFacing _redstoneFacing : facings())
			{
				_redstoneBlockPos = pos.offset(_redstoneFacing);
				if (Block.isEqualTo(world.getBlockState(_redstoneBlockPos).getBlock(), Blocks.REDSTONE_BLOCK))
				{
					EnumFacing _diamondFacing = _redstoneFacing.rotateY();
					_diamondBlockPos = pos.offset(_diamondFacing);
					if (Block.isEqualTo(world.getBlockState(_diamondBlockPos).getBlock(), Blocks.DIAMOND_BLOCK))
					{
						if (startPos == null)
						{
							this._redstoneFacing = _redstoneFacing;
							this._diamondFacing = _diamondFacing;
							this._startPosFacing = _diamondFacing.rotateY();
							restartDefaultStartPos();
							this.chestPosInput = new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
							this.chestPosOutput = pos.offset(getOutputFacing());
							try
							{
								this.inventoryInput = new InventoryWrapper(world, chestPosInput);
								this.inventoryOutput = new InventoryWrapper(world, chestPosOutput);
								MinecraftForge.EVENT_BUS.post(new EventQuarry.BuildCorrectly((IQuarry) this, world, pos));
							}
							catch (NotInventoryException e)
							{
								VanillaMagic.LOGGER.log(Level.ERROR, this.getClass().getSimpleName() + 
										" - error when converting to IInventory at position: " + e.position.toString());
							}
						}
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}
	
	public void restartDefaultStartPos()
	{
		startPos = pos.offset(_startPosFacing);
		workingPos = pos.offset(_startPosFacing);
	}
	
	public EnumFacing[] facings()
	{
		return EnumFacing.Plane.HORIZONTAL.facings();
	}
	
	/**
	 * will return if  quarry can dig next block
	 */
	public boolean canDig()
	{
		if (ticks >= oneOperationCost) return true;
		return false;
	}
	
	public boolean inventoryOutputHasSpace()
	{
		return !InventoryHelper.isInventoryFull(getOutputInventory().getInventory(), getOutputFacing());
	}
	
	public void spawnDigged(ItemStack digged)
	{
		Block.spawnAsEntity(world, this.pos.offset(EnumFacing.UP, 2), digged);
	}
	
	public BlockPos moveWorkingPosToNextPos()
	{
		return workingPos.offset(EnumFacing.DOWN);
	}
	
	/**
	 * Here I want to check for the number of IQuarryUpgrades next to the Quarry. <br>
	 * Count them and change the Quarry size.
	 */
	protected void performAdditionalOperations()
	{
		BlockPos cauldronPos = BlockPosUtil.copyPos(this.getMachinePos());
		cauldronPos = cauldronPos.offset(_diamondFacing);
		_diamondBlocks = 0;
		IBlockState checkingBlock = world.getBlockState(cauldronPos);
		// quarry resizing is not an actual upgrade, it's easier to count it here
		while ((Block.isEqualTo(checkingBlock.getBlock(), Blocks.DIAMOND_BLOCK))
				|| (QuarryUpgradeRegistry.isUpgradeBlock(checkingBlock.getBlock())))
		{
			if (Block.isEqualTo(checkingBlock.getBlock(), Blocks.DIAMOND_BLOCK)) _diamondBlocks++;
			else _upgradeHelper.addUpgradeFromBlock(checkingBlock.getBlock(), cauldronPos);
			
			cauldronPos = cauldronPos.offset(_diamondFacing);
			checkingBlock = world.getBlockState(cauldronPos);
		}
		_upgradeHelper.modifyQuarry(this);
		_quarrySize = QuarrySizeHelper.getSize(_diamondBlocks);
	}
	
	public List<String> getAdditionalInfo()
	{
		List<String> baseInfo = super.getAdditionalInfo();
		baseInfo.add("Quarry Size: " + _quarrySize + " blocks");
		performAdditionalOperations();
		List<String> withUpgrades = _upgradeHelper.addAdditionalInfo(baseInfo);
		_upgradeHelper.clearUpgrades();
		countRedstoneBlocks();
		baseInfo.add("Mining blocks per tick: " + _redstoneBlocks);
		return withUpgrades;
	}
	
	/**
	 * Returns the list of the drops from the block.
	 */
	public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos, IBlockState workingPosState)
	{
		return _upgradeHelper.getDrops(blockToDig, world, workingPos, workingPosState);
	}
	
	public void doWork() // once a world tick
	{
		// if  forced to stop, stop.
		if (_forceStop) return;
		
		// Counting the number of blocks
		countRedstoneBlocks();
		for (int i = 0; i < this._redstoneBlocks; ++i) performOneOperation();
		MinecraftForge.EVENT_BUS.post(new EventQuarry.Work((IQuarry) this, world, pos));
		_upgradeHelper.clearUpgrades();
	}
	
	/**
	 * Counts the number of RedstoneBlocks in line.
	 */
	public void countRedstoneBlocks()
	{
		BlockPos cauldronPos = BlockPosUtil.copyPos(this.getMachinePos());
		cauldronPos = cauldronPos.offset(_redstoneFacing);
		_redstoneBlocks = 0;
		IBlockState checkingBlock = world.getBlockState(cauldronPos);
		while (Block.isEqualTo(checkingBlock.getBlock(), Blocks.REDSTONE_BLOCK))
		{
			_redstoneBlocks++;
			cauldronPos = cauldronPos.offset(_redstoneFacing);
			checkingBlock = world.getBlockState(cauldronPos);
		}
	}
	
	/**
	 * One Quarry operation.
	 */
	public void performOneOperation()
	{
		if (!canDig()) return;
		if (world.isAirBlock(workingPos)) 
		{
			while(!world.isAirBlock(workingPos)) 
				workingPos = moveWorkingPosToNextPos();
		}
		else if (Block.isEqualTo(world.getBlockState(workingPos).getBlock(), Blocks.BEDROCK)) goToNextPosAfterHitBedrock();
		else // mine something like stone, iron-ore, etc.
		{
			boolean hasChest = isNextToOutput(); // chest or any other IInventory
			IBlockState stateToDig = world.getBlockState(workingPos);
			Block blockToDig = stateToDig.getBlock();
			List<ItemStack> drops = getDrops(blockToDig, world, workingPos, stateToDig);
			// Add drops from IInventory
			if (blockToDig instanceof IInventory)
			{
				IInventory inv = (IInventory) blockToDig;
				for (int i = 0; i < inv.getSizeInventory(); ++i)
				{
					ItemStack stack = inv.getStackInSlot(i);
					if (!ItemStackUtil.isNullStack(stack)) drops.add(stack);
				}
			}
			for (ItemStack stack : drops)
			{
				if (!hasChest) spawnDigged(stack);
				else if (hasChest)
				{
					ItemStack leftItems = InventoryHelper.putStackInInventoryAllSlots(getOutputInventory().getInventory(), stack, getOutputFacing());
					if (ItemStackUtil.isNullStack(leftItems)) break;
					if (ItemStackUtil.getStackSize(leftItems) > 0) spawnDigged(leftItems);
				}
			}
			world.setBlockToAir(workingPos);
			decreaseTicks();
		}
		// go down by 1 at the end of work in this tick
		workingPos = moveWorkingPosToNextPos();
	}
	
	/**
	 * Rotates the given facing the number of given times and returns this facing after rotation. <br>
	 * This will only rotate Horizontally.
	 * 
	 * @see {@link net.minecraft.util.EnumFacing.Plane#HORIZONTAL}
	 */
	public EnumFacing rotateY(EnumFacing startFace, int times)
	{
		for (int i = 0; i < times; ++i) startFace = startFace.rotateY();
		return startFace;
	}
	
	public void goToNextPosAfterHitBedrock()
	{
		workingPos = new BlockPos(workingPos.getX(), startPos.getY(), workingPos.getZ()).offset(_startPosFacing);
		if (BlockPosUtil.distanceInLine(workingPos, startPos) > _quarrySize)
		{
			startPos = startPos.offset(rotateY(_startPosFacing, 3));
			workingPos = BlockPosUtil.copyPos(startPos);
		}
	}

	public EnumFacing getOutputFacing() 
	{
		return _startPosFacing.rotateY();
	}
	
	public void forceChunkLoading(Ticket ticket)
	{
		super.forceChunkLoading(ticket);
		ChunkPos startChunk = new ChunkPos(startPos.getX() >> 4, startPos.getZ() >> 4);
		ForgeChunkManager.forceChunk(ticket, startChunk);
		ChunkPos workingChunk = new ChunkPos(workingPos.getX() >> 4, workingPos.getZ() >> 4);
		ForgeChunkManager.forceChunk(ticket, workingChunk);
	}
}