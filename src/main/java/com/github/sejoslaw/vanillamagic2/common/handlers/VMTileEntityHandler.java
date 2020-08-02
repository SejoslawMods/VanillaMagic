package com.github.sejoslaw.vanillamagic2.common.handlers;

import com.github.sejoslaw.vanillamagic2.common.functions.Consumer2;
import com.github.sejoslaw.vanillamagic2.core.VMFiles;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

import java.io.File;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class VMTileEntityHandler {
    protected final String key = "tiles";

    protected void execute(WorldEvent event, Consumer2<World, File> consumer) {
        World world = event.getWorld().getWorld();
        File tileEntitiesFile = VMFiles.getVMTileEntitiesFilePath(world).toFile();
        tileEntitiesFile.getParentFile().mkdirs();
        consumer.accept(world, tileEntitiesFile);
    }
}
