package seia.vanillamagic.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import seia.vanillamagic.VanillaMagic;

public class QuestHandler
{
	public static final QuestHandler INSTANCE = new QuestHandler();
	
	public final String PAGE_NAME = VanillaMagic.NAME;
	public final List<Object> registeredEvents = new ArrayList<Object>();
	
	private final AchievementPage page;
	
	private QuestHandler()
	{
		page = new AchievementPage(PAGE_NAME);
		AchievementPage.registerAchievementPage(page);
	}
	
	public void addAchievement(Achievement achievement)
	{
		page.getAchievements().add(achievement);
	}
	
	public List<Achievement> getAchievements()
	{
		return page.getAchievements();
	}
	
	public void registerEvent(Object o)
	{
		MinecraftForge.EVENT_BUS.register(o);
		INSTANCE.registeredEvents.add(o);
	}
}