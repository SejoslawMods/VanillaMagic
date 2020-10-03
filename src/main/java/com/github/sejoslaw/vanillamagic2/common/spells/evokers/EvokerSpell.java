package com.github.sejoslaw.vanillamagic2.common.spells.evokers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.IWorld;

import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class EvokerSpell {
    protected final Random rand = new Random();

    /**
     * Casts current Evoker Spell.
     */
    public abstract void cast(IWorld world, PlayerEntity player, Entity target);
}
