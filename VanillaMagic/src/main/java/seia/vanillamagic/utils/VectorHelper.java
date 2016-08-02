package seia.vanillamagic.utils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class VectorHelper 
{
	/*
	 * 0 - xCoord
	 * 1 - yCoord
	 * 2 - zCoord
	 */
	public static double[] getLookVec(EntityLivingBase player)
	{
		Vec3d lookVec = player.getLookVec();
		return new double[]{lookVec.xCoord, lookVec.yCoord, lookVec.zCoord};
	}
}