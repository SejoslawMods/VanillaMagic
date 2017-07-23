package seia.vanillamagic.core;

import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.quest.IQuest;
import seia.vanillamagic.api.quest.QuestList;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;
import seia.vanillamagic.api.util.IAdditionalInfoProvider;
import seia.vanillamagic.handler.CustomTileEntityHandler;
import seia.vanillamagic.util.EntityUtil;
import seia.vanillamagic.util.ItemStackUtil;
import seia.vanillamagic.util.QuestUtil;

/**
 * VM Debug Tools.
 * Right-Click with Clock to show additional information,
 * 64x Command Block in left and right hand and right-click in air to get all Quests,
 * etc.
 */
public class VanillaMagicDebug 
{
	public static final VanillaMagicDebug INSTANCE = new VanillaMagicDebug();
	
	/**
	 * ItemStack required in offhand to enable debug / get all Quests
	 */
	public static final ItemStack DEBUG_OFF_HAND_ITEMSTACK = new ItemStack(Blocks.COMMAND_BLOCK, 64);
	
	/**
	 * ItemStack required in mainhand to enable debug / get all Quests
	 */
	public static final ItemStack DEBUG_MAIN_HAND_ITEMSTACK = DEBUG_OFF_HAND_ITEMSTACK;
	
	private VanillaMagicDebug()
	{
		MinecraftForge.EVENT_BUS.register(this);
		VanillaMagic.LOGGER.log(Level.INFO, "VanillaMagicDebug registered");
	}
	
	/**
	 * Run in PreInitialization stage.
	 */
	public void preInit()
	{
	}
	
	/**
	 * 64x Command Block in left and right hand and right-click in air to get all the Quests
	 */
	@SubscribeEvent
	public void activateDebug(RightClickItem event)
	{
		EntityPlayer player = event.getEntityPlayer();
		if (ItemStackUtil.checkItemsInHands(player, DEBUG_OFF_HAND_ITEMSTACK, DEBUG_MAIN_HAND_ITEMSTACK)) activate(player);
	}
	
	/**
	 * Give all Quests to specified Player.
	 */
	public static void activate(EntityPlayer player)
	{
		if (EntityUtil.isMultiPlayer(player))
		{
			EntityPlayerMP playerMP = EntityUtil.toMultiPlayer(player);
			Iterable<Advancement> advancements = Minecraft.getMinecraft().getIntegratedServer().getAdvancementManager().getAdvancements();
			for (Advancement advancement : advancements)
			{
				AdvancementProgress progress = playerMP.getAdvancements().getProgress(advancement);
				for (String criterion : progress.getRemaningCriteria())
					playerMP.getAdvancements().grantCriterion(advancement, criterion);
			}
			for (IQuest quest : QuestList.getQuests()) QuestUtil.addStat(playerMP, quest);
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
		if (world.isRemote) return;
		
		if (showTime == 1) showTime++;
		else
		{
			showTime = 1;
			return;
		}
		
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stackRightHand = player.getHeldItemMainhand();
		if (ItemStackUtil.isNullStack(stackRightHand)) return;
		
		if (stackRightHand.getItem().equals(Items.CLOCK))
		{
			// show info
			BlockPos tilePos = event.getPos();
			ICustomTileEntity customTile = CustomTileEntityHandler.getCustomTileEntity(tilePos, world);
			if (customTile == null) return;
			
			TileEntity tile = customTile.getTileEntity();
			if (tile instanceof ICustomTileEntity)
			{
				if (customTile.getTileEntity() instanceof IAdditionalInfoProvider)
				{
					List<String> info = ((IAdditionalInfoProvider) customTile.getTileEntity()).getAdditionalInfo();
					EntityUtil.addChatComponentMessageNoSpam(player, info.toArray(new String[info.size()]));
				}
			}
		}
	}
}