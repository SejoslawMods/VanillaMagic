package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
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
        this.executor.onEntityPlace(event,
                (player, world, state, pos) ->
                        this.quests
                            .stream()
                            .filter(quest -> state.getBlock() instanceof CauldronBlock && AltarUtils.checkAltarTier(world, pos, quest.altarTier))
                            .findFirst()
                            .get(),
                (player, world, state, pos) -> { });
    }
}
