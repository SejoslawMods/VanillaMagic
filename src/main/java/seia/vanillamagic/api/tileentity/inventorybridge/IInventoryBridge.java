package seia.vanillamagic.api.tileentity.inventorybridge;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import seia.vanillamagic.api.exception.NotInventoryException;
import seia.vanillamagic.api.inventory.IInventoryWrapper;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;

/**
 * This is the connection to TileInventoryBridge.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IInventoryBridge extends ICustomTileEntity {
	/**
	 * @return Returns the wrapper which contains information about the input
	 *         Inventory.
	 */
	@Nullable
	public IInventoryWrapper getInputInventory();

	/**
	 * @return Returns the wrapper which contains information about the output /
	 *         bottom Inventory.
	 */
	@Nullable
	public IInventoryWrapper getOutputInventory();

	/**
	 * Sets the input position based on the ItemInventorySelector's NBT saved data.
	 * 
	 * @throws NotInventoryException if the selector has saved wrong NBT data about
	 *                               an Inventory OR the Inventory at saved position
	 *                               was broken.
	 */
	public void setPositionFromSelector(EntityPlayer player) throws NotInventoryException;

	/**
	 * @see #setPositionFromSelector(EntityPlayer)
	 */
	public void setPositionFromSelector(InventoryPlayer invPlayer) throws NotInventoryException;

	/**
	 * @see #setPositionFromSelector(EntityPlayer)
	 */
	public void setPositionFromSelector(NonNullList<ItemStack> mainInventory) throws NotInventoryException;
	
	// TODO: Add setPositionFromSelector from Item / ItemStack
}