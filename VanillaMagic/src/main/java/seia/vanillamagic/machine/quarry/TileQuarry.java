package seia.vanillamagic.machine.quarry;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.machine.TileMachine;
import seia.vanillamagic.utils.BlockPosHelper;
import seia.vanillamagic.utils.InventoryHelper;

public class TileQuarry extends TileMachine
{
	// It's (size)x(size) but (size-2)x(size-2) is for digging
	// ChunkNumber * BlocksInChunk + 2 blocks bounding
	public static final int BASIC_QUARRY_SIZE = 4 * 16 + 2;
	// Input side from the Quarry into IInventory (argument in methods)
	public static final EnumFacing INPUT_FACING = EnumFacing.NORTH;
	// The name for registry
	public static final String REGISTRY_NAME = TileQuarry.class.getSimpleName();
	
	public BlockCauldron cauldron;
	public BlockPos diamondBlockPos;
	public Block diamondBlock;
	public BlockPos redstoneBlockPos;
	public Block redstoneBlock;

	private Random rand = new Random();
	
	public void init(World world, BlockPos machinePos)
	{
		super.init(world, machinePos);
		this.cauldron = (BlockCauldron) worldObj.getBlockState(machinePos).getBlock();
		this.diamondBlockPos = new BlockPos(machinePos.getX() + 1, machinePos.getY(), machinePos.getZ());
		this.diamondBlock = worldObj.getBlockState(diamondBlockPos).getBlock();
		this.redstoneBlockPos = new BlockPos(machinePos.getX(), machinePos.getY(), machinePos.getZ() - 1);
		this.redstoneBlock = worldObj.getBlockState(redstoneBlockPos).getBlock();
		this.workingPos = new BlockPos(machinePos.getX() - 1, machinePos.getY(), machinePos.getZ() + 1);
		this.startPos = BlockPosHelper.copyPos(workingPos);
	}
	
	public BlockPos getTopPos()
	{
		return new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() + BASIC_QUARRY_SIZE - 1);
	}
	
	public BlockPos getLeftPos()
	{
		return new BlockPos(this.pos.getX() - BASIC_QUARRY_SIZE + 1, this.pos.getY(), this.pos.getZ());
	}
	
	public BlockPos getTopLeftPos()
	{
		return new BlockPos(this.pos.getX() - BASIC_QUARRY_SIZE + 1, this.pos.getY(), this.pos.getZ() + BASIC_QUARRY_SIZE - 1);
	}
	
	public BlockPos getworkingPos()
	{
		return new BlockPos(workingPos.getX(), workingPos.getY(), workingPos.getZ());
	}
	
	public BlockPos getInventoryOutputPos()
	{
		return new BlockPos(this.pos.getX() - 1, this.pos.getY(), this.pos.getZ());
	}
	
	public IInventory getOutputInventory()
	{
		return ((IInventory) worldObj.getTileEntity(getInventoryOutputPos()));
	}
	
	public BlockPos getInputFuelChestPos()
	{
		return new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
	}
	
	public IInventory getInputInventory()
	{
		return ((IInventory) worldObj.getTileEntity(getInputFuelChestPos()));
	}
	
	/**
	 * Method for checking DiamondBlock and RedstoneBlock
	 */
	public boolean checkSurroundings()
	{
		if(!Block.isEqualTo(diamondBlock, worldObj.getBlockState(diamondBlockPos).getBlock()))
		{
			return false;
		}
		if(!Block.isEqualTo(redstoneBlock, worldObj.getBlockState(redstoneBlockPos).getBlock()))
		{
			return false;
		}
		return true;
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
	
	public void endWork()
	{
		this.worldObj.removeTileEntity(this.pos);
	}
	
	public boolean inventoryOutputHasSpace()
	{
		return !InventoryHelper.isInventoryFull(getOutputInventory(), INPUT_FACING);
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
		
//		if(workingPos.getX() == getLeftPos().getX())
//		{
//			endWork();
//			return;
//		}
		
		if(worldObj.isAirBlock(workingPos)) 
		{
			while(!worldObj.isAirBlock(workingPos))
			{
				workingPos = moveWorkingPosToNextPos();
			}
		}
		else if(Block.isEqualTo(worldObj.getBlockState(workingPos).getBlock(), Blocks.BEDROCK))
		{
			int nextCoordX = workingPos.getX();
			int nextCoordZ = workingPos.getZ();
			nextCoordZ++;
			if(nextCoordZ >= getTopPos().getZ())
			{
				nextCoordX++;
				nextCoordZ = this.pos.getZ() + 1;
			}
			// kill quarry because it's outside the rectangle
//			if(nextCoordX <= getLeftPos().getX() - 1)
//			{
//				endWork();
//				return;
//			}
			workingPos = new BlockPos(nextCoordX, this.pos.getY(), nextCoordZ);
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
					ItemStack leftItems = InventoryHelper.putStackInInventoryAllSlots(getOutputInventory(), stack, INPUT_FACING);
					try
					{
						if(leftItems.stackSize > 0)
						{
							spawnDigged(leftItems);
						}
					}
					catch(Exception e)
					{
					}
				}
			}
		}
		// go down by 1 at the end of work in this tick
		workingPos = moveWorkingPosToNextPos();
	}
	
	public EnumFacing getOutputFacing() 
	{
		return INPUT_FACING;
	}
}