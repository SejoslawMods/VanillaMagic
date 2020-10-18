package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestCastSpell;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
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
                                    ItemStackUtils.areEqual(quest.leftHandStack, leftHandStack, true) &&
                                    ItemStackUtils.areEqual(quest.rightHandStack, rightHandStack, true))
                            .findFirst()
                            .orElse(null);
                },
                (player, world, pos, direction, quest) -> {
                    if (quest.getSpell() == null) {
                        return;
                    }

                    quest.getSpell().cast(player, world, pos, direction);

                    this.executor.withHands(player, (leftHandStack, rightHandStack) ->
                            leftHandStack.shrink(quest.leftHandStack.getCount()));
                });
    }
}
