package com.github.sejoslaw.vanillamagic.common.quest;

import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.google.gson.JsonObject;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestPickup extends Quest {
    protected ItemStack whatToPick;

    public void readData(JsonObject jo) {
        this.whatToPick = ItemStackUtil.getItemStackFromJSON(jo.get("whatToPick").getAsJsonObject());
        this.icon = whatToPick.copy();

        super.readData(jo);
    }

    @SubscribeEvent
    public void pickItem(PlayerEvent.ItemPickupEvent event) {
        PlayerEntity player = event.getPlayer();

        if (!canPlayerGetQuest(player)) {
            return;
        }

        ItemEntity onGround = event.getOriginalEntity();

        if (!ItemStack.areItemStacksEqual(whatToPick, onGround.getItem()) || hasQuest(player)) {
            return;
        }

        addStat(player);
    }
}