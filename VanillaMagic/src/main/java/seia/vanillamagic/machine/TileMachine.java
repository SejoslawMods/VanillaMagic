package seia.vanillamagic.machine;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.utils.CustomTileEntity;
import seia.vanillamagic.utils.InventoryHelper;
import seia.vanillamagic.utils.NBTHelper;
import seia.vanillamagic.utils.SmeltingHelper;

public abstract class TileMachine extends CustomTileEntity implements IMachine
{
	public static final String REGISTRY_NAME = TileMachine.class.getSimpleName();
	
	protected BlockPos workingPos;
	protected BlockPos startPos;
	protected ItemStack shouldBeInLeftHand;
	protected ItemStack shouldBeInRightHand;
	protected int radius = 4;
	protected int oneOperationCost = 400;
	protected int ticks = 0;
	protected int maxTicks = 4000;
	protected boolean isActive = false;
	protected boolean finished = false;
	protected int delayInTicks = 0;
	protected boolean needsFuel = true;
	
	/**
	 * This should check if the Machine is build correctly.
	 */
	public abstract boolean checkSurroundings();
	
	/**
	 * This method is for a Machine work.
	 * Each Machine have to know when to decrease ticks.
	 * For instance: I don't want Quarry to decrease ticks if it hits Air or Bedrock. <br>
	 * In this method Machine should, for instance: move workingPos.
	 */
	public abstract void doWork();
	
	/**
	 * This method is used to check if the output inventory has space for more items.
	 */
	public abstract EnumFacing getOutputFacing();
	
	public boolean inventoryOutputHasSpace() 
	{
		return !InventoryHelper.isInventoryFull(getOutputInventory(), getOutputFacing());
	}
	
	public void init(World world, BlockPos machinePos)
	{
		super.init(world, machinePos);
		setWorkRadius(radius);
		setWorkingPos(machinePos);
	}
	
	int delay = 0;
	public void update()
	{
		if(delay >= delayInTicks)
		{
			delay = 0;
			if(worldObj.getChunkFromBlockCoords(getMachinePos()).isLoaded())
			{
				if(worldObj.getChunkFromBlockCoords(getWorkingPos()).isLoaded())
				{
					if(checkSurroundings())
					{
						showBoundingBox();
						if(needsFuel)
						{
							checkFuel();
						}
						
						if(isNextToOutput())
						{
							if(inventoryOutputHasSpace())
							{
								tryWork();
							}
						}
						else
						{
							tryWork();
						}
					}
				}
			}
			isActive = false;
		}
		else
		{
			delay++;
		}
	}
	
	private void tryWork()
	{
		if(ticks >= oneOperationCost)
		{
			isActive = true;
			doWork();
			return;
		}
	}
	
	protected void decreaseTicks()
	{
		ticks -= oneOperationCost;
		if(ticks < oneOperationCost)
		{
			isActive = false;
		}
	}
	
	public TileEntity getTileEntity()
	{
		return this;
	}
	
	/**
	 * Additional method for showing the box on which Machine operates.
	 */
	public void showBoundingBox()
	{
	}
	
	public boolean finishedWork()
	{
		return finished;
	}

	public boolean isNextToOutput()
	{
		return getOutputInventory() != null ? true : false;
	}
	
	public void setWorld(World world)
	{
		this.worldObj = world;
	}

	public BlockPos getMachinePos()
	{
		return this.pos;
	}

	public void setMachinePos(BlockPos newPos)
	{
		this.pos = newPos;
	}

	public BlockPos getWorkingPos()
	{
		return workingPos;
	}

	public void setWorkingPos(BlockPos newPos)
	{
		this.workingPos = newPos;
	}
	
	public BlockPos getStartPos()
	{
		return startPos;
	}
	
	public void setNewStartPos(BlockPos newStartPos)
	{
		this.startPos = newStartPos;
	}

	public int getWorkRadius()
	{
		return radius;
	}
	
	public void setWorkRadius(int newRadius)
	{
		this.radius = newRadius;
	}

	public int getOneOperationCost()
	{
		return oneOperationCost;
	}

	public int getCurrentTicks()
	{
		return ticks;
	}

	public int getMaxTicks()
	{
		return maxTicks;
	}

	public boolean isActive()
	{
		return isActive;
	}
	
	/**
	 * Try to override serializeNBT instead of this method.
	 */
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
		super.writeToNBT(compound);
		compound = NBTHelper.writeToINBTSerializable(this, compound);
		return compound;
    }
	
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger(NBTHelper.NBT_MACHINE_POS_X, this.pos.getX());
		compound.setInteger(NBTHelper.NBT_MACHINE_POS_Y, this.pos.getY());
		compound.setInteger(NBTHelper.NBT_MACHINE_POS_Z, this.pos.getZ());
		compound.setInteger(NBTHelper.NBT_WORKING_POS_X, workingPos.getX());
		compound.setInteger(NBTHelper.NBT_WORKING_POS_Y, workingPos.getY());
		compound.setInteger(NBTHelper.NBT_WORKING_POS_Z, workingPos.getZ());
		compound.setInteger(NBTHelper.NBT_RADIUS, radius);
		compound.setInteger(NBTHelper.NBT_ONE_OPERATION_COST, oneOperationCost);
		compound.setInteger(NBTHelper.NBT_TICKS, ticks);
		compound.setInteger(NBTHelper.NBT_MAX_TICKS, maxTicks);
		compound.setBoolean(NBTHelper.NBT_IS_ACTIVE, isActive);
		compound.setBoolean(NBTHelper.NBT_NEEDS_FUEL, needsFuel);
		return compound;
	}
	
	/**
	 * Try to override deserializeNBT instead of this method.
	 */
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
		NBTHelper.readFromINBTSerializable(this, compound);
    }
	
	public void deserializeNBT(NBTTagCompound compound)
	{
		int machinePosX = compound.getInteger(NBTHelper.NBT_MACHINE_POS_X);
		int machinePosY = compound.getInteger(NBTHelper.NBT_MACHINE_POS_Y);
		int machinePosZ = compound.getInteger(NBTHelper.NBT_MACHINE_POS_Z);
		BlockPos machinePos = new BlockPos(machinePosX, machinePosY, machinePosZ);
		this.pos = machinePos;
		int workingPosX = compound.getInteger(NBTHelper.NBT_WORKING_POS_X);
		int workingPosY = compound.getInteger(NBTHelper.NBT_WORKING_POS_Y);
		int workingPosZ = compound.getInteger(NBTHelper.NBT_WORKING_POS_Z);
		BlockPos workingPos = new BlockPos(workingPosX, workingPosY, workingPosZ);
		this.workingPos = workingPos;
		this.radius = compound.getInteger(NBTHelper.NBT_RADIUS);
		this.oneOperationCost = compound.getInteger(NBTHelper.NBT_ONE_OPERATION_COST);
		this.ticks = compound.getInteger(NBTHelper.NBT_TICKS);
		this.maxTicks = compound.getInteger(NBTHelper.NBT_MAX_TICKS);
		this.isActive = compound.getBoolean(NBTHelper.NBT_IS_ACTIVE);
		this.needsFuel = compound.getBoolean(NBTHelper.NBT_NEEDS_FUEL);
	}
	
	public boolean hasInputInventory()
	{
		return getInputInventory() != null ? true : false;
	}
	
	public void checkFuel()
	{
		if(isNextToOutput())
		{
			if(!inventoryOutputHasSpace())
			{
				return;
			}
		}
		if(ticks >= maxTicks)
		{
			return;
		}
		if(ticks >= oneOperationCost)
		{
			return;
		}
		
		if(hasInputInventory())
		{
			ItemStack fuelToAdd = SmeltingHelper.getFuelFromInventoryAndDelete(getInputInventory());
			if(fuelToAdd == null)
			{
				return;
			}
			ticks += SmeltingHelper.countTicks(fuelToAdd);
		}
		else if(!hasInputInventory())
		{
			List<EntityItem> fuelsInCauldron = SmeltingHelper.getFuelFromCauldron(worldObj, getMachinePos());
			if(fuelsInCauldron.size() == 0)
			{
				return;
			}
			for(EntityItem entityItem : fuelsInCauldron)
			{
				ItemStack stack = entityItem.getEntityItem();
				ticks += SmeltingHelper.countTicks(stack);
				if(ticks >= oneOperationCost)
				{
					worldObj.removeEntity(entityItem);
				}
			}
		}
	}
	
	public void endWork()
	{
	}
	
	public ItemStack getActivationStackLeftHand()
	{
		return shouldBeInLeftHand;
	}
	
	public void setActivationStackLeftHand(ItemStack stack) 
	{
		this.shouldBeInLeftHand = stack;
	}
	
	public ItemStack getActivationStackRightHand()
	{
		return shouldBeInRightHand;
	}
	
	public void setActivationStackRightHand(ItemStack stack) 
	{
		this.shouldBeInRightHand = stack;
	}
	
	public IBlockState getMachineState()
	{
		return this.worldObj.getBlockState(getMachinePos());
	}
	
	public Block getMachineBlock()
	{
		return getMachineState().getBlock();
	}
	
	public TileEntity getNeighborTile(EnumFacing face)
	{
		return this.worldObj.getTileEntity(getNeighborPos(face));
	}
	
	public IMachine getNeighborMachine(EnumFacing face)
	{
		return (IMachine) getNeighborTile(face);
	}
	
	public BlockPos getNeighborPos(EnumFacing face)
	{
		return getMachinePos().offset(face);
	}
	
	public IBlockState getNeighborState(EnumFacing face)
	{
		return this.worldObj.getBlockState(getNeighborPos(face));
	}
	
	public Block getNeighborBlock(EnumFacing face)
	{
		return getNeighborState(face).getBlock();
	}
}