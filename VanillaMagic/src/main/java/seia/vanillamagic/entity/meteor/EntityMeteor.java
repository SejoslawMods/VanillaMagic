package seia.vanillamagic.entity.meteor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;

public class EntityMeteor extends EntityLargeFireball
{
	public static final float BASIC_METEOR_SIZE = 5f;
	public static final int BASIC_METEOR_EXPLOSION_POWER = 20;

	public EntityMeteor(EntityLivingBase castingEntity, 
			double spawnMeteorX, double spawnMeteorY, double spawnMeteorZ,
			double accelX, double accelY, double accelZ) 
	{
		super(castingEntity.worldObj);
		setLocationAndAngles(spawnMeteorX, spawnMeteorY, spawnMeteorZ, this.rotationYaw, this.rotationPitch);
        setPosition(spawnMeteorX, spawnMeteorY, spawnMeteorZ);
		setSize(BASIC_METEOR_SIZE, BASIC_METEOR_SIZE);
		explosionPower = BASIC_METEOR_EXPLOSION_POWER;
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		shootingEntity = castingEntity;
		double accel = (double)MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
		this.accelerationX = 0;
		this.accelerationY = accelY / accel * 0.1D;
		this.accelerationZ = 0;
	}
	
	/**
	 * Called when this EntityMeteor hits a block or entity.
	 */
	protected void onImpact(RayTraceResult result)
	{
		if(!this.worldObj.isRemote)
		{
			if(result.entityHit != null)
			{
				result.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6.0F);
				this.applyEnchantments(this.shootingEntity, result.entityHit);
			}
			boolean flag = this.worldObj.getGameRules().getBoolean("mobGriefing");
			this.worldObj.newExplosion((Entity)null, this.posX, this.posY, this.posZ, (float)this.explosionPower, flag, flag);
			this.setDead();
		}
	}
	
	public static void fixData(DataFixer dataFixer)
	{
		EntityFireball.registerFixesFireball(dataFixer, "Meteor");
	}
}