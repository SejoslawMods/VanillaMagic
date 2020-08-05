package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.tileentities;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.tileentities.QuestQuarry;
import com.github.sejoslaw.vanillamagic2.common.registries.MachineModuleRegistry;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.VMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerQuarry extends EventCaller<QuestQuarry> {
    @SubscribeEvent
    public void addVMTile(PlayerInteractEvent.RightClickBlock event) {
        this.executor.onPlayerInteract(event,
                (player, world, pos, direction) -> this.executor.click(Blocks.CAULDRON, world, pos, () -> this.quests.get(0)),
                (player, world, pos, direction, quest) -> WorldUtils.spawnVMTile(world, pos, new VMTileMachine(),
                        (tile) -> tile.setModuleKey(MachineModuleRegistry.QUARRY_KEY)));
    }
}
