package com.github.sejoslaw.vanillamagic.magic.spell.spells;

import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.entity.EntitySpellFreezeLiquid;
import com.github.sejoslaw.vanillamagic.magic.spell.Spell;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellWaterFreeze extends Spell {
	public SpellWaterFreeze(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(PlayerEntity caster, BlockPos pos, Direction face, Vector3D hitVec) {
		if (pos != null) {
			return false;
		}

		World world = caster.world;

		Vector3D lookingAt = VectorWrapper.wrap(caster.getLookVec());
		double accelX = lookingAt.getX();
		double accelY = lookingAt.getY();
		double accelZ = lookingAt.getZ();

		EntitySpellFreezeLiquid spellLightningBolt = new EntitySpellFreezeLiquid(world, caster, accelX, accelY, accelZ,
				Blocks.ICE, new BlockLiquid[] { Blocks.WATER, Blocks.FLOWING_WATER }, 5);

		world.spawnEntity(spellLightningBolt);
		world.updateEntities();

		return true;
	}
}