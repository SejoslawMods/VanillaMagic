package com.github.sejoslaw.vanillamagic2.common.spells.evokers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EvokerSpellWololo extends EvokerSpell {
    public void cast(World world, PlayerEntity player, Entity target) {
        List<SheepEntity> sheeps = world.getEntitiesWithinAABB(EntityType.SHEEP, player.getBoundingBox().expand(16.0D, 4.0D, 16.0D), entity -> true);

        if (sheeps.isEmpty()) {
            return;
        }

        SheepEntity sheep = sheeps.get(rand.nextInt(sheeps.size()));
        sheep.setFleeceColor(DyeColor.values()[rand.nextInt(DyeColor.values().length)]);
        player.playSound(SoundEvents.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0F, 1.0F);
    }
}
