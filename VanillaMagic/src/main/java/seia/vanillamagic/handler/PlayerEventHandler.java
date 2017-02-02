package seia.vanillamagic.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import seia.vanillamagic.config.VMConfig;
import seia.vanillamagic.item.book.BookRegistry;
import seia.vanillamagic.item.book.IBook;
import seia.vanillamagic.util.NBTHelper;

public class PlayerEventHandler 
{
	public static final String NBT_PLAYER_HAS_BOOK = "NBT_PLAYER_HAS_BOOK";
	
	private PlayerEventHandler()
	{
	}
	
	public static void preInit() 
	{
		MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
	}
	
	@SubscribeEvent
	public void onPlayerLoggedInGiveBooks(PlayerLoggedInEvent event)
	{
		if(VMConfig.givePlayerCustomBooksOnLoggedIn)
		{
			EntityPlayer player = event.player;
			NBTTagCompound playerData = player.getEntityData();
			NBTTagCompound data = NBTHelper.getTagSafe(playerData, EntityPlayer.PERSISTED_NBT_TAG);
			if(!data.getBoolean(NBT_PLAYER_HAS_BOOK))
			{
				for(IBook book : BookRegistry.getBooks())
				{
					ItemHandlerHelper.giveItemToPlayer(player, book.getItem());
				}
				data.setBoolean(NBT_PLAYER_HAS_BOOK, true);
				playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);
			}
		}
	}
}