package seia.vanillamagic.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import seia.vanillamagic.util.WeatherUtil;

/**
 * Class which defines the Spell Summon Lightning Bolt.
 */
public class EntitySpellSummonLightningBolt extends EntitySpellUpdate
{
	public EntitySpellSummonLightningBolt(World world, EntityLivingBase caster, 
			double accelX, double accelY, double accelZ)
	{
		super(world, caster, accelX, accelY, accelZ);
	}
	
	/**
	 * Run when Entity collide with something.
	 */
	protected void onImpact(RayTraceResult result) 
	{
		double spawnLightningBoltX = 0;
		double spawnLightningBoltY = 0;
		double spawnLightningBoltZ = 0;
		if (result.typeOfHit == Type.BLOCK)
		{
			BlockPos blockPos = result.getBlockPos().offset(result.sideHit);
			spawnLightningBoltX = blockPos.getX();
			spawnLightningBoltY = blockPos.getY();
			spawnLightningBoltZ = blockPos.getZ();
			IBlockState airCheckerState = world.getBlockState(blockPos);
			Block airCheckerStateBlock = airCheckerState.getBlock();
			if (airCheckerStateBlock.isAir(airCheckerState, world, blockPos)) return;
		}
		else if (result.typeOfHit == Type.ENTITY)
		{
			Entity entityHit = result.entityHit;
			spawnLightningBoltX = entityHit.posX;
			spawnLightningBoltY = entityHit.posY;
			spawnLightningBoltZ = entityHit.posZ;
		}
		else
			return;
		WeatherUtil.spawnLightningBolt(world, spawnLightningBoltX, spawnLightningBoltY, spawnLightningBoltZ);
		this.setDead();
	}
}