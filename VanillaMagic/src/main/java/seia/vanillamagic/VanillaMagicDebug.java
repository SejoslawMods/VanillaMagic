package seia.vanillamagic;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.quest.QuestList;
import seia.vanillamagic.utils.ItemStackHelper;

public class VanillaMagicDebug 
{
	public static final VanillaMagicDebug INSTANCE = new VanillaMagicDebug();
	public static final ItemStack DEBUG_OFF_HAND_ITEMSTACK = new ItemStack(Blocks.COMMAND_BLOCK, 64);
	public static final ItemStack DEBUG_MAIN_HAND_ITEMSTACK = DEBUG_OFF_HAND_ITEMSTACK;
	
	private VanillaMagicDebug()
	{
		try
		{
			ItemStackHelper.printItemStackInfo(DEBUG_OFF_HAND_ITEMSTACK, new String[]{"Left Hand"});
			ItemStackHelper.printItemStackInfo(DEBUG_MAIN_HAND_ITEMSTACK, new String[]{"Right Hand"});
			MinecraftForge.EVENT_BUS.register(this);
			VanillaMagic.logger.log(Level.INFO, "VanillaMagicDebug registered");
		}
		catch(Exception e)
		{
			VanillaMagic.logger.log(Level.INFO, "VanillaMagicDebug not registered");
		}
	}
	
	public void preInit()
	{
	}
	
	@SubscribeEvent
	public void activateDebug(RightClickItem event)
	{
		EntityPlayer player = event.getEntityPlayer();
		if(ItemStackHelper.checkItemsInHands(player, DEBUG_OFF_HAND_ITEMSTACK, DEBUG_MAIN_HAND_ITEMSTACK))
		{
			for(Quest quest : QuestList.QUESTS)
			{
				Achievement toAchieve = quest.achievement;
				player.addStat(toAchieve, 1);
			}
			for(Achievement a : AchievementList.ACHIEVEMENTS)
			{
				player.addStat(a, 1);
			}
		}
	}
}