package com.github.sejoslaw.vanillamagic.api.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface ICustomTileEntity extends ITileEntityObject, ITickableTileEntity, IAdditionalInfoProvider {
    /**
     * This initialization will be used for placing the ICustomTileEntity FOR THE FIRST TIME on the right position. <br>
     * As well as starting chunkloading the Chunk with current CustomTileEntity.
     */
    void init(World world, BlockPos pos);

    /**
     * @return True if CustomTileEntity can be added to World during loading the World.
     */
    boolean isPrepared();
}