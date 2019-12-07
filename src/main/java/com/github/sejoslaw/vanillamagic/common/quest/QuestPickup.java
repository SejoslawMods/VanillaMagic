package com.github.sejoslaw.vanillamagic.quest;

import com.google.gson.JsonObject;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemEntityPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;

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

	public ItemStack getWhatToPick() {
		return whatToPick;
	}

	@SubscribeEvent
	public void pickItem(ItemEntityPickupEvent event) {
		PlayerEntity player = event.getPlayerEntity();

		if (!canPlayerGetQuest(player)) {
			return;
		}

		ItemEntity onGround = event.getItem();

		if (!ItemStack.areItemStacksEqual(whatToPick, onGround.getItem()) || hasQuest(player)) {
			return;
		}

		addStat(player);
	}
}