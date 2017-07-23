package seia.vanillamagic.entity.meteor;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import seia.vanillamagic.entity.EntitySpellUpdate;

/**
 * Class which represents EntitySpell which will spawn Meteor.
 * Player shot this Entity and after hitting ground This will spawn EntityMeteor.
 */
public class EntitySpellSummonMeteor extends EntitySpellUpdate
{	
	public EntitySpellSummonMeteor(World world, EntityLivingBase caster, 
			double accelX, double accelY, double accelZ)
	{
		super(world, caster, accelX, accelY, accelZ);
	}
	
	/**
	 * This method is called when this Entity hits something.
	 */
	public void onImpact(RayTraceResult result)
	{
		double spawnMeteorX = 0;
		double spawnMeteorY = 300;
		double spawnMeteorZ = 0;
		if (result.typeOfHit == Type.BLOCK)
		{
			BlockPos blockPos = result.getBlockPos();
			spawnMeteorX = blockPos.getX();
			spawnMeteorZ = blockPos.getZ();
			IBlockState airCheckerState = world.getBlockState(blockPos);
			Block airCheckerStateBlock = airCheckerState.getBlock();
			if (airCheckerStateBlock.isAir(airCheckerState, world, blockPos)) return;
		}
		else
			return;
		
		// Meteor spawning
		EntityMeteor meteor = new EntityMeteor(this.castingEntity, 
				spawnMeteorX, spawnMeteorY, spawnMeteorZ,
				this.accelerationX, this.accelerationY, this.accelerationZ);
		this.castingEntity.world.spawnEntity(meteor);
		this.setDead();
	}
}