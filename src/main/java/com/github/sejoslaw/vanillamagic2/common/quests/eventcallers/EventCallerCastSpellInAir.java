package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestCastSpellInAir;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerCastSpellInAir extends EventCallerCastSpell<QuestCastSpellInAir> {
    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickItem event) {
        this.castSpell(event);
    }
}
