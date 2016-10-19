package seia.vanillamagic.item.inventoryselector;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.inventory.InventoryHelper;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.util.EntityHelper;
import seia.vanillamagic.util.NBTHelper;
import seia.vanillamagic.util.WorldHelper;

public class InventorySelector
{
	public static void preInit()
	{
		MinecraftForge.EVENT_BUS.register(new InventorySelector());
		VanillaMagic.LOGGER.log(Level.INFO, "Inventory Selector registered");
	}
	
	int clicks = 0;
	@SubscribeEvent
	public void selectInventory(RightClickBlock event)
	{
		clicks++;
		if(clicks == 1)
		{
			clicks++;
		}
		else
		{
			clicks = 0;
			return;
		}
		World world = event.getWorld();
		if(world.isRemote)
		{
			return;
		}
		EntityPlayer player = event.getEntityPlayer();
		ItemStack rightHand = player.getHeldItemMainhand();
		if(rightHand == null)
		{
			return;
		}
		if(VanillaMagicItems.INSTANCE.isCustomItem(rightHand, VanillaMagicItems.INSTANCE.itemInventorySelector))
		{
			BlockPos clickedPos = event.getPos();
			if(!InventoryHelper.isInventory(world, clickedPos))
			{
				EntityHelper.addChatComponentMessage(player, "Clicked block is not an Inventory");
				return;
			}
			NBTTagCompound rightHandTagOld = rightHand.getTagCompound();
			NBTTagCompound rightHandTagNew = NBTHelper.setBlockPosDataToNBT(rightHandTagOld, clickedPos, world);
			rightHand.setTagCompound(rightHandTagNew);
			EntityHelper.addChatComponentMessage(player, "Registered Inventory at: X=" + clickedPos.getX() + 
					", Y=" + clickedPos.getY() + ", Z=" + clickedPos.getZ() + ", Dim=" + WorldHelper.getDimensionID(world));
		}
	}
	
	@SubscribeEvent
	public void showSavedPosition(RightClickItem event)
	{
		World world = event.getWorld();
		if(!world.isRemote)
		{
			return;
		}
		EntityPlayer player = event.getEntityPlayer();
		ItemStack rightHand = player.getHeldItemMainhand();
		if(rightHand == null)
		{
			return;
		}
		if(VanillaMagicItems.INSTANCE.isCustomItem(rightHand, VanillaMagicItems.INSTANCE.itemInventorySelector))
		{
			NBTTagCompound rightHandTag = rightHand.getTagCompound();
			if(player.isSneaking())
			{
				// Clear saved position
				if(rightHandTag.hasKey(NBTHelper.NBT_POSX)) // checking only posX
				{
					EntityHelper.addChatComponentMessage(player, "Cleared position");
					rightHandTag.removeTag(NBTHelper.NBT_POSX);
					rightHandTag.removeTag(NBTHelper.NBT_POSY);
					rightHandTag.removeTag(NBTHelper.NBT_POSZ);
				}
			}
			else
			{
				// Show saved position
				BlockPos savedPos = NBTHelper.getBlockPosDataFromNBT(rightHandTag);
				if(savedPos == null)
				{
					EntityHelper.addChatComponentMessage(player, "No saved position");
					return;
				}
				EntityHelper.addChatComponentMessage(player, "Saved position: X=" + savedPos.getX() + 
						", Y=" + savedPos.getY() + ", Z=" + savedPos.getZ() + ", Dim=" + WorldHelper.getDimensionID(world));
			}
		}
	}
}