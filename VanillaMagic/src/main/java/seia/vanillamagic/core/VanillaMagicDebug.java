package seia.vanillamagic.core;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.quest.IQuest;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;
import seia.vanillamagic.api.util.IAdditionalInfoProvider;
import seia.vanillamagic.handler.CustomTileEntityHandler;
import seia.vanillamagic.quest.QuestList;
import seia.vanillamagic.util.EntityHelper;
import seia.vanillamagic.util.ItemStackHelper;

public class VanillaMagicDebug 
{
	public static final VanillaMagicDebug INSTANCE = new VanillaMagicDebug();
	public static final ItemStack DEBUG_OFF_HAND_ITEMSTACK = new ItemStack(Blocks.COMMAND_BLOCK, 64);
	public static final ItemStack DEBUG_MAIN_HAND_ITEMSTACK = DEBUG_OFF_HAND_ITEMSTACK;
	
	private VanillaMagicDebug()
	{
		MinecraftForge.EVENT_BUS.register(this);
		VanillaMagic.LOGGER.log(Level.INFO, "VanillaMagicDebug registered");
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
			// Handle all Quests
			for(IQuest quest : QuestList.getQuests())
			{
				Achievement toAchieve = quest.getAchievement();
				player.addStat(toAchieve, 1);
			}
			// Handle all Basic Achievements
			for(Achievement a : AchievementList.ACHIEVEMENTS)
			{
				player.addStat(a, 1);
			}
			// Handle all other achievements
			Set<AchievementPage> pages = AchievementPage.getAchievementPages();
			for(AchievementPage page : pages)
			{
				List<Achievement> achievements = page.getAchievements();
				for(Achievement achievement : achievements)
				{
					if(!player.hasAchievement(achievement))
					{
						player.addStat(achievement, 1);
					}
				}
			}
		}
	}
	
	int showTime = 1; // hue hue
	/**
	 * Method used for showing additional information of the CustomTileEntitys. <br>
	 * Right-click with Clock on IAdditionalInfoProvider.
	 */
	@SubscribeEvent
	public void showTileEntityInfo(RightClickBlock event)
	{
		World world = event.getWorld();
		if(world.isRemote)
		{
			return;
		}
		
		if(showTime == 1)
		{
			showTime++;
		}
		else
		{
			showTime = 1;
			return;
		}
		
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stackRightHand = player.getHeldItemMainhand();
		if(ItemStackHelper.isNullStack(stackRightHand))
		{
			return;
		}
		if(stackRightHand.getItem().equals(Items.CLOCK))
		{
			// show info
			BlockPos tilePos = event.getPos();
			ICustomTileEntity customTile = CustomTileEntityHandler.getCustomTileEntity(tilePos, world);
			if(customTile == null)
			{
				return;
			}
			TileEntity tile = customTile.getTileEntity();
			if(tile instanceof ICustomTileEntity)
			{
				if(customTile.getTileEntity() instanceof IAdditionalInfoProvider)
				{
					List<String> info = ((IAdditionalInfoProvider) customTile.getTileEntity()).getAdditionalInfo();
					for(String message : info)
					{
						EntityHelper.addChatComponentMessage(player, message);
					}
				}
			}
		}
	}
}