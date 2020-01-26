package com.github.sejoslaw.vanillamagic.common.quest;

import com.github.sejoslaw.vanillamagic.common.util.BlockUtil;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestMineBlock extends Quest {
    protected List<Block> blocksToBeMined;

    public void readData(JsonObject jo) {
        super.readData(jo);

        String blockName = jo.get("blocksToBeMined").getAsString();
        this.blocksToBeMined = ForgeRegistries.BLOCKS
                .getEntries()
                .stream()
                .filter(entry -> entry.getKey().toString().contains(blockName))
                .map(entry -> entry.getValue())
                .collect(Collectors.toList());
    }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();

        if (!canPlayerGetQuest(player)) {
            return;
        }

        Block block = event.getState().getBlock();

        for (Block value : blocksToBeMined) {
            if (BlockUtil.areEqual(block, value)) {
                if (!hasQuest(player)) {
                    addStat(player);
                }
                return;
            }
        }
    }
}