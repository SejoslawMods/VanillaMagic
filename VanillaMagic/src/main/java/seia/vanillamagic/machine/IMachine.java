package seia.vanillamagic.machine;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Machine is a TileEntity that perform some work on World.
 * This is made to unify some of the machine works.
 */
public interface IMachine extends ITickable, INBTSerializable<NBTTagCompound>
{
	/**
	 * Get the actual position of the machine block.
	 * In Vanilla Magic it usually will be Cauldron.
	 */
	BlockPos getMachinePos();
}