package com.github.sejoslaw.vanillamagic.common.quest.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestCastSpellInAir extends QuestCastSpell {
    /**
     * Cast spell when right-clicked while pointing in air.
     */
    @SubscribeEvent
    public void castSpell(PlayerInteractEvent.RightClickItem event) {
        castSpell(event.getPlayer(), null, null, null);
    }
}