package com.github.sejoslaw.vanillamagic.api.event;

import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import com.github.sejoslaw.vanillamagic.api.item.ICustomItem;

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

    /**
     * This Event is fired BEFORE Growable grow.
     */
    public static class Grow extends EventMotherNatureCrystal {
        private final FakePlayer fakePlayer;
        private final IGrowable growable;

        public Grow(ICustomItem usedItem, FakePlayer fakePlayer, World world, BlockPos clickedPos, IGrowable growable) {
            super(usedItem, fakePlayer, world, clickedPos);
            this.fakePlayer = fakePlayer;
            this.growable = growable;
        }

        /**
         * @return Returns FakePlayer who apply bonemeal effect.
         */
        public FakePlayer getFakePlayer() {
            return fakePlayer;
        }

        /**
         * @return Returns growable on which the effect will be used.
         */
        public IGrowable getGrowable() {
            return growable;
        }
    }
}