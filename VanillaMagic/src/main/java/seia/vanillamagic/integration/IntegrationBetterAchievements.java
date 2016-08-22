package seia.vanillamagic.integration;

import betterachievements.api.util.IMCHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.handler.QuestHandler;

public class IntegrationBetterAchievements implements IIntegration
{
	public void preInit()
	{
	}
	
	public void init() 
	{
		IMCHelper.sendIconForPage(QuestHandler.INSTANCE.PAGE_NAME, new ItemStack(Items.CAULDRON));
		System.out.println("BetterAchievements integration enabled");
	}
	
	public void postInit() 
	{
	}
}