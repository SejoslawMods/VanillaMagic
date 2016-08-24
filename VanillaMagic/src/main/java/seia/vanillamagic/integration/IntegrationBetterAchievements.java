package seia.vanillamagic.integration;

import org.apache.logging.log4j.Level;

import betterachievements.api.util.IMCHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.handler.QuestHandler;

public class IntegrationBetterAchievements implements IIntegration
{
	public void preInit()
	{
	}
	
	public void init() 
	{
		IMCHelper.sendIconForPage(QuestHandler.INSTANCE.PAGE_NAME, new ItemStack(Items.CAULDRON));
		VanillaMagic.logger.log(Level.INFO, "BetterAchievements integration enabled");
	}
	
	public void postInit() 
	{
	}
}