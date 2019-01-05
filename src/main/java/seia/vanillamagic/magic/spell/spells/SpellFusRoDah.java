package seia.vanillamagic.magic.spell.spells;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.api.util.VectorWrapper.Vector3D;
import seia.vanillamagic.magic.spell.Spell;
import seia.vanillamagic.util.EntityUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellFusRoDah extends Spell {
	public SpellFusRoDah(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(EntityPlayer caster, BlockPos pos, EnumFacing face, Vector3D hitVec) {
		if (pos != null) {
			return false;
		}

		// it will be (2 *SIZE)x(2 *SIZE)x(2 * SIZE) area of effect
		int SIZE = 8;
		float strenght = 2.0f;
		double casterX = caster.posX;
		double casterY = caster.posY;
		double casterZ = caster.posZ;

		BlockPos casterPos = new BlockPos(casterX, casterY, casterZ);
		AxisAlignedBB aabb = new AxisAlignedBB(casterPos);
		aabb = aabb.expand(SIZE, SIZE, SIZE);

		World world = caster.world;
		List<Entity> entitiesInAABB = world.getEntitiesWithinAABBExcludingEntity(caster, aabb);

		for (Entity entity : entitiesInAABB) {
			if (entity instanceof EntityLivingBase) {
				EntityUtil.knockBack(caster, entity, strenght);
			}
		}

		return true;
	}
}