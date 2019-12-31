package com.github.sejoslaw.vanillamagic.api.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.quarry.IQuarry;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.quarry.IQuarryUpgrade;

/**
 * Base for all Quarry Events.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventQuarry extends EventMachine {
    private final IQuarry tileQuarry;

    public EventQuarry(IQuarry tileQuarry, World world, BlockPos customTilePos) {
        super(tileQuarry, world, customTilePos);
        this.tileQuarry = tileQuarry;
    }

    /**
     * @return Returns the TileQuarry.
     */
    public IQuarry getTileQuarry() {
        return tileQuarry;
    }

    /**
     * This Event is fired when a new Quarry has been build correctly on World.
     */
    public static class BuildCorrectly extends EventQuarry {
        public BuildCorrectly(IQuarry tileQuarry, World world, BlockPos customTilePos) {
            super(tileQuarry, world, customTilePos);
        }
    }

    /**
     * This Event is fired at the end of TileQuarry tick.
     */
    public static class Work extends EventQuarry {
        public Work(IQuarry tileQuarry, World world, BlockPos customTilePos) {
            super(tileQuarry, world, customTilePos);
        }
    }

    /**
     * This is base for all IQuarryUpgrade-related Events.
     */
    public static class QuarryUpgrade extends EventQuarry {
        private final IQuarryUpgrade upgrade;
        private final BlockPos upgradePos;

        public QuarryUpgrade(IQuarry tileQuarry, World world, BlockPos customTilePos, IQuarryUpgrade upgrade, BlockPos upgradePos) {
            super(tileQuarry, world, customTilePos);
            this.upgrade = upgrade;
            this.upgradePos = upgradePos;
        }

        /**
         * @return Returns the IQuarryUpgrade connected with this Event.
         */
        public IQuarryUpgrade getUpgrade() {
            return upgrade;
        }

        /**
         * @return Returns position of the block connected with QuarryUpgrade.
         */
        public BlockPos getUpgradePos() {
            return upgradePos;
        }
    }

    /**
     * This Event is fired when Quarry added correctly the given upgrade.
     */
    public static class AddUpgrade extends QuarryUpgrade {
        public AddUpgrade(IQuarry tileQuarry, World world, BlockPos customTilePos, IQuarryUpgrade upgrade, BlockPos upgradePos) {
            super(tileQuarry, world, customTilePos, upgrade, upgradePos);
        }
    }

    /**
     * This Event is fired when Quarry should be modified by the QuarryUpgrade.
     */
    public static class ModifyQuarry extends QuarryUpgrade {
        public ModifyQuarry(IQuarry tileQuarry, World world, BlockPos customTilePos, IQuarryUpgrade upgrade, BlockPos upgradePos) {
            super(tileQuarry, world, customTilePos, upgrade, upgradePos);
        }

        /**
         * This Event is fired BEFORE the QuarryUpgrade modifies the Quarry.
         */
        public static class Before extends ModifyQuarry {
            public Before(IQuarry tileQuarry, World world, BlockPos customTilePos, IQuarryUpgrade upgrade, BlockPos upgradePos) {
                super(tileQuarry, world, customTilePos, upgrade, upgradePos);
            }
        }

        /**
         * This Event is fired AFTER the QuarryUpgrade modified the Quarry.
         */
        public static class After extends ModifyQuarry {
            public After(IQuarry tileQuarry, World world, BlockPos customTilePos, IQuarryUpgrade upgrade, BlockPos upgradePos) {
                super(tileQuarry, world, customTilePos, upgrade, upgradePos);
            }
        }
    }
}