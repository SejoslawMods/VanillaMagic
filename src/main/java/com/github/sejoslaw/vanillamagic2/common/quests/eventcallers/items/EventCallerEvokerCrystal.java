package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.items;

import com.github.sejoslaw.vanillamagic2.common.quests.types.items.QuestEvokerCrystal;
import com.github.sejoslaw.vanillamagic2.common.registries.EvokerSpellRegistry;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerEvokerCrystal extends EventCallerVMItem<QuestEvokerCrystal> {
    @SubscribeEvent
    public void useItem(PlayerInteractEvent event) {
        this.executor.onPlayerInteract(event, (player, world, blockPos, direction) ->
                this.executor.useVMItem(player, this.getVMItem().getUniqueKey(), (handStack) -> {
                    if (player.isSneaking()) {
                        EvokerSpellRegistry.changeSpell(handStack);
                    } else {
                        EvokerSpellRegistry.castSpell(world, player, handStack);
                    }
                }));
    }
}
