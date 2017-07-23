package seia.vanillamagic.tileentity.machine;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import seia.vanillamagic.api.event.EventMachine;
import seia.vanillamagic.api.exception.NotInventoryException;
import seia.vanillamagic.api.inventory.IInventoryWrapper;
import seia.vanillamagic.api.inventory.InventoryWrapper;
import seia.vanillamagic.api.tileentity.machine.IMachine;
import seia.vanillamagic.config.VMConfig;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.inventory.InventoryHelper;
import seia.vanillamagic.tileentity.CustomTileEntity;
import seia.vanillamagic.util.BlockPosUtil;
import seia.vanillamagic.util.ItemStackUtil;
import seia.vanillamagic.util.NBTUtil;
import seia.vanillamagic.util.SmeltingUtil;
import seia.vanillamagic.util.TextUtil;
import seia.vanillamagic.util.WorldUtil;

public abstract class TileMachine extends CustomTileEntity implements IMachine
{
	public static final String REGISTRY_NAME = TileMachine.class.getSimpleName();
	
	protected BlockPos workingPos;
	protected BlockPos startPos;
	protected ItemStack shouldBeInLeftHand;
	protected ItemStack shouldBeInRightHand;
	protected int radius = 4;
	protected int oneOperationCost = VMConfig.TILE_MACHINE_ONE_OPERATION_COST;
	protected int ticks = 0;
	protected int maxTicks = VMConfig.TILE_MACHINE_MAX_TICKS;
	protected boolean isActive = false;
	protected boolean finished = false;
	protected int delayInTicks = 0;
	protected boolean needsFuel = true;
	protected IInventoryWrapper inventoryInput;
	protected IInventoryWrapper inventoryOutput;
	protected BlockPos chestPosInput;
	protected BlockPos chestPosOutput;
	
	/**
	 * This should check if  the Machine is build correctly.
	 */
	public abstract boolean checkSurroundings();
	
	/**
	 * This method is for a Machine work.
	 * Each Machine have to know when to decrease ticks.
	 * For instance: I don't want Quarry to decrease ticks if  it hits Air or Bedrock. <br>
	 * In this method Machine should, for instance: move workingPos.
	 */
	public abstract void doWork();
	
	public boolean inventoryOutputHasSpace() 
	{
		IInventoryWrapper outInv = getOutputInventory();
		if (outInv == null) return false;
		return !InventoryHelper.isInventoryFull(outInv.getInventory(), getOutputFacing());
	}
	
	public void init(World world, BlockPos machinePos)
	{
		super.init(world, machinePos);
		setWorkRadius(radius);
		setWorkingPos(BlockPosUtil.copyPos(machinePos));
	}
	
	int delay = 0;
	public void update()
	{
		if (delay >= delayInTicks)
		{
			delay = 0;
			if (world.getChunkFromBlockCoords(getMachinePos()).isLoaded())
			{
				if (getWorkingPos() == null) return;
				
				if (world.getChunkFromBlockCoords(getWorkingPos()).isLoaded())
				{
					if (checkSurroundings())
					{
						showBoundingBox();
						if (needsFuel) checkFuel();
						if (getInputInventory() == null || getOutputInventory() == null) return;
						tryWork();
					}
				}
			}
			isActive = false;
		}
		else
			delay++;
	}
	
	private void tryWork()
	{
		if (ticks >= oneOperationCost)
		{
			if (!MinecraftForge.EVENT_BUS.post(new EventMachine.Work(this, world, pos)))
			{
				isActive = true;
				performAdditionalOperations();
				doWork();
			}
		}
	}
	
	public IInventoryWrapper getInputInventory()
	{
		return inventoryInput;
	}
	
	public void setInputInventory(IInventoryWrapper inv)
	{
		this.inventoryInput = inv;
	}
	
	public IInventoryWrapper getOutputInventory()
	{
		return inventoryOutput;
	}
	
	public void setOutputInventory(IInventoryWrapper inv)
	{
		this.inventoryOutput = inv;
	}
	
	/**
	 * Method which is execute before the right doWork()
	 * @see seia.vanillamagic.machine.TileMachine#doWork()
	 */
	protected void performAdditionalOperations()
	{
	}
	
	protected void decreaseTicks()
	{
		ticks -= oneOperationCost;
		if (ticks < oneOperationCost) isActive = false;
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
	
	public void setCurrentTicks(int ticks)
	{
		this.ticks = ticks;
	}

	public int getMaxTicks()
	{
		return maxTicks;
	}

	public boolean isActive()
	{
		return isActive;
	}
	
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger(NBTUtil.NBT_MACHINE_POS_X, pos.getX());
		compound.setInteger(NBTUtil.NBT_MACHINE_POS_Y, pos.getY());
		compound.setInteger(NBTUtil.NBT_MACHINE_POS_Z, pos.getZ());
		compound.setInteger(NBTUtil.NBT_DIMENSION, WorldUtil.getDimensionID(this));
		compound.setInteger(NBTUtil.NBT_WORKING_POS_X, workingPos.getX());
		compound.setInteger(NBTUtil.NBT_WORKING_POS_Y, workingPos.getY());
		compound.setInteger(NBTUtil.NBT_WORKING_POS_Z, workingPos.getZ());
		compound.setInteger(NBTUtil.NBT_RADIUS, radius);
		compound.setInteger(NBTUtil.NBT_ONE_OPERATION_COST, oneOperationCost);
		compound.setInteger(NBTUtil.NBT_TICKS, ticks);
		compound.setInteger(NBTUtil.NBT_MAX_TICKS, maxTicks);
		compound.setBoolean(NBTUtil.NBT_IS_ACTIVE, isActive);
		compound.setBoolean(NBTUtil.NBT_NEEDS_FUEL, needsFuel);
		compound.setInteger(NBTUtil.NBT_START_POS_X, startPos.getX());
		compound.setInteger(NBTUtil.NBT_START_POS_Y, startPos.getY());
		compound.setInteger(NBTUtil.NBT_START_POS_Z, startPos.getZ());
		compound.setInteger(NBTUtil.NBT_CHEST_POS_INPUT_X, chestPosInput.getX());
		compound.setInteger(NBTUtil.NBT_CHEST_POS_INPUT_Y, chestPosInput.getY());
		compound.setInteger(NBTUtil.NBT_CHEST_POS_INPUT_Z, chestPosInput.getZ());
		compound.setInteger(NBTUtil.NBT_CHEST_POS_OUTPUT_X, chestPosOutput.getX());
		compound.setInteger(NBTUtil.NBT_CHEST_POS_OUTPUT_Y, chestPosOutput.getY());
		compound.setInteger(NBTUtil.NBT_CHEST_POS_OUTPUT_Z, chestPosOutput.getZ());
		return compound;
	}
	
	public void deserializeNBT(NBTTagCompound compound)
	{
		if (this.world == null)
		{
			int dimension = compound.getInteger(NBTUtil.NBT_DIMENSION);
			WorldServer world = DimensionManager.getWorld(dimension);
			this.world = world;
		}
		int machinePosX = compound.getInteger(NBTUtil.NBT_MACHINE_POS_X);
		int machinePosY = compound.getInteger(NBTUtil.NBT_MACHINE_POS_Y);
		int machinePosZ = compound.getInteger(NBTUtil.NBT_MACHINE_POS_Z);
		this.pos = new BlockPos(machinePosX, machinePosY, machinePosZ);
		int workingPosX = compound.getInteger(NBTUtil.NBT_WORKING_POS_X);
		int workingPosY = compound.getInteger(NBTUtil.NBT_WORKING_POS_Y);
		int workingPosZ = compound.getInteger(NBTUtil.NBT_WORKING_POS_Z);
		this.workingPos = new BlockPos(workingPosX, workingPosY, workingPosZ);
		this.radius = compound.getInteger(NBTUtil.NBT_RADIUS);
		this.oneOperationCost = compound.getInteger(NBTUtil.NBT_ONE_OPERATION_COST);
		this.ticks = compound.getInteger(NBTUtil.NBT_TICKS);
		this.maxTicks = compound.getInteger(NBTUtil.NBT_MAX_TICKS);
		this.isActive = compound.getBoolean(NBTUtil.NBT_IS_ACTIVE);
		this.needsFuel = compound.getBoolean(NBTUtil.NBT_NEEDS_FUEL);
		int startPosX = compound.getInteger(NBTUtil.NBT_START_POS_X);
		int startPosY = compound.getInteger(NBTUtil.NBT_START_POS_Y);
		int startPosZ = compound.getInteger(NBTUtil.NBT_START_POS_Z);
		this.startPos = new BlockPos(startPosX, startPosY, startPosZ);
		
		int chestPosInputX = compound.getInteger(NBTUtil.NBT_CHEST_POS_INPUT_X);
		int chestPosInputY = compound.getInteger(NBTUtil.NBT_CHEST_POS_INPUT_Y);
		int chestPosInputZ = compound.getInteger(NBTUtil.NBT_CHEST_POS_INPUT_Z);
		this.chestPosInput = new BlockPos(chestPosInputX, chestPosInputY, chestPosInputZ);
		int chestPosOutputX = compound.getInteger(NBTUtil.NBT_CHEST_POS_OUTPUT_X);
		int chestPosOutputY = compound.getInteger(NBTUtil.NBT_CHEST_POS_OUTPUT_Y);
		int chestPosOutputZ = compound.getInteger(NBTUtil.NBT_CHEST_POS_OUTPUT_Z);
		this.chestPosOutput = new BlockPos(chestPosOutputX, chestPosOutputY, chestPosOutputZ);
		try
		{
			if (inventoryInput == null) inventoryInput = new InventoryWrapper(world, chestPosInput);
			if (inventoryOutput == null) inventoryOutput = new InventoryWrapper(world, chestPosOutput);
		}
		catch (NotInventoryException e)
		{
			VanillaMagic.LOGGER.log(Level.ERROR, this.getClass().getSimpleName() + 
					" - error when converting to IInventory at position: " + 
					e.position.toString());
		}
	}
	
	public boolean hasInputInventory()
	{
		return getInputInventory() != null ? true : false;
	}
	
	public void checkFuel()
	{
		if (ticks >= maxTicks) return;
		if (ticks >= oneOperationCost) return;
		
		if (hasInputInventory())
		{
			IInventory invInput = getInputInventory().getInventory();
			Object[] returnedStack = SmeltingUtil.getFuelFromInventoryAndDelete(invInput);
			if (returnedStack == null) return;
			
			ItemStack fuelToAdd = (ItemStack) returnedStack[0];
			if (ItemStackUtil.isIInventory(fuelToAdd))
			{
				try
				{
					InventoryHelper.insertStack(null, invInput, fuelToAdd, (int) returnedStack[1], EnumFacing.DOWN);
				}
				catch (ReflectiveOperationException e)
				{
					e.printStackTrace();
				}
				return;
			}
			ticks += SmeltingUtil.countTicks(fuelToAdd);
		}
		else if (!hasInputInventory())
		{
			List<EntityItem> fuelsInCauldron = SmeltingUtil.getFuelFromCauldron(world, getMachinePos());
			if (fuelsInCauldron.size() == 0) return;
			
			for (EntityItem entityItem : fuelsInCauldron)
			{
				ItemStack stack = entityItem.getItem();
				ticks += SmeltingUtil.countTicks(stack);
				if (ticks >= oneOperationCost) world.removeEntity(entityItem);
			}
		}
	}
	
	public void endWork()
	{
	}
	
	@Nullable
	public ItemStack getActivationStackLeftHand()
	{
		return shouldBeInLeftHand;
	}
	
	public void setActivationStackLeftHand(ItemStack stack) 
	{
		this.shouldBeInLeftHand = stack;
	}
	
	@Nullable
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
		return this.world.getBlockState(getMachinePos());
	}
	
	public Block getMachineBlock()
	{
		return getMachineState().getBlock();
	}
	
	public List<String> getAdditionalInfo()
	{
		List<String> list = new ArrayList<String>();
		try
		{
			list.add("Machine name: " + getClass().getSimpleName());
			list.add("Machine position: " + TextUtil.constructPositionString(getWorld(), pos));
			list.add("Start position: " + TextUtil.constructPositionString(getWorld(), startPos));
			list.add("Working position: " + TextUtil.constructPositionString(getWorld(), workingPos));
			list.add("One operation cost: " + oneOperationCost);
			list.add("Fuel left: " + ticks);
			list.add("Max fuel: " + maxTicks);
		}
		catch (Exception e)
		{
			VanillaMagic.LOGGER.log(Level.ERROR, "Error while reading Additional Information from TileMachine.");
		}
		return list;
	}
	
	@Nullable
	public TileEntity getNeighborTile(EnumFacing face)
	{
		return this.world.getTileEntity(getNeighborPos(face));
	}
	
	@Nullable
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
		return this.world.getBlockState(getNeighborPos(face));
	}
	
	public Block getNeighborBlock(EnumFacing face)
	{
		return getNeighborState(face).getBlock();
	}
}