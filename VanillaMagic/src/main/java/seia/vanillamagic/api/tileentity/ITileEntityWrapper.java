package seia.vanillamagic.api.tileentity;

import net.minecraft.tileentity.TileEntity;

/**
 * Wraps a single {@link TileEntity}
 */
public interface ITileEntityWrapper 
{
	/**
	 * @return Returns {@link TileEntity} to which this interface is implemented into.
	 */
	TileEntity getTileEntity();
}