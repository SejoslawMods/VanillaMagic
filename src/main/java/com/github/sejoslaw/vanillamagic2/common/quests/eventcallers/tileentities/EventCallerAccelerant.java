package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.tileentities;

import com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.items.EventCallerVMItem;
import com.github.sejoslaw.vanillamagic2.common.quests.types.tileentities.QuestAccelerant;
import com.github.sejoslaw.vanillamagic2.common.tileentities.VMTileAccelerant;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerAccelerant extends EventCallerVMItem<QuestAccelerant> {
    @SubscribeEvent
    public void addVMTile(PlayerInteractEvent.RightClickBlock event) {
        this.executor.onPlayerInteract(event,
                (player, world, pos, direction) -> this.executor.click(Blocks.CAULDRON, world, pos, () -> this.quests.get(0)),
                (player, world, pos, direction, quest) ->
                        this.executor.useVMItem(player, this.getVMItem().getUniqueKey(), (handStack) ->
                                WorldUtils.spawnVMTile(world, pos, new VMTileAccelerant(), (tile) -> true)));
    }
}
