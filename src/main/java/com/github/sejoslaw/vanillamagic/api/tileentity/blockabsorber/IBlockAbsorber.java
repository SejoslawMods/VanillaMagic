package com.github.sejoslaw.vanillamagic.api.tileentity.blockabsorber;

import com.github.sejoslaw.vanillamagic.api.tileentity.ICustomTileEntity;
import net.minecraft.tileentity.HopperTileEntity;

/**
 * This is a connection to TileBlockAbsorber.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IBlockAbsorber extends ICustomTileEntity {
    /**
     * @return Returns Hopper to which Block Absorber will export items.
     */
    HopperTileEntity getConnectedHopper();
}