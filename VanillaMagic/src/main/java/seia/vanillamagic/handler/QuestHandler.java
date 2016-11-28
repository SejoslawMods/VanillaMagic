package seia.vanillamagic.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import seia.vanillamagic.core.VanillaMagic;

public class QuestHandler
{
	public static final String PAGE_NAME = VanillaMagic.NAME;
	public static final List<Object> REGISTERED_EVENTS = new ArrayList<Object>();
	
	private static final AchievementPage page;
	
	private QuestHandler()
	{
	}
	
	static
	{
		page = new AchievementPage(PAGE_NAME);
		AchievementPage.registerAchievementPage(page);
	}
	
	public static void addAchievement(Achievement achievement)
	{
		page.getAchievements().add(achievement);
	}
	
	public static List<Achievement> getAchievements()
	{
		return page.getAchievements();
	}
	
	public static void registerEvent(Object o)
	{
		MinecraftForge.EVENT_BUS.register(o);
		REGISTERED_EVENTS.add(o);
	}
}