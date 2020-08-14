package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.tileentities;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.tileentities.QuestBlockAbsorber;
import com.github.sejoslaw.vanillamagic2.common.tileentities.VMTileBlockAbsorber;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerBlockAbsorber extends EventCaller<QuestBlockAbsorber> {
    @SubscribeEvent
    public void addVMTile(PlayerInteractEvent.RightClickBlock event) {
        this.executor.onPlayerInteract(event,
                (player, world, pos, direction) -> this.executor.click(Blocks.HOPPER, world, pos, () -> this.quests.get(0)),
                (player, world, pos, direction, quest) -> WorldUtils.spawnVMTile(world, pos.offset(Direction.UP), new VMTileBlockAbsorber(), (tile) -> { }));
    }
}
