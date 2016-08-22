package seia.vanillamagic;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import seia.vanillamagic.handler.ChunkLoadingHandler;
import seia.vanillamagic.handler.QuestHandler;
import seia.vanillamagic.handler.WorldHandler;
import seia.vanillamagic.handler.customtileentity.CustomTileEntityHandler;
import seia.vanillamagic.items.VanillaMagicItems;
import seia.vanillamagic.items.book.BookRegistry;
import seia.vanillamagic.quest.QuestList;

@Mod(
		modid = VanillaMagic.MODID, 
		version = VanillaMagic.VERSION,
		name = VanillaMagic.NAME
		)
public class VanillaMagic 
{
	public static final String MODID = "vanillamagic";
	public static final String VERSION = "1.10.2-0.13.0.0";
	public static final String NAME = "Vanilla Magic";
	
	@Mod.Instance
	public static VanillaMagic INSTANCE;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		for(int i = 0; i < QuestList.QUESTS.size(); i++)
		{
			QuestHandler.INSTANCE.registerEvent(QuestList.QUESTS.get(i));
			//System.out.println("Registered event: [" + QuestList.QUESTS.get(i).getQuestName() + "]");
		}
		System.out.println("Registered events: " + QuestHandler.INSTANCE.registeredEvents.size());
		VanillaMagicDebug.INSTANCE.preInit();
		VanillaMagicRegistry.INSTANCE.preInit();
		ForgeChunkManager.setForcedChunkLoadingCallback(INSTANCE, new ChunkLoadingHandler());
		WorldHandler.INSTANCE.preInit(); //TODO: FIX
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		for(int i = 0; i < QuestList.QUESTS.size(); i++)
		{
			QuestHandler.INSTANCE.addAchievement(QuestList.QUESTS.get(i).achievement);
			//System.out.println("Registered achievement: [" + QuestList.QUESTS.get(i).getUniqueName() + "]");
		}
		System.out.println("Registered achievements: " + QuestHandler.INSTANCE.getAchievements().size());
		VanillaMagicIntegration.INSTANCE.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		BookRegistry.INSTANCE.postInit();
		CustomTileEntityHandler.INSTANCE.postInit();
		VanillaMagicItems.INSTANCE.postInit();
	}
}