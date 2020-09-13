package com.github.sejoslaw.vanillamagic2.common.itemupgrades.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.itemupgrades.ItemUpgradeEventCaller;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.Difficulty;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemUpgradeEventCallerWither extends ItemUpgradeEventCaller {
    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        this.eventCaller.executor.onAttackEntityNoHandsCheck(event,
                (player, world, entity) -> this.getQuest(player),
                (player, world, entity, quest) ->
                        this.execute(player, () -> {
                            int multiplier = 1;

                            if (world.getDifficulty() == Difficulty.NORMAL) {
                                multiplier = 10;
                            } else if (world.getDifficulty() == Difficulty.HARD) {
                                multiplier = 40;
                            }

                            if (entity instanceof LivingEntity) {
                                ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.WITHER, 20 * multiplier, 1));
                            }
                        }));
    }
}
