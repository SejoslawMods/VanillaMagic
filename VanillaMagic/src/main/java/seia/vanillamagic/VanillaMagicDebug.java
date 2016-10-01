package seia.vanillamagic;

import java.util.List;

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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.handler.customtileentity.CustomTileEntityHandler;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.quest.QuestList;
import seia.vanillamagic.util.EntityHelper;
import seia.vanillamagic.util.IAdditionalInfoProvider;
import seia.vanillamagic.util.ItemStackHelper;
import seia.vanillamagic.util.WorldHelper;

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
			VanillaMagic.LOGGER.log(Level.INFO, "VanillaMagicDebug registered");
		}
		catch(Exception e)
		{
			VanillaMagic.LOGGER.log(Level.INFO, "VanillaMagicDebug not registered");
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
			for(Quest quest : QuestList.getQuests())
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
		if(stackRightHand == null)
		{
			return;
		}
		if(stackRightHand.getItem().equals(Items.CLOCK))
		{
			// show info
			BlockPos tilePos = event.getPos();
			TileEntity tile = CustomTileEntityHandler.INSTANCE.getCustomTileEntity(tilePos, WorldHelper.getDimensionID(player));
			if(tile instanceof IAdditionalInfoProvider)
			{
				List<String> info = ((IAdditionalInfoProvider) tile).getAdditionalInfo();
				for(String message : info)
				{
					EntityHelper.addChatComponentMessage(player, message);
				}
			}
		}
	}
}