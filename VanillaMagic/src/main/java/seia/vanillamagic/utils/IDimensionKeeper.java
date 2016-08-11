package seia.vanillamagic.utils;

import net.minecraft.tileentity.TileEntity;

/**
 * Implement it to any object that should keep the value of Dimension in which it is.
 */
public interface IDimensionKeeper 
{
	/**
	 * Set Dimension.
	 */
	void setDimension(int dimension);
	
	/**
	 * Get the Dimension in which this object is.
	 */
	int getDimension();
	
	/**
	 * Returns the TileEntity to which this interface should be implemented into.
	 */
	TileEntity getCustomTileEntity();
}