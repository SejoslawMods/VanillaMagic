package seia.vanillamagic.api.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * This interface will be used on {@link TileEntity} to get the internal NBT related methods.
 */
public interface ITileEntityNBT
{
	/**
	 * @see TileEntity#readFromNBT(NBTTagCompound)
	 */
	void readFromNBT(NBTTagCompound tag);
	
	/**
	 * @see TileEntity#writeToNBT(NBTTagCompound)
	 */
	NBTTagCompound writeToNBT(NBTTagCompound tag);
}