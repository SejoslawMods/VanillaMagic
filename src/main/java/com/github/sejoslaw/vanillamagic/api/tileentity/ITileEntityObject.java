package com.github.sejoslaw.vanillamagic.api.tileentity;

import net.minecraft.tileentity.TileEntity;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface ITileEntityObject {
    /**
     * @return Parses current object to vanilla TileEntity.
     */
    TileEntity asTileEntity();
}