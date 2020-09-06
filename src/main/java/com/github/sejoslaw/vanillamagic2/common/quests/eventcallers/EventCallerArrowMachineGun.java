package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestArrowMachineGun;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerArrowMachineGun extends EventCaller<QuestArrowMachineGun> {
    @SubscribeEvent
    public void shootArrow(PlayerInteractEvent.RightClickItem event) {
        this.executor.onPlayerInteractNoStackSizeCheck(event, (player, world, pos, face) ->
                this.executor.withHands(player, (leftHandStack, rightHandStack) -> {
                    ArrowEntity arrowEntity = new ArrowEntity(world, player);

                    arrowEntity.setPotionEffect(leftHandStack);
                    arrowEntity.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.0F, 1.0F);
                    arrowEntity.setIsCritical(true);
                    arrowEntity.setDamage(arrowEntity.getDamage() + (double) EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, leftHandStack) * 0.5D + 0.5D);
                    arrowEntity.setKnockbackStrength(EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, leftHandStack));

                    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, leftHandStack) > 0) {
                        arrowEntity.setFire(100);
                    }

                    world.addEntity(arrowEntity);
                    world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(),
                            SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (new Random().nextFloat() * 0.4F + 1.2F) + 0.5F);

                    if (leftHandStack.getCount() > 0) {
                        leftHandStack.grow(-1);
                    }
                }));
    }
}
