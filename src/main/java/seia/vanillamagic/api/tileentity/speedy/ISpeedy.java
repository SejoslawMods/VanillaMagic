package seia.vanillamagic.api.tileentity.speedy;

import net.minecraft.inventory.IInventory;
import seia.vanillamagic.api.inventory.IInventoryWrapper;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;
import seia.vanillamagic.api.util.Box;

/**
 * This is the connection to TileSpeedy.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface ISpeedy extends ICustomTileEntity {
	/**
	 * @return Returns the number of ticks for one block. (Default = 1000)
	 */
	int getTicks();

	/**
	 * Sets the number of ticks for one block.
	 * 
	 * @param ticks
	 */
	void setTicks(int ticks);

	/**
	 * @return Returns the working radius box.
	 */
	Box getRadiusBox();

	/**
	 * Sets the working radius box.
	 * 
	 * @param radiusBox
	 */
	void setRadiusBox(Box radiusBox);

	/**
	 * @return Returns the size of the Speedy. (Default = 4)
	 */
	int getSize();

	/**
	 * Sets the size of the Speedy.
	 * 
	 * @param size
	 */
	void setSize(int size);

	/**
	 * @return Returns the {@link IInventoryWrapper} for the {@link IInventory} with
	 *         Acceleration Crystal.
	 */
	IInventoryWrapper getInventoryWithCrystal();

	/**
	 * @return Returns TRUE if the connected {@link IInventory} contains
	 *         Acceleration Crystal.
	 */
	boolean containsCrystal();
}