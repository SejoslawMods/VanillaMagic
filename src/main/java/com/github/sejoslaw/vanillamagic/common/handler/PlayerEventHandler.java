package com.github.sejoslaw.vanillamagic.common.handler;

import com.github.sejoslaw.vanillamagic.core.VMConfig;
import com.github.sejoslaw.vanillamagic.common.item.book.BookRegistry;
import com.github.sejoslaw.vanillamagic.common.item.book.IBook;
import com.github.sejoslaw.vanillamagic.common.util.NBTUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

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
    public void onPlayerLoggedInGiveBooks(PlayerEvent.PlayerLoggedInEvent event) {
        if (!VMConfig.GIVE_PLAYER_CUSTOM_BOOKS_ON_LOGIN.get()) {
            return;
        }

        PlayerEntity player = event.getPlayer();
        CompoundNBT playerData = player.writeWithoutTypeId(new CompoundNBT());
        CompoundNBT data = NBTUtil.getTagSafe(playerData, PlayerEntity.PERSISTED_NBT_TAG);

        if (data.getBoolean(NBT_PLAYER_HAS_BOOK)) {
            return;
        }

        for (IBook book : BookRegistry.getBooks()) {
            ItemHandlerHelper.giveItemToPlayer(player, book.getItem());
        }

        data.putBoolean(NBT_PLAYER_HAS_BOOK, true);
        playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
    }
}