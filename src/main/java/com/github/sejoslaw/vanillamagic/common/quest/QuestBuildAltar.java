package com.github.sejoslaw.vanillamagic.quest;

import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.util.AltarUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestBuildAltar extends Quest {
	protected int tier;

	public void readData(JsonObject jo) {
		super.readData(jo);
		this.tier = jo.get("tier").getAsInt();
	}

	/**
	 * Returns the Tier of the Altar connected to this Quest.
	 */
	public int getTier() {
		return tier;
	}

	@SubscribeEvent
	public void placeBlock(PlaceEvent event) {
		PlayerEntity player = event.getPlayer();
		Block middleBlock = event.getPlacedBlock().getBlock();

		if (canPlayerGetQuest(player) && !hasQuest(player) && (middleBlock instanceof BlockCauldron)
				&& AltarUtil.checkAltarTier(player.world, event.getPos(), tier)) {
			addStat(player);
		}
	}
}