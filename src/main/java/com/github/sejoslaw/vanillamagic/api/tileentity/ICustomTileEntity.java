package com.github.sejoslaw.vanillamagic.api.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface ICustomTileEntity extends ITileEntityObject, ITickableTileEntity, IAdditionalInfoProvider {
    /**
     * Performs additional logic when current CustomTileEntity is constructed.
     */
    void init();

    /**
     * Setups CustomTileEntity.
     */
    void setup(World world, BlockPos pos);

    /**
     * @return True if CustomTileEntity can be added to World during loading the World.
     */
    boolean isPrepared();
}