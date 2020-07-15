package com.github.sejoslaw.vanillamagic2.common.quests.eventCallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestBuildAltar;
import com.github.sejoslaw.vanillamagic2.common.utils.AltarUtils;
import net.minecraft.block.CauldronBlock;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerBuildAltar extends EventCaller<QuestBuildAltar> {
    @SubscribeEvent
    public void buildAltar(BlockEvent.EntityPlaceEvent event) {
        this.executor.onEntityPlace(event, (player, world, state, pos) -> {
            if (!(state.getBlock() instanceof CauldronBlock)) {
                return null;
            }

            for (Quest quest : this.quests) {
                if (AltarUtils.checkAltarTier(world, pos, quest.altarTier)) {
                    return quest;
                }
            }

            return null;
        }, (player, world, state, pos) -> { });
    }
}
