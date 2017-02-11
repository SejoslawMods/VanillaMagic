package seia.vanillamagic.entity.meteor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import seia.vanillamagic.config.VMConfig;
import seia.vanillamagic.util.explosion.ExplosionHelper;

public class EntityMeteor extends EntityLargeFireball
{
	public EntityMeteor(EntityLivingBase castingEntity, 
			double spawnMeteorX, double spawnMeteorY, double spawnMeteorZ,
			double accelX, double accelY, double accelZ) 
	{
		super(castingEntity.world);
		setLocationAndAngles(spawnMeteorX, spawnMeteorY, spawnMeteorZ, this.rotationYaw, this.rotationPitch);
		setPosition(spawnMeteorX, spawnMeteorY, spawnMeteorZ);
		float baseSize = VMConfig.basicMeteorSize;
		setSize(baseSize, baseSize);
		
		this.explosionPower = VMConfig.basicMeteorExplosionPower;
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.shootingEntity = castingEntity;
		double accel = (double)MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
		this.accelerationX = 0;
		this.accelerationY = accelY / accel * 0.1D;
		this.accelerationZ = 0;
	}
	
	/**
	 * Called when this EntityMeteor hits a block or entity.
	 */
	protected void onImpact(RayTraceResult result)
	{
		super.onImpact(result);
		if(!this.world.isRemote)
		{
			if(result.entityHit != null)
			{
				result.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6.0F);
				this.applyEnchantments(this.shootingEntity, result.entityHit);
			}
			boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
			ExplosionHelper.newExplosion2(this.world, (Entity)null, this.posX, this.posY, this.posZ, (float)this.explosionPower, flag, flag);
			this.setDead();
		}
	}
	
	public static void fixData(DataFixer dataFixer)
	{
		EntityFireball.registerFixesFireball(dataFixer, "Meteor");
	}
}