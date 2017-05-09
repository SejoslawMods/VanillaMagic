package seia.vanillamagic.util.explosion;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

/**
 * Class which is used for various explosion related mathematics.
 */
public class ExplosionHelper
{
	private ExplosionHelper()
	{
	}
	
	/**
	 * Explosion was made using {@link World#newExplosion(Entity, double, double, double, float, boolean, boolean)}
	 * 
	 * @return Returns new Explosion object.
	 */
	public static Explosion newExplosion1(World world, 
			@Nullable Entity entity, 
			double x, double y, double z, 
			float strength, 
			boolean isFlaming, boolean isSmoking)
	{
		return world.newExplosion((Entity)null, x, y, z, strength, isFlaming, isSmoking);
	}
	
	/**
	 * Explosion was made using {@link VMExplosion}
	 * 
	 * @return Returns new Explosion object.
	 */
	public static Explosion newExplosion2(World world, 
			@Nullable Entity entity, 
			double x, double y, double z, 
			float strength, 
			boolean isFlaming, boolean isSmoking)
	{
		VMExplosion explosion = new VMExplosion(world, entity, x, y, z, strength, isFlaming, isSmoking);
		explosion.doExplosion();
		return explosion;
	}
	
	public static int roundToNegInf(float x)
	{
		int ret = (int)x;
		if(ret > x) 
		{
			ret--;
		}
		return ret;
	}
	
	public static int roundToNegInf(double x)
	{
		int ret = (int)x;
		if(ret > x) 
		{
			ret--;
		}
		return ret;
	}

	public static int square(int x)
	{
		return x * x;
	}
	  
	public static float square(float x)
	{
		return x * x;
	}
	  
	public static double square(double x)
	{
		return x * x;
	}
}