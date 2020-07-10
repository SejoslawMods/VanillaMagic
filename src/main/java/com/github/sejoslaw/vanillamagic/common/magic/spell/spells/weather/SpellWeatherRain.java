package com.github.sejoslaw.vanillamagic.common.magic.spell.spells.weather;

import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.common.magic.spell.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellWeatherRain extends Spell {
    public SpellWeatherRain(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
        super(spellID, spellName, spellUniqueName, wand, itemOffHand);
    }

    public boolean castSpell(PlayerEntity caster, BlockPos pos, Direction face, Vec3d hitVec) {
        World world = caster.world;

        WorldInfo worldInfo = world.getWorldInfo();
        worldInfo.setClearWeatherTime(0);
        worldInfo.setRainTime(1000);
        worldInfo.setThunderTime(1000);
        worldInfo.setRaining(true);
        worldInfo.setThundering(false);

        return true;
    }
}
