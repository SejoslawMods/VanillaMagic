package com.github.sejoslaw.vanillamagic.common.quest;

import com.github.sejoslaw.vanillamagic.common.util.AltarUtil;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestBuildAltar extends Quest {
    protected int tier;

    public void readData(JsonObject jo) {
        super.readData(jo);
        this.tier = jo.get("tier").getAsInt();
    }

    @SubscribeEvent
    public void placeBlock(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity player = (PlayerEntity) entity;

        Block middleBlock = event.getPlacedBlock().getBlock();

        if (canPlayerGetQuest(player) && !hasQuest(player) && (middleBlock instanceof CauldronBlock) && AltarUtil.checkAltarTier(player.world, event.getPos(), tier)) {
            addStat(player);
        }
    }
}