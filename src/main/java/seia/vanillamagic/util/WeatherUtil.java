package seia.vanillamagic.util;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.World;

/**
 * Class which holds methods connected with MC Weather interaction.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class WeatherUtil {
	private WeatherUtil() {
	}

	/**
	 * Spawn Lightning Bolt.
	 * 
	 * @param world World on which Lightning Bolt should be spawned.
	 * @param x     Position X
	 * @param y     Position Y
	 * @param z     Position Z
	 * @return Returns spawned Entity.
	 */
	public static EntityLightningBolt spawnLightningBolt(World world, double x, double y, double z) {
		EntityLightningBolt elb = new EntityLightningBolt(world, x, y, z, false);
		world.addWeatherEffect(elb);
		return elb;
	}
}