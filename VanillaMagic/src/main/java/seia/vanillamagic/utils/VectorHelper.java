package seia.vanillamagic.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class VectorHelper 
{
	private VectorHelper()
	{
	}
	
	/**
	 * 0 - xCoord;
	 * 1 - yCoord;
	 * 2 - zCoord;
	 */
	public static double[] getLookVec(EntityLivingBase player)
	{
		Vec3d lookVec = player.getLookVec();
		return new double[]{lookVec.xCoord, lookVec.yCoord, lookVec.zCoord};
	}
	
	/**
	 * 0 - motionX;
	 * 1 - motionY;
	 * 2 - motionZ;
	 */
	public static double[] getMotion(Entity entity)
	{
		return new double[]{entity.motionX, entity.motionY, entity.motionZ};
	}
}