package seia.vanillamagic.magic.spell.spells.summon.hostile;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.util.BlockPosUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellSummonSpiderJockey extends SpellSummonHostile {
	public SpellSummonSpiderJockey(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(EntityPlayer caster, BlockPos pos, EnumFacing face, Vec3d hitVec) {
		if (pos == null) {
			return false;
		}

		World world = caster.world;
		BlockPos spawnPos = pos.offset(face);
		Entity entityMob = null;
		ItemStack boneStack = new ItemStack(Items.BONE);
		BlockPos bonePos = pos.offset(face);
		List<Entity> entities = world.getLoadedEntityList();

		for (Entity entity : entities) {
			if ((entity instanceof EntityItem) && BlockPosUtil.isSameBlockPos(entity.getPosition(), bonePos)
					&& ItemStack.areItemsEqual(boneStack, ((EntityItem) entity).getItem())) {
				entityMob = new EntitySpider(world);

				EntitySkeleton skeleton = new EntitySkeleton(world);
				skeleton.setLocationAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), caster.rotationYaw,
						0.0F);
				skeleton.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
				world.spawnEntity(skeleton);

				skeleton.startRiding(entityMob);
				world.removeEntity(entity);

				break;
			}
		}

		if (entityMob == null) {
			VanillaMagic.logInfo("Wrong spellID. (spellID: " + getSpellID() + ")");
			return false;
		}

		entityMob.setLocationAndAngles(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D,
				caster.rotationYaw, 0.0F);

		world.spawnEntity(entityMob);
		world.updateEntities();

		return true;
	}

	// Build in castSpell method.
	public Entity getEntity(World world) {
		return null;
	}
}