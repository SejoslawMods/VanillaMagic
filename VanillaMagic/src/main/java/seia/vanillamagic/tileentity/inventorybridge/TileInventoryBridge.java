package seia.vanillamagic.tileentity.inventorybridge;

import javax.annotation.Nullable;

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
import seia.vanillamagic.util.NBTHelper;

public class TileInventoryBridge extends CustomTileEntity implements IInventoryBridge
{
	public static final String REGISTRY_NAME = TileInventoryBridge.class.getSimpleName();
	
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
				}
			}
		}
	}
	
	public void setOutputInventory(World world, BlockPos pos) throws NotInventoryException
	{
		outputInvWrapper = new InventoryWrapper(world, pos);
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