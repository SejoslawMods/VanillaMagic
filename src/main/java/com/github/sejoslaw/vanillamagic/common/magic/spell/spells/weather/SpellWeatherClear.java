package com.github.sejoslaw.vanillamagic.common.magic.spell.spells.weather;

import com.github.sejoslaw.vanillamagic.common.magic.spell.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellWeatherClear extends Spell {
    public SpellWeatherClear(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
        super(spellID, spellName, spellUniqueName, wand, itemOffHand);
    }

    public boolean castSpell(PlayerEntity caster, BlockPos pos, Direction face, Vec3d hitVec) {
        World world = caster.world;

        WorldInfo worldInfo = world.getWorldInfo();
        worldInfo.setClearWeatherTime(1000);
        worldInfo.setRainTime(0);
        worldInfo.setThunderTime(0);
        worldInfo.setRaining(false);
        worldInfo.setThundering(false);

        return true;
    }
}
