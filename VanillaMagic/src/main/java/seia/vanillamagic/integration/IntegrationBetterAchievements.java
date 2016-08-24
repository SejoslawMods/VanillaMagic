package seia.vanillamagic.integration;

import java.lang.reflect.Method;

import org.apache.logging.log4j.Level;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.handler.QuestHandler;

public class IntegrationBetterAchievements implements IIntegration
{
	public void init() 
	{
		try
		{
			Class<?> clazz = Class.forName("betterachievements.api.util.IMCHelper");
			Method sendIconForPage = clazz.getMethod("sendIconForPage", new Class[]{String.class, ItemStack.class});
			sendIconForPage.invoke(null, new Object[]{QuestHandler.INSTANCE.PAGE_NAME, new ItemStack(Items.CAULDRON)});
			
			VanillaMagic.logger.log(Level.INFO, "BetterAchievements integration enabled");
		}
		catch(Exception e)
		{
			VanillaMagic.logger.log(Level.INFO, "BetterAchievements integration failed");
		}
	}
}