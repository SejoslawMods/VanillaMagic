package seia.vanillamagic.machine.quarry;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import seia.vanillamagic.machine.TileMachine;
import seia.vanillamagic.utils.BlockPosHelper;
import seia.vanillamagic.utils.InventoryHelper;

public class TileQuarry extends TileMachine
{
	// It's (size)x(size) but (size-2)x(size-2) is for digging
	// ChunkNumber * BlocksInChunk
	public static final int BASIC_QUARRY_SIZE = 8 * 16;
	// The name for registry
	public static final String REGISTRY_NAME = TileQuarry.class.getSimpleName();
	
	private BlockPos diamondBlockPos;
	private BlockPos redstoneBlockPos;
	private Random rand = new Random();
	private EnumFacing startPosFacing;
	
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
			for(EnumFacing facing : facings())
			{
				redstoneBlockPos = pos.offset(facing);
				if(Block.isEqualTo(worldObj.getBlockState(redstoneBlockPos).getBlock(), Blocks.REDSTONE_BLOCK))
				{
					EnumFacing diamondFacing = facing.rotateY();
					diamondBlockPos = pos.offset(diamondFacing);
					if(Block.isEqualTo(worldObj.getBlockState(diamondBlockPos).getBlock(), Blocks.DIAMOND_BLOCK))
					{
						if(startPos == null)
						{
							startPosFacing = diamondFacing.rotateY();
							restartDefaultStartPos();
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
	
	public BlockPos getInventoryOutputPos()
	{
		return pos.offset(getOutputFacing());
	}
	
	@Nullable
	public IInventory getOutputInventory()
	{
		return ((IInventory) worldObj.getTileEntity(getInventoryOutputPos()));
	}
	
	public BlockPos getInputFuelChestPos()
	{
		return new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
	}
	
	@Nullable
	public IInventory getInputInventory()
	{
		return ((IInventory) worldObj.getTileEntity(getInputFuelChestPos()));
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
		return !InventoryHelper.isInventoryFull(getOutputInventory(), getOutputFacing());
	}
	
	public void spawnDigged(ItemStack digged)
	{
		Block.spawnAsEntity(worldObj, this.pos.offset(EnumFacing.UP, 2), digged);
	}
	
	public BlockPos moveWorkingPosToNextPos()
	{
		return workingPos.offset(EnumFacing.DOWN);
	}
	
	public void doWork() // once a world tick
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
			List<ItemStack> drops = blockToDig.getDrops(worldObj, workingPos, worldObj.getBlockState(workingPos), 0);
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
					ItemStack leftItems = InventoryHelper.putStackInInventoryAllSlots(getOutputInventory(), stack, getOutputFacing());
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
		if(BlockPosHelper.distanceInLine(workingPos, startPos) > BASIC_QUARRY_SIZE)
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