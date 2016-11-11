package seia.vanillamagic.api.tileentity.blockabsorber;

import net.minecraft.tileentity.TileEntityHopper;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;

/**
 * This is a connection to TileBlockAbsorber
 */
public interface IBlockAbsorber extends ICustomTileEntity
{
	/** 
	 * Returns Hopper to which Block Absorber will export items.
	 */
	TileEntityHopper getConnectedHopper();
}