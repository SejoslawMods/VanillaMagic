package seia.vanillamagic.quest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.event.EventJumper;
import seia.vanillamagic.magic.spell.spells.teleport.TeleportHelper;
import seia.vanillamagic.util.EntityUtil;
import seia.vanillamagic.util.ItemStackUtil;
import seia.vanillamagic.util.NBTUtil;
import seia.vanillamagic.util.TextUtil;

public class QuestJumper extends Quest
{
	private static final ItemStack _REQUIRED_RIGHT_HAND = new ItemStack(Items.COMPASS);
	private static final ItemStack _REQUIRED_LEFT_HAND = new ItemStack(Items.BOOK);
	private static final ItemStack _POSITION_HOLDER = new ItemStack(Items.ENCHANTED_BOOK);
	
	@SubscribeEvent
	public void saveBlockPosToBook(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		ItemStack leftHand = player.getHeldItemOffhand();
		if (!ItemStackUtil.checkItemsInHands(player, _REQUIRED_LEFT_HAND, _REQUIRED_RIGHT_HAND)) return;
		if (ItemStackUtil.getStackSize(leftHand) > 1) return;
		if (!player.isSneaking()) return;
		if (canPlayerGetQuest(player))
		{
			World world = event.getWorld();
			BlockPos posToSave = event.getPos().offset(event.getFace()); // clicked position moved by clicked face
			if (!MinecraftForge.EVENT_BUS.post(new EventJumper.SavePosition.Before(player, world, posToSave)))
			{
				player.setHeldItem(EnumHand.OFF_HAND, writeDataToBook(world, posToSave));
				addStat(player);
				EntityUtil.addChatComponentMessageNoSpam(player, "Position saved: " + TextUtil.constructPositionString(world, posToSave));
			}
			MinecraftForge.EVENT_BUS.post(new EventJumper.SavePosition.After(player, world, posToSave));
		}
	}
	
	public static ItemStack writeDataToBook(World world, BlockPos pos)
	{
		ItemStack bookWithData = _POSITION_HOLDER.copy();
		bookWithData.setStackDisplayName("Jumper's Book: " + TextUtil.constructPositionString(world, pos));
		bookWithData.setTagCompound(NBTUtil.setBlockPosDataToNBT(bookWithData.getTagCompound(), pos, world));
		return bookWithData;
	}
	
	/**
	 * In teleportation hands are switched.
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void teleportPlayer(RightClickItem event)
	{
		EntityPlayer player = event.getEntityPlayer();
		ItemStack rightHand = player.getHeldItemMainhand();
		if (!ItemStackUtil.checkItemsInHands(player, _REQUIRED_RIGHT_HAND, _POSITION_HOLDER)) return;
		if (!player.isSneaking()) return;
		NBTTagCompound bookData = rightHand.getTagCompound();
		if (bookData != null)
		{
			BlockPos teleportPos = NBTUtil.getBlockPosDataFromNBT(bookData);
			if (teleportPos != null)
			{
				int dimId = NBTUtil.getDimensionFromNBT(bookData);
				if (MinecraftForge.EVENT_BUS.post(new EventJumper.Teleport.Before(player, event.getWorld(), teleportPos, dimId))) return;
				
				if (player.dimension == dimId) TeleportHelper.teleportEntity(player, teleportPos); // Teleport occurres in the same Dimension
				else // Teleport to another Dimension
				{
					if (player instanceof EntityPlayerMP) TeleportHelper.changePlayerDimensionWithoutPortal((EntityPlayerMP) player, dimId);
				}
				EntityUtil.addChatComponentMessageNoSpam(player, "Teleported to: " + TextUtil.constructPositionString(player.getEntityWorld(), teleportPos));
				MinecraftForge.EVENT_BUS.post(new EventJumper.Teleport.After(player, event.getWorld(), teleportPos, dimId));
			}
		}
	}
	
	@SubscribeEvent
	public void showSavedPosTooltip(ItemTooltipEvent event)
	{
		ItemStack jumperBookStack = event.getItemStack();
		NBTTagCompound jumperBookTagCompound = jumperBookStack.getTagCompound();
		if (jumperBookTagCompound != null)
		{
			BlockPos teleportPos = NBTUtil.getBlockPosDataFromNBT(jumperBookTagCompound);
			if (teleportPos != null)
			{
				int dimId = NBTUtil.getDimensionFromNBT(jumperBookTagCompound);
				String info = TextUtil.constructPositionString(dimId, teleportPos);
				event.getToolTip().add("Jumper's Book: " + info);
			}
		}
	}
}