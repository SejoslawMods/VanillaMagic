package com.github.sejoslaw.vanillamagic.api.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IFarm;

/**
 * Event connected with TileFarm.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventFarm extends EventMachine {
    private final IFarm tileFarm;

    public EventFarm(IFarm tileFarm, World world, BlockPos customTilePos) {
        super(tileFarm, world, customTilePos);
        this.tileFarm = tileFarm;
    }

    /**
     * @return Returns the TileFarm.
     */
    public IFarm getTileFarm() {
        return tileFarm;
    }

    /**
     * This Event is fired at the end of TileFarm tick. After all the work has been
     * done.
     */
    public static class Work extends EventFarm {
        public Work(IFarm tileFarm, World world, BlockPos customTilePos) {
            super(tileFarm, world, customTilePos);
        }
    }
}