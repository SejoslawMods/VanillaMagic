package seia.vanillamagic.integration;

import java.lang.reflect.Method;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.handler.QuestHandler;
import seia.vanillamagic.util.ClassUtils;

public class IntegrationBetterAchievements implements IIntegration
{
	public String getModName()
	{
		return "BetterAchievements";
	}
	
	public boolean init() throws Exception
	{
		Method sendIconForPage = ClassUtils.getMethod("betterachievements.api.util.IMCHelper", "sendIconForPage", new Class[]{String.class, ItemStack.class});
		sendIconForPage.invoke(null, new Object[]{QuestHandler.INSTANCE.pageName, new ItemStack(Items.CAULDRON)});
		
		return true;
	}
}