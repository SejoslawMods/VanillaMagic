package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestCastSpell;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerCastSpell<TQuest extends QuestCastSpell> extends EventCaller<TQuest> {
    protected void castSpell(PlayerInteractEvent event) {
        this.executor.onPlayerInteract(event,
                (player, world, pos, direction) -> {
                    ItemStack leftHandStack = player.getHeldItemOffhand();
                    ItemStack rightHandStack = player.getHeldItemMainhand();

                    return this.quests
                            .stream()
                            .filter(quest ->
                                    quest.leftHandStack.getItem() == leftHandStack.getItem() &&
                                    quest.leftHandStack.getCount() <= leftHandStack.getCount() &&
                                    quest.rightHandStack.getItem() == rightHandStack.getItem() &&
                                    quest.rightHandStack.getCount() <= rightHandStack.getCount())
                            .findFirst()
                            .orElse(null);
                },
                (player, world, pos, direction, quest) -> {
                    if (quest.spell == null) {
                        return;
                    }

                    quest.spell.cast(player, world, pos, direction);

                    this.executor.withHands(player, (leftHandStack, rightHandStack) ->
                            leftHandStack.shrink(quest.leftHandStack.getCount()));
                });
    }
}
