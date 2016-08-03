package seia.vanillamagic.entity.meteor;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import seia.vanillamagic.entity.EntitySpell;

public class EntitySpellSummonMeteor extends EntitySpell
{	
	public EntitySpellSummonMeteor(World world, EntityLivingBase caster, 
			double accelX, double accelY, double accelZ)
    {
        super(world, caster, accelX, accelY, accelZ);
    }
	
	@Override
	public void onImpact(RayTraceResult result)
	{
		double spawnMeteorX = 0;
		double spawnMeteorY = 300;
		double spawnMeteorZ = 0;
		if(result.typeOfHit == Type.BLOCK)
		{
			BlockPos blockPos = result.getBlockPos();
			spawnMeteorX = blockPos.getX();
			spawnMeteorZ = blockPos.getZ();
			IBlockState airCheckerState = worldObj.getBlockState(blockPos);
			Block airCheckerStateBlock = airCheckerState.getBlock();
			if(airCheckerStateBlock.isAir(airCheckerState, worldObj, blockPos))
			{
				return;
			}
		}
		else
		{
			return;
		}
		// Meteor spawning
		EntityMeteor meteor = new EntityMeteor(this.castingEntity, 
				spawnMeteorX, spawnMeteorY, spawnMeteorZ,
				this.accelerationX, this.accelerationY, this.accelerationZ);
		this.castingEntity.worldObj.spawnEntityInWorld(meteor);
		this.setDead();
	}
    
	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		EntityLivingBase caster = this.castingEntity;
		if (caster != null && caster instanceof EntityPlayer && !caster.isEntityAlive())
		{
			this.setDead();
		}
		else
		{
			super.onUpdate();
		}
	}
}