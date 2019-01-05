package seia.vanillamagic.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import seia.vanillamagic.magic.spell.spells.teleport.TeleportUtil;

/**
 * Class which defines Pull Entity Spell.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EntitySpellPull extends EntitySpell {
	public BlockPos casterPosWhileCasting;

	public EntitySpellPull(World world, EntityLivingBase caster, double accelX, double accelY, double accelZ,
			BlockPos casterPosWhileCasting) {
		super(world, caster, accelX, accelY, accelZ);
		this.casterPosWhileCasting = casterPosWhileCasting;
	}

	/**
	 * Run when Entity collide with something.
	 */
	protected void onImpact(RayTraceResult result) {
		if (result.typeOfHit == Type.BLOCK) {
			// TODO: EntitySpellPull -> what should happened if hit a block, maybe pull
			// block ???
		} else if (result.typeOfHit == Type.ENTITY) {
			TeleportUtil.teleportEntity(result.entityHit, casterPosWhileCasting);
			castingEntity.world.updateEntities();
		}
	}
}