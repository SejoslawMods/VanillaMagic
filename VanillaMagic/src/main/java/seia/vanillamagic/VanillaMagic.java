package seia.vanillamagic;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import seia.vanillamagic.chunkloader.ChunkLoadingHandler;
import seia.vanillamagic.chunkloader.ChunkLoadingHelper;
import seia.vanillamagic.quest.QuestList;
import seia.vanillamagic.quest.quarry.QuarryHandler;
import seia.vanillamagic.quest.quarry.QuestQuarryEvent;

@Mod(
		modid = VanillaMagic.MODID, 
		version = VanillaMagic.VERSION,
		name = VanillaMagic.NAME
		)
public class VanillaMagic 
{
	public static final String MODID = "vanillamagic";
	public static final String VERSION = "1.10.2-0.10.0.0";
	public static final String NAME = "Vanilla Magic";
	
	@Mod.Instance
	public static VanillaMagic INSTANCE;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		for(int i = 0; i < QuestList.QUESTS.size(); i++)
		{
			VanillaMagicQuestHandler.INSTANCE.registerEvent(QuestList.QUESTS.get(i));
			//System.out.println("Registered event: [" + QuestList.QUESTS.get(i).getQuestName() + "]");
		}
		System.out.println("Registered events: " + VanillaMagicQuestHandler.INSTANCE.registeredEvents.size());
		VanillaMagicDebug.INSTANCE.preInit();
		VanillaMagicIntegration.INSTANCE.preInit();
		// TODO: For future. Add quarrys to be saved and load from file.
		// TODO: Currently quarrys MUST BE replace each time the world is loaded ???
		MinecraftForge.EVENT_BUS.register(new QuestQuarryEvent()); // WorldTickEvent
		VanillaMagicRegistry.INSTANCE.preInit();
		QuarryHandler.INSTANCE.init();
		ChunkLoadingHelper.INSTANCE.init();
		ForgeChunkManager.setForcedChunkLoadingCallback(INSTANCE, new ChunkLoadingHandler());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		for(int i = 0; i < QuestList.QUESTS.size(); i++)
		{
			VanillaMagicQuestHandler.INSTANCE.addAchievement(QuestList.QUESTS.get(i).achievement);
			//System.out.println("Registered achievement: [" + QuestList.QUESTS.get(i).getUniqueName() + "]");
		}
		System.out.println("Registered achievements: " + VanillaMagicQuestHandler.INSTANCE.getAchievements().size());
		VanillaMagicIntegration.INSTANCE.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		VanillaMagicIntegration.INSTANCE.postInit();
	}
}