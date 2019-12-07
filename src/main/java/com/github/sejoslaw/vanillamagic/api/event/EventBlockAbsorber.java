package com.github.sejoslaw.vanillamagic.api.event;

import com.github.sejoslaw.vanillamagic.api.tileentity.blockabsorber.IBlockAbsorber;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This Event is fired BEFORE the TileBlockAbsorber changed upper block to air.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventBlockAbsorber extends EventCustomTileEntity {
    private final IBlockAbsorber tileAbsorber;
    private final HopperTileEntity connectedHopper;

    public EventBlockAbsorber(IBlockAbsorber tileAbsorber, World world, BlockPos tilePos, HopperTileEntity connectedHopper) {
        super(tileAbsorber, world, tilePos);
        this.tileAbsorber = tileAbsorber;
        this.connectedHopper = connectedHopper;
    }

    /**
     * @return Returns the TileBlockAbsorber.
     */
    public IBlockAbsorber getTileAbsorber() {
        return tileAbsorber;
    }

    /**
     * @return Returns the Hopper which is connected with this TileBlockAbsorber.
     */
    public HopperTileEntity getConnectedHopper() {
        return connectedHopper;
    }
}