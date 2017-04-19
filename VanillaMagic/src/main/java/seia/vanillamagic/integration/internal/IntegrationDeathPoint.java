package seia.vanillamagic.integration.internal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.config.VMConfig;
import seia.vanillamagic.integration.IIntegration;
import seia.vanillamagic.util.EntityHelper;
import seia.vanillamagic.util.TextHelper;

public class IntegrationDeathPoint implements IIntegration
{
	public String getModName() 
	{
		return "VM Death Point";
	}
	
	public void preInit() throws Exception
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void registerTweak(LivingDeathEvent event)
	{
		if(!VMConfig.showLastDeathPoint) return;
		
		Entity entity = event.getEntity();
		
		// Return if it is not Player
		if (!(entity instanceof EntityPlayer)) return;
		
		EntityPlayer player = (EntityPlayer) entity;
		EntityHelper.addChatComponentMessage(player, "Last death position: " + TextHelper.constructPositionString(player.world, entity.getPosition()));
	}
}