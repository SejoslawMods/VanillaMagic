package seia.vanillamagic.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.config.VMConfig;
import seia.vanillamagic.util.EntityUtil;
import seia.vanillamagic.util.TextUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ActionEventDeathPoint {
	@SubscribeEvent
	public void registerTweak(LivingDeathEvent event) {
		Entity entity = event.getEntity();

		if ((!VMConfig.SHOW_LAST_DEATH_POINT) || (!(entity instanceof EntityPlayer))) {
			return;
		}

		EntityPlayer player = (EntityPlayer) entity;
		EntityUtil.addChatComponentMessageNoSpam(player,
				"Last death position: " + TextUtil.constructPositionString(player.world, entity.getPosition()));
	}
}