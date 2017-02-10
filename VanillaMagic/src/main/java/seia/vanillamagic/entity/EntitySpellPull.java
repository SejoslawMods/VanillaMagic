package seia.vanillamagic.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import seia.vanillamagic.magic.spell.spells.teleport.TeleportHelper;

public class EntitySpellPull extends EntitySpell
{
	public BlockPos casterPosWhileCasting;
	public Entity hittedEntity;
	
	public EntitySpellPull(World world, EntityLivingBase caster, 
			double accelX, double accelY, double accelZ,
			BlockPos casterPosWhileCasting) 
	{
		super(world, caster, accelX, accelY, accelZ);
		this.casterPosWhileCasting = casterPosWhileCasting;
	}
	
	protected void onImpact(RayTraceResult result) 
	{
		if(result.typeOfHit == Type.BLOCK)
		{
			// TODO: EntitySpellPull -> what should happened if hit a block, maybe pull block ???
		}
		else if(result.typeOfHit == Type.ENTITY)
		{
			if(hittedEntity == null)
			{
				hittedEntity = result.entityHit;
				TeleportHelper.teleportEntity(hittedEntity, casterPosWhileCasting);
				castingEntity.world.updateEntities();
			}
		}
	}
}