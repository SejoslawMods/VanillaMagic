package seia.vanillamagic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;

public class VanillaMagicQuestHandler
{
	public static final VanillaMagicQuestHandler INSTANCE = new VanillaMagicQuestHandler();
	
	public final String PAGE_NAME = VanillaMagic.NAME;
	public final List<Object> registeredEvents = new ArrayList<Object>();
	
	private final AchievementPage page;
	
	private VanillaMagicQuestHandler()
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