package com.github.sejoslaw.vanillamagic.api.tileentity;

import net.minecraft.tileentity.TileEntity;

/**
 * Wraps a single {@link TileEntity}.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface ITileEntityWrapper {
    /**
     * @return Returns {@link TileEntity} to which this interface is implemented
     * into.
     */
    TileEntity getTileEntity();
}