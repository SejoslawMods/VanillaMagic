package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestCastSpellOnBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerCastSpellOnBlock extends EventCallerCastSpell<QuestCastSpellOnBlock> {
    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        this.castSpell(event);
    }
}
