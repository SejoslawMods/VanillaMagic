package seia.vanillamagic.api.tileentity.blockabsorber;

import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;

/**
 * This is a connection to TileBlockAbsorber
 */
public interface IBlockAbsorber extends ICustomTileEntity
{
	/**
	 * Returns the facing on which items will be added to Hopper.
	 */
	EnumFacing getInputFacing();
	
	/**
	 * Sets the Hopper to which Block Absorber will export items.
	 */
	void setConnectedHopper(TileEntityHopper hopper);
	
	/** 
	 * Returns Hopper to which Block Absorber will export items.
	 */
	TileEntityHopper getConnectedHopper();
}