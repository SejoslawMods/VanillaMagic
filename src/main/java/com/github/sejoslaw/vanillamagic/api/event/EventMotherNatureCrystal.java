package com.github.sejoslaw.vanillamagic.api.event;

import com.github.sejoslaw.vanillamagic.api.item.ICustomItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Base class for all Mother Nature Crystal related events.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventMotherNatureCrystal extends EventCustomItem.OnUseByPlayer {
    public EventMotherNatureCrystal(ICustomItem usedItem, PlayerEntity user, World world, BlockPos usedOnPos) {
        super(usedItem, user, world, usedOnPos);
    }

    /**
     * This Event is fired BEFORE block is ticked.
     */
    public static class TickBlock extends EventMotherNatureCrystal {
        public TickBlock(ICustomItem usedItem, PlayerEntity user, World world, BlockPos usedOnPos) {
            super(usedItem, user, world, usedOnPos);
        }
    }
}