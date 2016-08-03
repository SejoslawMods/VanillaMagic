package seia.vanillamagic.quest.quarry;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.utils.BlockPosHelper;
import seia.vanillamagic.utils.InventoryHelper;
import seia.vanillamagic.utils.SmeltingHelper;

public class TileQuarry extends TileEntity implements ITickable
{
	// Cost for mining one block
	public static final int ONE_OPERATION_COST = 400;
	// Max number of ticks that Quarry can have at a time
	public static final int MAX_QUARRY_TICKS = 4000;
	// It's (size)x(size) but (size-2)x(size-2) is for digging
	// ChunkNumber * BlocksInChunk + 2 blocks bounding
	public static final int BASIC_QUARRY_SIZE = 4 * 16 + 2;
	// Input side from the Quarry into IInventory (argument in methods)
	public static final EnumFacing INPUT_FACING = EnumFacing.NORTH;
	// The name for registry
	public static final String TILE_QUARRY_NAME = "tileQuarry";

	private final String quarryPosX = "quarryPosX", quarryPosY = "quarryPosY", quarryPosZ = "quarryPosZ";
	private final String ticksNBT = "ticks";
	private final String digPosX = "digPosX", digPosY = "digPosY", digPosZ = "digPosZ";
	
	public BlockPos quarryPos;
	public EntityPlayer placedBy;
	public ItemStack itemInHand; // should be cauldron
	public World world;
	public BlockCauldron cauldron;
	public BlockPos diamondBlockPos;
	public Block diamondBlock;
	public BlockPos redstoneBlockPos;
	public Block redstoneBlock;
	
	private int ticks = 0;
	private BlockPos diggingPos;
	private Random rand = new Random();
	
	public TileQuarry(BlockPos quarryPos, EntityPlayer whoPlacedQuarry, ItemStack itemInHand) 
			throws Exception
	{
		this.quarryPos = quarryPos;
		this.placedBy = whoPlacedQuarry;
		this.itemInHand = itemInHand;
		this.world = placedBy.worldObj;
		this.cauldron = (BlockCauldron) world.getBlockState(quarryPos).getBlock();
		this.diamondBlockPos = new BlockPos(quarryPos.getX() + 1, quarryPos.getY(), quarryPos.getZ());
		this.diamondBlock = world.getBlockState(diamondBlockPos).getBlock();
		this.redstoneBlockPos = new BlockPos(quarryPos.getX(), quarryPos.getY(), quarryPos.getZ() - 1);
		this.redstoneBlock = world.getBlockState(redstoneBlockPos).getBlock();
		
		if(!Block.isEqualTo(diamondBlock, Blocks.DIAMOND_BLOCK))
		{
			throw new Exception("DiamondBlock is not in place.");
		}
		if(!Block.isEqualTo(redstoneBlock, Blocks.REDSTONE_BLOCK))
		{
			throw new Exception("RedstoneBlock is not in place.");
		}
		
		this.diggingPos = new BlockPos(
				quarryPos.getX() - 1, 
				quarryPos.getY(), 
				quarryPos.getZ() + 1);
	}

	/**
	 * @return - true if quarry is complete and right, otherwise false 
	 * (this should be achievable if the quarry isn't correctly build 
	 * - exception should be thrown when the object is creating)
	 */
	public boolean isComplete() 
	{
		return true;
	}
	
	public BlockPos getTopPos()
	{
		return new BlockPos(quarryPos.getX(), quarryPos.getY(), quarryPos.getZ() + BASIC_QUARRY_SIZE - 1);
	}
	
	public BlockPos getLeftPos()
	{
		return new BlockPos(quarryPos.getX() - BASIC_QUARRY_SIZE + 1, quarryPos.getY(), quarryPos.getZ());
	}
	
	public BlockPos getTopLeftPos()
	{
		return new BlockPos(quarryPos.getX() - BASIC_QUARRY_SIZE + 1, quarryPos.getY(), quarryPos.getZ() + BASIC_QUARRY_SIZE - 1);
	}
	
	public BlockPos getDiggingPos()
	{
		return new BlockPos(diggingPos.getX(), diggingPos.getY(), diggingPos.getZ());
	}
	
	public BlockPos getInventoryOutputPos()
	{
		return new BlockPos(quarryPos.getX() - 1, quarryPos.getY(), quarryPos.getZ());
	}
	
	public IInventory getInventoryOutput()
	{
		TileEntity output = world.getTileEntity(getInventoryOutputPos());
		if(output instanceof IInventory)
		{
			return (IInventory) output;
		}
		return null;
	}
	
	public BlockPos getInputFuelChestPos()
	{
		return new BlockPos(quarryPos.getX(), quarryPos.getY() + 1, quarryPos.getZ());
	}
	
	public IInventory getInputFuelChest()
	{
		TileEntity input = world.getTileEntity(getInputFuelChestPos());
		if(input instanceof IInventory)
		{
			return (IInventory) input;
		}
		return null;
	}
	
	public boolean hasInputFuelChest()
	{
		return getInputFuelChest() != null ? true : false;
	}
	
	/*
	 * Method for checking DiamondBlock and RedstoneBlock
	 */
	public boolean checkSurroundings()
	{
		if(!Block.isEqualTo(diamondBlock, world.getBlockState(diamondBlockPos).getBlock()))
		{
			return false;
		}
		if(!Block.isEqualTo(redstoneBlock, world.getBlockState(redstoneBlockPos).getBlock()))
		{
			return false;
		}
		return true;
	}

	/*
	 * check if quarry has fuel to dig
	 */
	public void checkFuel()
	{
		try
		{
			if(isNextToInventory())
			{
				if(!inventoryHasSpace())
				{
					return;
				}
			}
			if(ticks >= MAX_QUARRY_TICKS)
			{
				return;
			}
			if(ticks >= ONE_OPERATION_COST)
			{
				return;
			}
			
			if(hasInputFuelChest())
			{
				ItemStack fuelToAdd = SmeltingHelper.getFuelFromInventoryAndDelete(getInputFuelChest());
				ticks += SmeltingHelper.countTicks(fuelToAdd);
			}
			else if(!hasInputFuelChest())
			{
				List<EntityItem> fuelsInCauldron = SmeltingHelper.getFuelFromCauldron(world, quarryPos);
				if(fuelsInCauldron.size() == 0)
				{
					return;
				}
				for(EntityItem entityItem : fuelsInCauldron)
				{
					ItemStack stack = entityItem.getEntityItem();
					ticks += SmeltingHelper.countTicks(stack);
					if(ticks >= ONE_OPERATION_COST)
					{
						world.removeEntity(entityItem);
					}
				}
			}
		}
		catch(Exception e)
		{
		}
	}
	
	/*
	 * will return if quarry can dig next block
	 */
	public boolean canDig()
	{
		if(ticks >= ONE_OPERATION_COST)
		{
			return true;
		}
		return false;
	}
	
	public void decreaseTicks()
	{
		ticks -= ONE_OPERATION_COST;
	}

	/*
	 * TODO: fix bounding box
	 * showing the rendered particles around the digging area
	 */
	public void showBoundingBox()
	{
		/*
		int posX = quarryPos.getX();
		int posZ = quarryPos.getZ();
		for(int x = 0; x < BASIC_QUARRY_SIZE; x++)
		{
			for(int z = 0; z < BASIC_QUARRY_SIZE; z++)
			{
				if(((posX - x) == posX) || // bottom line
						((posZ + z) == posZ) || // right line
						((posX - x) == (posX - BASIC_QUARRY_SIZE - 1)) || // top line
						((posZ + z) == (posZ + BASIC_QUARRY_SIZE - 1))) // left line
				{
					world.spawnParticle(EnumParticleTypes.END_ROD,
							posX - x, 
							quarryPos.getY(), 
							posZ + z, 
							rand.nextGaussian() * 0.005D, 
							rand.nextGaussian() * 0.005D, 
							rand.nextGaussian() * 0.005D, 
							new int[0]);
				}
			}
		}
		*/
	}
	
	public void endWork()
	{
		QuarryHandler.INSTANCE.killQuarry(this);
	}
	
	/*
	 * In theory we will use chest but it should be more flexible.
	 */
	public boolean isNextToInventory()
	{
		return getInventoryOutput() != null ? true : false;
	}
	
	public boolean inventoryHasSpace()
	{
		return !InventoryHelper.isInventoryFull(getInventoryOutput(), INPUT_FACING);
	}
	
	public void spawnDigged(ItemStack digged)
	{
		Block.spawnAsEntity(world, quarryPos.offset(EnumFacing.UP, 2), digged);
	}
	
	/*
	 * updating the quarry (dig block, place block in chest)
	 */
	public void doWork() // once a world tick
	{
		if(!canDig())
		{
			return;
		}
		
//		if(diggingPos.getX() == getLeftPos().getX())
//		{
//			endWork();
//			return;
//		}
		
		if(world.isAirBlock(diggingPos)) 
		{
			while(!world.isAirBlock(diggingPos))
			{
				diggingPos = diggingPos.offset(EnumFacing.DOWN);
			}
		}
		else if(Block.isEqualTo(world.getBlockState(diggingPos).getBlock(), Blocks.BEDROCK))
		{
			int nextCoordX = diggingPos.getX();
			int nextCoordZ = diggingPos.getZ();
			nextCoordZ++;
			if(nextCoordZ >= getTopPos().getZ())
			{
				nextCoordX++;
				nextCoordZ = quarryPos.getZ() + 1;
			}
			// kill quarry because it's outside the rectangle
//			if(nextCoordX <= getLeftPos().getX() - 1)
//			{
//				endWork();
//				return;
//			}
			diggingPos = new BlockPos(nextCoordX, quarryPos.getY(), nextCoordZ);
		}
		else // dig something like stone, iron-ore, etc.
		{
			decreaseTicks();
			boolean hasChest = isNextToInventory(); // chest or any other IInventory
			Block blockToDig = world.getBlockState(diggingPos).getBlock();
			List<ItemStack> drops = blockToDig.getDrops(world, diggingPos, world.getBlockState(diggingPos), 0);
			world.setBlockToAir(diggingPos);
			for(ItemStack stack : drops)
			{
				if(!hasChest)
				{
					spawnDigged(stack);
				}
				else if(hasChest)
				{
					ItemStack leftItems = InventoryHelper.putStackInInventoryAllSlots(getInventoryOutput(), stack, INPUT_FACING);
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
		diggingPos = diggingPos.offset(EnumFacing.DOWN);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		try
		{
			super.readFromNBT(compound);
			this.ticks = compound.getInteger(ticksNBT);
			int digPosX = compound.getInteger(this.digPosX);
			int digPosY = compound.getInteger(this.digPosY);
			int digPosZ = compound.getInteger(this.digPosZ);
			BlockPos digPos = new BlockPos(digPosX, digPosY, digPosZ);
			this.diggingPos = digPos;
			int qPosX = compound.getInteger(this.quarryPosX);
			int qPosY = compound.getInteger(this.quarryPosY);
			int qPosZ = compound.getInteger(this.quarryPosZ);
			BlockPos qPos = new BlockPos(qPosX, qPosY, qPosZ);
			this.quarryPos = qPos;
			QuarryHandler.INSTANCE.addNewQuarry(this);
		}
		catch(Exception e)
		{
			System.out.println("Error while reading NBT to TileQuarry at:");
			BlockPosHelper.printCoords(quarryPos);
		}
    }
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
		try
		{
			compound.setInteger(ticksNBT, ticks);
			compound.setInteger(digPosX, diggingPos.getX());
			compound.setInteger(digPosY, diggingPos.getY());
			compound.setInteger(digPosZ, diggingPos.getZ());
			compound.setInteger(quarryPosX, quarryPos.getX());
			compound.setInteger(quarryPosY, quarryPos.getY());
			compound.setInteger(quarryPosZ, quarryPos.getZ());
			return super.writeToNBT(compound);
		}
		catch(Exception e)
		{
			System.out.println("Error while writing NBT from TileQuarry at:");
			BlockPosHelper.printCoords(quarryPos);
		}
		return null;
    }
	
	@Override
	public void update()
	{
		if(world.getChunkFromBlockCoords(quarryPos).isLoaded())
		{
			if(world.getChunkFromBlockCoords(getDiggingPos()).isLoaded())
			{
				if(checkSurroundings())
				{
					showBoundingBox();
					checkFuel();
					if(isNextToInventory())
					{
						if(inventoryHasSpace())
						{
							doWork();
						}
					}
					else
					{
						doWork();
					}
				}
			}
		}
	}
}