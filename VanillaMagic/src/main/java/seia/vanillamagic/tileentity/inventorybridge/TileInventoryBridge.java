package seia.vanillamagic.tileentity.inventorybridge;

import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import seia.vanillamagic.api.exception.NotInventoryException;
import seia.vanillamagic.api.inventory.IInventoryWrapper;
import seia.vanillamagic.api.inventory.InventoryWrapper;
import seia.vanillamagic.api.tileentity.inventorybridge.IInventoryBridge;
import seia.vanillamagic.inventory.InventoryHelper;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.item.inventoryselector.ItemInventorySelector;
import seia.vanillamagic.tileentity.CustomTileEntity;
import seia.vanillamagic.util.BlockPosHelper;
import seia.vanillamagic.util.NBTHelper;
import seia.vanillamagic.util.WorldHelper;

public class TileInventoryBridge extends CustomTileEntity implements IInventoryBridge
{
	public static final String REGISTRY_NAME = TileInventoryBridge.class.getName();
	
	/*
	 * Input Inventory.
	 */
	@Nullable
	protected IInventoryWrapper inputInvWrapper;
	
	/**
	 * Inventory under this Tile.
	 */
	@Nullable
	protected IInventoryWrapper outputInvWrapper;
	
	protected int inputInvX, inputInvY, inputInvZ, inputInvDim;
	protected int outputInvX, outputInvY, outputInvZ, outputInvDim;
	
	/**
	 * Used ONLY in loading from file.
	 */
	public void addToTickable()
	{
		worldObj.tickableTileEntities.add(this);
	}
	
	@Nullable
	public IInventoryWrapper getInputInventory()
	{
		return inputInvWrapper;
	}
	
	@Nullable
	public IInventoryWrapper getOutputInventory()
	{
		return outputInvWrapper;
	}
	
	public List<String> getAdditionalInfo()
	{
		List<String> list = super.getAdditionalInfo();
		list.add("Input:  X=" + inputInvX + ", Y=" + inputInvY + ", Z=" + inputInvZ + ", Dim=" + inputInvDim);
		list.add("Output: X=" + outputInvX + ", Y=" + outputInvY + ", Z=" + outputInvZ + ", Dim=" + outputInvDim);
		return list;
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
		compound.setInteger("inputInvX", inputInvX);
		compound.setInteger("inputInvY", inputInvY);
		compound.setInteger("inputInvZ", inputInvZ);
		compound.setInteger("inputInvDim", inputInvDim);
		
		compound.setInteger("outputInvX", outputInvX);
		compound.setInteger("outputInvY", outputInvY);
		compound.setInteger("outputInvZ", outputInvZ);
		compound.setInteger("outputInvDim", outputInvDim);
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
		inputInvX = compound.getInteger("inputInvX");
		inputInvY = compound.getInteger("inputInvY");
		inputInvZ = compound.getInteger("inputInvZ");
		inputInvDim = compound.getInteger("inputInvDim");
		BlockPos inputPos = new BlockPos(inputInvX, inputInvY, inputInvZ);
		try
		{
			inputInvWrapper = new InventoryWrapper(DimensionManager.getWorld(inputInvDim), inputPos);
		}
		catch(NotInventoryException e)
		{
			BlockPosHelper.printCoords(Level.ERROR, "NotInventoryException at: ", inputPos);
			e.printStackTrace();
		}
		
		outputInvX = compound.getInteger("outputInvX");
		outputInvY = compound.getInteger("outputInvY");
		outputInvZ = compound.getInteger("outputInvZ");
		outputInvDim = compound.getInteger("outputInvDim");
		BlockPos outputPos = new BlockPos(outputInvX, outputInvY, outputInvZ);
		try
		{
			outputInvWrapper = new InventoryWrapper(DimensionManager.getWorld(outputInvDim), outputPos);
		}
		catch(NotInventoryException e)
		{
			BlockPosHelper.printCoords(Level.ERROR, "NotInventoryException at: ", outputPos);
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the input position based on the {@link ItemInventorySelector}'s NBT saved data. 
	 */
	public void setPositionFromSelector(EntityPlayer player) throws NotInventoryException 
	{
		setPositionFromSelector(player.inventory);
	}
	
	public void setPositionFromSelector(InventoryPlayer invPlayer) throws NotInventoryException 
	{
		setPositionFromSelector(invPlayer.mainInventory);
	}
	
	public void setPositionFromSelector(ItemStack[] mainInventory) throws NotInventoryException 
	{
		for(ItemStack currentCheckingStack : mainInventory)
		{
			if(VanillaMagicItems.INSTANCE.isCustomItem(currentCheckingStack, VanillaMagicItems.INSTANCE.itemInventorySelector))
			{
				NBTTagCompound currentCheckingStackTag = currentCheckingStack.getTagCompound();
				if(currentCheckingStackTag != null)
				{
					BlockPos savedPosition = NBTHelper.getBlockPosDataFromNBT(currentCheckingStackTag);
					World world = DimensionManager.getWorld(NBTHelper.getDimensionFromNBT(currentCheckingStackTag));
					inputInvWrapper = new InventoryWrapper(world, savedPosition);
					inputInvX = savedPosition.getX();
					inputInvY = savedPosition.getY();
					inputInvZ = savedPosition.getZ();
					inputInvDim = WorldHelper.getDimensionID(world);
				}
			}
		}
	}
	
	public void setOutputInventory(World world, BlockPos pos) throws NotInventoryException
	{
		outputInvWrapper = new InventoryWrapper(world, pos);
		outputInvX = pos.getX();
		outputInvY = pos.getY();
		outputInvZ = pos.getZ();
		outputInvDim = WorldHelper.getDimensionID(world);
	}
	
	public EnumFacing getInputFacing()
	{
		return EnumFacing.UP;
	}
	
	/**
	 * Currently transporting slot
	 */
	int slotNumber = 0;
	public void update()
	{
		IInventory inv = inputInvWrapper.getInventory();
		if(slotNumber >= inv.getSizeInventory())
		{
			slotNumber = 0;
		}
		ItemStack transportingStack = inv.getStackInSlot(slotNumber);
		if(transportingStack == null)
		{
			slotNumber++;
			return;
		}
		ItemStack leftItems = InventoryHelper.putStackInInventoryAllSlots(outputInvWrapper.getInventory(), transportingStack, getInputFacing());
		inv.setInventorySlotContents(slotNumber, leftItems);
		slotNumber++;
	}
}