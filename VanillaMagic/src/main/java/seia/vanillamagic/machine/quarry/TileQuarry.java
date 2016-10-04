package seia.vanillamagic.machine.quarry;

import java.util.List;
import java.util.Random;

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
import seia.vanillamagic.inventory.InventoryHelper;
import seia.vanillamagic.inventory.InventoryWrapper;
import seia.vanillamagic.machine.TileMachine;
import seia.vanillamagic.util.BlockPosHelper;

public class TileQuarry extends TileMachine implements IQuarry
{
	public static final String REGISTRY_NAME = TileQuarry.class.getSimpleName();
	
	private QuarryUpgradeHelper upgradeHelper = new QuarryUpgradeHelper(this);
	
	private BlockPos diamondBlockPos;
	private BlockPos redstoneBlockPos;
	private Random rand = new Random();
	/**
	 * It is not final, but should never be changed. <br>
	 * It is set in the checkSurroundings().
	 */
	private EnumFacing startPosFacing;
	private EnumFacing diamondFacing;
	private EnumFacing redstoneFacing;
	
	/**
	 * Forces Quarry to stop working.
	 */
	private boolean forceStop = false;
	/**
	 * It's (size)x(size) but (size-2)x(size-2) is for digging
	 * BlocksInChunk
	 * Only multiply this
	 */
	private int quarrySize = QuarrySizeHelper.BASIC_SIZE;
	
	public int getQuarrySize()
	{
		return quarrySize;
	}
	
	public EnumFacing getStartPosFacing()
	{
		return startPosFacing;
	}
	
	public void forceQuarryStop()
	{
		forceStop = true;
	}
	
	public void forceQuarryStart()
	{
		forceStop = false;
	}
	
	/**
	 * Method for checking DiamondBlock and RedstoneBlock
	 */
	public boolean checkSurroundings()
	{
		if(diamondBlockPos != null)
		{
			if(!Block.isEqualTo(Blocks.DIAMOND_BLOCK, worldObj.getBlockState(diamondBlockPos).getBlock()))
			{
				return false;
			}
		}
		if(redstoneBlockPos != null)
		{
			if(!Block.isEqualTo(Blocks.REDSTONE_BLOCK, worldObj.getBlockState(redstoneBlockPos).getBlock()))
			{
				return false;
			}
		}
		if(redstoneBlockPos == null || diamondBlockPos == null)
		{
			for(EnumFacing redstoneFacing : facings())
			{
				redstoneBlockPos = pos.offset(redstoneFacing);
				if(Block.isEqualTo(worldObj.getBlockState(redstoneBlockPos).getBlock(), Blocks.REDSTONE_BLOCK))
				{
					EnumFacing diamondFacing = redstoneFacing.rotateY();
					diamondBlockPos = pos.offset(diamondFacing);
					if(Block.isEqualTo(worldObj.getBlockState(diamondBlockPos).getBlock(), Blocks.DIAMOND_BLOCK))
					{
						if(startPos == null)
						{
							this.redstoneFacing = redstoneFacing;
							this.diamondFacing = diamondFacing;
							this.startPosFacing = diamondFacing.rotateY();
							restartDefaultStartPos();
							this.inventoryInput = new InventoryWrapper(worldObj, new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ()));
							this.inventoryOutput = new InventoryWrapper(worldObj, pos.offset(getOutputFacing()));
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
		startPos = pos.offset(startPosFacing);
		workingPos = pos.offset(startPosFacing);
	}
	
	public EnumFacing[] facings()
	{
		return EnumFacing.Plane.HORIZONTAL.facings();
	}
	
	/**
	 * will return if quarry can dig next block
	 */
	public boolean canDig()
	{
		if(ticks >= oneOperationCost)
		{
			return true;
		}
		return false;
	}
	
	public boolean inventoryOutputHasSpace()
	{
		return !InventoryHelper.isInventoryFull(getOutputInventory().getInventory(), getOutputFacing());
	}
	
	public void spawnDigged(ItemStack digged)
	{
		Block.spawnAsEntity(worldObj, this.pos.offset(EnumFacing.UP, 2), digged);
	}
	
	public BlockPos moveWorkingPosToNextPos()
	{
		return workingPos.offset(EnumFacing.DOWN);
	}
	
	int diamondBlocks = 0;
	/**
	 * Here I want to check for the number of IQuarryUpgrades next to the Quarry. <br>
	 * Count them and change the Quarry size.
	 */
	protected void performAdditionalOperations()
	{
		BlockPos cauldronPos = BlockPosHelper.copyPos(this.getMachinePos());
		cauldronPos = cauldronPos.offset(diamondFacing);
		diamondBlocks = 0;
		IBlockState checkingBlock = this.getWorld().getBlockState(cauldronPos);
		while((Block.isEqualTo(checkingBlock.getBlock(), Blocks.DIAMOND_BLOCK)) // quarry resizing is not an actual upgrade, it's easier to count it here
				|| (QuarryUpgradeRegistry.isUpgradeBlock(checkingBlock.getBlock())))
		{
			if(Block.isEqualTo(checkingBlock.getBlock(), Blocks.DIAMOND_BLOCK))
			{
				diamondBlocks++;
			}
			else // if(QuarryUpgradeRegistry.isUpgradeBlock(checkingBlock.getBlock()))
			{
				upgradeHelper.addUpgradeFromBlock(checkingBlock.getBlock());
			}
			cauldronPos = cauldronPos.offset(diamondFacing);
			checkingBlock = this.getWorld().getBlockState(cauldronPos);
		}
		upgradeHelper.modifyQuarry(this);
		quarrySize = QuarrySizeHelper.getSize(diamondBlocks);
	}
	
	public List<String> getAdditionalInfo()
	{
		List<String> baseInfo = super.getAdditionalInfo();
		baseInfo.add("Quarry Size: " + quarrySize + " blocks");
		performAdditionalOperations();
		List<String> withUpgrades = upgradeHelper.addAdditionalInfo(baseInfo);
		upgradeHelper.clearUpgrades();
		countRedstoneBlocks();
		baseInfo.add("Mining blocks per tick: " + redstoneBlocks);
		return withUpgrades;
	}
	
	/**
	 * Returns the list of the drops from the block.
	 */
	public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos, IBlockState workingPosState)
	{
		return upgradeHelper.getDrops(blockToDig, world, workingPos, workingPosState);
	}
	
	public void doWork() // once a world tick
	{
		// If forced to stop, stop.
		if(forceStop)
		{
			return;
		}
		// Counting the number of blocks
		countRedstoneBlocks();
		// Memory efficiency.
		int redstoneBlocks = this.redstoneBlocks;
		for(int i = 0; i < redstoneBlocks; i++)
		{
			performOneOperation();
		}
		upgradeHelper.clearUpgrades();
	}
	
	int redstoneBlocks = 0;
	/**
	 * Counts the number of RedstoneBlocks in line.
	 */
	public void countRedstoneBlocks()
	{
		BlockPos cauldronPos = BlockPosHelper.copyPos(this.getMachinePos());
		cauldronPos = cauldronPos.offset(redstoneFacing);
		redstoneBlocks = 0;
		IBlockState checkingBlock = this.getWorld().getBlockState(cauldronPos);
		while(Block.isEqualTo(checkingBlock.getBlock(), Blocks.REDSTONE_BLOCK))
		{
			redstoneBlocks++;
			cauldronPos = cauldronPos.offset(redstoneFacing);
			checkingBlock = this.getWorld().getBlockState(cauldronPos);
		}
	}
	
	/**
	 * One Quarry operation.
	 */
	public void performOneOperation()
	{
		if(!canDig())
		{
			return;
		}
		if(worldObj.isAirBlock(workingPos)) 
		{
			while(!worldObj.isAirBlock(workingPos))
			{
				workingPos = moveWorkingPosToNextPos();
			}
		}
		else if(Block.isEqualTo(worldObj.getBlockState(workingPos).getBlock(), Blocks.BEDROCK))
		{
			goToNextPosAfterHitBedrock();
		}
		else // dig something like stone, iron-ore, etc.
		{
			boolean hasChest = isNextToOutput(); // chest or any other IInventory
			Block blockToDig = worldObj.getBlockState(workingPos).getBlock();
			List<ItemStack> drops = getDrops(blockToDig, worldObj, workingPos, worldObj.getBlockState(workingPos)); // blockToDig.getDrops(worldObj, workingPos, worldObj.getBlockState(workingPos), 0);
			// Add drops from IInventory
			if(blockToDig instanceof IInventory)
			{
				IInventory inv = (IInventory) blockToDig;
				for(int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack stack = inv.getStackInSlot(i);
					if(stack != null)
					{
						drops.add(stack);
					}
				}
			}
			worldObj.setBlockToAir(workingPos);
			for(ItemStack stack : drops)
			{
				if(!hasChest)
				{
					spawnDigged(stack);
				}
				else if(hasChest)
				{
					ItemStack leftItems = InventoryHelper.putStackInInventoryAllSlots(getOutputInventory().getInventory(), stack, getOutputFacing());
					if(leftItems == null)
					{
						break;
					}
					if(leftItems.stackSize > 0)
					{
						spawnDigged(leftItems);
					}
				}
			}
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
		for(int i = 0; i < times; i++)
		{
			startFace = startFace.rotateY();
		}
		return startFace;
	}
	
	public void goToNextPosAfterHitBedrock()
	{
		workingPos = new BlockPos(workingPos.getX(), startPos.getY(), workingPos.getZ()).offset(startPosFacing);
		if(BlockPosHelper.distanceInLine(workingPos, startPos) > quarrySize)
		{
			startPos = startPos.offset(rotateY(startPosFacing, 3));
			workingPos = BlockPosHelper.copyPos(startPos);
		}
	}

	public EnumFacing getOutputFacing() 
	{
		return startPosFacing.rotateY();
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