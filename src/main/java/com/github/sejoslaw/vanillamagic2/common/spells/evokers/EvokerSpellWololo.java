package com.github.sejoslaw.vanillamagic2.common.spells.evokers;

import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.IWorld;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EvokerSpellWololo extends EvokerSpell {
    public void cast(IWorld world, PlayerEntity player, Entity target) {
        List<SheepEntity> sheeps = WorldUtils.getEntities(world, SheepEntity.class, player.getPosition(), 16, entity -> true);

        if (sheeps.isEmpty()) {
            return;
        }

        SheepEntity sheep = sheeps.get(rand.nextInt(sheeps.size()));
        DyeColor[] colors = DyeColor.values();
        sheep.setFleeceColor(colors[rand.nextInt(colors.length)]);
        player.playSound(SoundEvents.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0F, 1.0F);
    }
}
