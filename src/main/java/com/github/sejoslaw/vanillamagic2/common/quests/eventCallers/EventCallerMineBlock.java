package com.github.sejoslaw.vanillamagic2.common.quests.eventCallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestMineBlock;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerMineBlock extends EventCaller<QuestMineBlock> {
    @SubscribeEvent
    public void onBlockMined(BlockEvent.BreakEvent event) {
        this.executor.onBlockBreak(event,
                (player, world, pos, blockState) -> this.quests
                        .stream()
                        .filter(quest -> quest.blocksToMine.stream().anyMatch(block -> block == blockState.getBlock()))
                        .findFirst()
                        .orElse(null),
                (player, world, pos, blockState) -> { });
    }
}
