package com.github.sejoslaw.vanillamagic.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.config.VMConfig;
import com.github.sejoslaw.vanillamagic.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.util.TextUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ActionEventDeathPoint {
	@SubscribeEvent
	public void registerTweak(LivingDeathEvent event) {
		Entity entity = event.getEntity();

		if ((!VMConfig.SHOW_LAST_DEATH_POINT) || (!(entity instanceof PlayerEntity))) {
			return;
		}

		PlayerEntity player = (PlayerEntity) entity;
		EntityUtil.addChatComponentMessageNoSpam(player,
				"Last death position: " + TextUtil.constructPositionString(player.world, entity.getPosition()));
	}
}