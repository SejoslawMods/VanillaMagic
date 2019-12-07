package com.github.sejoslaw.vanillamagic.api.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;

/**
 * This Event is fired BEFORE PotionedCrystal apply PotionEffect to Player.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventPotionedCrystalTick extends EventPlayerOnWorld {
    private final ItemStack crystalStack;
    private final Effect effectToApply;

    public EventPotionedCrystalTick(PlayerEntity player, ItemStack crystalStack, Effect effectToApply) {
        super(player, player.world);
        this.crystalStack = crystalStack;
        this.effectToApply = effectToApply;
    }

    /**
     * @return Returns the PotionedCrystal itself in a form of ItemStack.
     */
    public ItemStack getPotionedCrystal() {
        return this.crystalStack;
    }

    /**
     * @return Returns the PotionEffect which will be applied to Player.
     */
    public Effect getPotionEffect() {
        return this.effectToApply;
    }
}