package com.github.sejoslaw.vanillamagic2.common.itemupgrades.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.itemupgrades.ItemUpgradeEventCaller;
import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collection;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemUpgradeEventCallerLifesteal extends ItemUpgradeEventCaller {
    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        this.eventCaller.executor.onAttackEntity(event,
                (player, world, entity) -> this.getQuest(player),
                (player, world, entity, quest) ->
                    this.execute(player, () ->
                            this.eventCaller.executor.withHands(player,
                                    (leftHandStack, rightHandStack) -> {
                                        Multimap<String, AttributeModifier> attributes = rightHandStack.getItem().getAttributeModifiers(EquipmentSlotType.MAINHAND, rightHandStack);

                                        String attributeName = SharedMonsterAttributes.ATTACK_DAMAGE.getName();
                                        Collection<AttributeModifier> modifiers = attributes.get(attributeName);

                                        double amount = modifiers
                                                .stream()
                                                .mapToDouble(am -> am.getAmount() / 2)
                                                .sum();

                                        player.heal((float) amount);
                                    })));
    }
}
