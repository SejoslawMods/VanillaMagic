package seia.vanillamagic.api.tileentity.blockabsorber;

import net.minecraft.tileentity.TileEntityHopper;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;

/**
 * This is a connection to TileBlockAbsorber.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IBlockAbsorber extends ICustomTileEntity {
	/**
	 * @return Returns Hopper to which Block Absorber will export items.
	 */
	TileEntityHopper getConnectedHopper();
}