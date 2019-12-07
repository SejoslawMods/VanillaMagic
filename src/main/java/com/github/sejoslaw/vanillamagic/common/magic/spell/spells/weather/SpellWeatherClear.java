package com.github.sejoslaw.vanillamagic.magic.spell.spells.weather;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.magic.spell.Spell;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellWeatherClear extends Spell {
	public SpellWeatherClear(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(PlayerEntity caster, BlockPos pos, Direction face, Vector3D hitVec) {
		World world = caster.world;

		WorldInfo worldInfo = world.getWorldInfo();
		worldInfo.setCleanWeatherTime(1000);
		worldInfo.setRainTime(0);
		worldInfo.setThunderTime(0);
		worldInfo.setRaining(false);
		worldInfo.setThundering(false);

		return true;
	}
}