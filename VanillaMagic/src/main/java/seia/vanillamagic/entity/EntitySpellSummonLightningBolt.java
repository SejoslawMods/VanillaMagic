package seia.vanillamagic.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class EntitySpellSummonLightningBolt extends EntitySpell
{
	public EntitySpellSummonLightningBolt(World world, EntityLivingBase caster, 
			double accelX, double accelY, double accelZ)
	{
		super(world, caster, accelX, accelY, accelZ);
	}
	
	protected void onImpact(RayTraceResult result) 
	{
		double spawnLightningBoltX = 0;
		double spawnLightningBoltY = 0;
		double spawnLightningBoltZ = 0;
		if(result.typeOfHit == Type.BLOCK)
		{
			BlockPos blockPos = result.getBlockPos();
			spawnLightningBoltX = blockPos.getX();
			spawnLightningBoltY = blockPos.getY();
			spawnLightningBoltZ = blockPos.getZ();
			IBlockState airCheckerState = world.getBlockState(blockPos);
			Block airCheckerStateBlock = airCheckerState.getBlock();
			if(airCheckerStateBlock.isAir(airCheckerState, world, blockPos))
			{
				return;
			}
		}
		else if(result.typeOfHit == Type.ENTITY)
		{
			Entity entityHit = result.entityHit;
			spawnLightningBoltX = entityHit.posX;
			spawnLightningBoltY = entityHit.posY;
			spawnLightningBoltZ = entityHit.posZ;
		}
		else
		{
			return;
		}
		// Lightning Bolt spawning
		EntityLightningBolt entityLightningBolt = new EntityLightningBolt(world, 
				spawnLightningBoltX, spawnLightningBoltY, spawnLightningBoltZ, false);
		this.castingEntity.world.spawnEntity(entityLightningBolt);
		this.setDead();
	}
	
	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		EntityLivingBase caster = this.castingEntity;
		if(caster != null && caster instanceof EntityPlayer && !caster.isEntityAlive())
		{
			this.setDead();
		}
		else
		{
			super.onUpdate();
		}
	}
}