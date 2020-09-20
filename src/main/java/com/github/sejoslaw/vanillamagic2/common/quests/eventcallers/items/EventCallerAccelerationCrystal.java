package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.items;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.quests.types.items.QuestAccelerationCrystal;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerAccelerationCrystal extends EventCallerVMItem<QuestAccelerationCrystal> {
    private final Random rand = new Random();

    @SubscribeEvent
    public void useItem(PlayerInteractEvent.RightClickBlock event) {
        this.executor.onPlayerInteract(event,
                (player, world, pos, direction) -> this.getVMItem().isVMItem(player.getHeldItemMainhand()) ? this.quests.get(0) : null,
                (player, world, pos, direction, quest) ->
                    this.executor.useVMItem(player, this.getVMItem().getUniqueKey(), (handStack) ->
                            WorldUtils.tick(world, pos, VMForgeConfig.ACCELERATION_CRYSTAL_UPDATE_TICKS.get(), this.rand)));
    }
}
