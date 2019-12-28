package com.github.sejoslaw.vanillamagic.common.event;

import com.github.sejoslaw.vanillamagic.common.config.VMConfig;
import com.github.sejoslaw.vanillamagic.common.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ActionEventDeathPoint {

    @SubscribeEvent
    public void registerTweak(LivingDeathEvent event) {
        Entity entity = event.getEntity();

        if ((!VMConfig.SHOW_LAST_DEATH_POINT.get()) || (!(entity instanceof PlayerEntity))) {
            return;
        }

        PlayerEntity player = (PlayerEntity) entity;
        String message = "Last death position: " + TextUtil.constructPositionString(player.world.getDimension().getType(), entity.getPosition());
        EntityUtil.addChatComponentMessageNoSpam(player, TextUtil.wrap(message));
    }
}