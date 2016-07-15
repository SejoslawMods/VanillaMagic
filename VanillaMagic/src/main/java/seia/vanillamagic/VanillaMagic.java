package seia.vanillamagic;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import seia.vanillamagic.quest.QuestList;

@Mod(
		modid = VanillaMagic.MODID, 
		version = VanillaMagic.VERSION,
		name = VanillaMagic.NAME
		)
public class VanillaMagic 
{
	public static final String MODID = "vanillamagic";
	public static final String VERSION = "1.10.2-0.3.0.0";
	public static final String NAME = "Vanilla Magic";
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		for(int i = 0; i < QuestList.QUESTS.size(); i++)
		{
			VanillaMagicQuestHandler.INSTANCE.registerEvent(QuestList.QUESTS.get(i));
			String name = QuestList.QUESTS.get(i).getQuestName();
			System.out.println("Registered event: [" + name + "]");
		}
		System.out.println("Registered events: " + VanillaMagicQuestHandler.INSTANCE.registeredEvents.size());
		VanillaMagicDebug.INSTANCE.init();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		for(int i = 0; i < QuestList.QUESTS.size(); i++)
		{
			VanillaMagicQuestHandler.INSTANCE.addAchievement(QuestList.QUESTS.get(i).getAchievement());
			String name = QuestList.QUESTS.get(i).getUniqueName();
			System.out.println("Registered achievement: [" + name + "]");
		}
		System.out.println("Registered achievements: " + VanillaMagicQuestHandler.INSTANCE.getAchievements().size());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}
}