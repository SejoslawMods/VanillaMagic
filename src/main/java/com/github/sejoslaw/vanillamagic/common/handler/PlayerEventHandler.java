package com.github.sejoslaw.vanillamagic.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import com.github.sejoslaw.vanillamagic.config.VMConfig;
import com.github.sejoslaw.vanillamagic.item.book.BookRegistry;
import com.github.sejoslaw.vanillamagic.item.book.IBook;
import com.github.sejoslaw.vanillamagic.util.NBTUtil;

/**
 * Class which contains Events connected with Player.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class PlayerEventHandler {
	public static final String NBT_PLAYER_HAS_BOOK = "NBT_PLAYER_HAS_BOOK";

	/**
	 * Event which will give Player books at first login to new World.
	 */
	@SubscribeEvent
	public void onPlayerLoggedInGiveBooks(PlayerLoggedInEvent event) {
		if (VMConfig.GIVE_PLAYER_CUSTOM_BOOKS_ON_LOGIN) {
			PlayerEntity player = event.player;
			CompoundNBT playerData = player.getEntityData();
			CompoundNBT data = NBTUtil.getTagSafe(playerData, PlayerEntity.PERSISTED_NBT_TAG);

			if (!data.getBoolean(NBT_PLAYER_HAS_BOOK)) {
				for (IBook book : BookRegistry.getBooks()) {
					ItemHandlerHelper.giveItemToPlayer(player, book.getItem());
				}

				data.setBoolean(NBT_PLAYER_HAS_BOOK, true);
				playerData.setTag(PlayerEntity.PERSISTED_NBT_TAG, data);
			}
		}
	}
}