package com.github.sejoslaw.vanillamagic.common.handler;

import com.github.sejoslaw.vanillamagic.api.tileentity.ICustomTileEntity;
import com.github.sejoslaw.vanillamagic.core.VMConfig;
import com.github.sejoslaw.vanillamagic.core.VMLogger;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Class which is responsible for loading / saving CustomTileEntities.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class WorldHandler {
    private static final String TILES = "tiles";

    /**
     * Show additional console info if enabled in config,
     */
    public void log(Level level, String message) {
        if (VMConfig.SHOW_CUSTOM_TILE_ENTITY_SAVING.get()) {
            VMLogger.log(level, message);
        }
    }

    /**
     * Event which loads all CustomTileEntities to for World.
     */
    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event) {
        World world = event.getWorld().getWorld();
        File vmDirectory = getVanillaMagicRootDirectory(world);

        if (vmDirectory == null) {
            return;
        }

        if (!vmDirectory.exists()) {
            vmDirectory.mkdirs();
            return;
        }

        int dimension = world.getDimension().getType().getId();
        File directoryDimension = new File(vmDirectory, dimension + "/");

        if (!directoryDimension.exists()) {
            directoryDimension.mkdirs();
            return;
        }

        File[] dimFiles = directoryDimension.listFiles();

        try {
            for (File dimFile : dimFiles) {
                String dimFileName = dimFile.getName();
                String fileExtension = dimFileName.substring(dimFileName.lastIndexOf("."));

                if (fileExtension.equals("dat")) {
                    FileInputStream fis = new FileInputStream(dimFile);
                    CompoundNBT data;

                    try {
                        data = CompressedStreamTools.readCompressed(fis);
                    } catch (EOFException eof) {
                        log(Level.ERROR, "[World Load] Error while reading CustomTileEntities data from file for Dimension: " + dimension);
                        return;
                    }

                    fis.close();

                    ListNBT tagList = data.getList(TILES, 10);
                    boolean canAdd;

                    for (int i = 0; i < tagList.size(); ++i) {
                        CompoundNBT tileEntityTag = tagList.getCompound(i);
                        TileEntity tileEntity = null;

                        try {
                            tileEntity = TileEntity.create(tileEntityTag);
                            tileEntity.setWorldAndPos(world, tileEntity.getPos());

                            ((ICustomTileEntity) tileEntity).init();

                            canAdd = ((ICustomTileEntity) tileEntity).isPrepared();

                            log(Level.INFO, "[World Load] Created CustomTileEntity (" + tileEntity.getClass().getSimpleName() + ")");
                        } catch (Exception e) {
                            log(Level.ERROR, "[World Load] Error while reading class for CustomTileEntity: " + tileEntity.getClass().getName());
                            continue;
                        }

                        if (canAdd && !tileEntity.getPos().equals(CustomTileEntityHandler.EMPTY_SPACE)) {
                            tileEntity.deserializeNBT(tileEntityTag);
                            CustomTileEntityHandler.addCustomTileEntity((ICustomTileEntity) tileEntity, world);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log(Level.INFO, "[World Load] Error while loading CustomTileEntities for Dimension: " + dimension);
            e.printStackTrace();
        }
    }

    /**
     * Event which saves all CustomTileEntities for given World.
     */
    @SubscribeEvent
    public void worldSave(WorldEvent.Save event) {
        World world = event.getWorld().getWorld();
        File vmDirectory = getVanillaMagicRootDirectory(world);

        if (vmDirectory == null) {
            return;
        }

        if (!vmDirectory.exists()) {
            vmDirectory.mkdirs();
        }

        int dimension = world.getDimension().getType().getId();
        File folderDimension = Paths.get(vmDirectory.getAbsolutePath(), String.valueOf(dimension)).toFile(); //new File(vmDirectory, String.valueOf(dimension) + "/");

        if (!folderDimension.exists()) {
            folderDimension.mkdirs();
        }

        File fileTiles = new File(folderDimension, "VanillaMagicTileEntities.dat");

        if (!fileTiles.exists()) {
            try {
                fileTiles.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File fileTilesOld = new File(folderDimension, "VanillaMagicTileEntities.dat_old");

        try {
            Files.copy(fileTiles.toPath(), fileTilesOld.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            CompoundNBT data = new CompoundNBT();
            ListNBT entityListNBT = new ListNBT();
            List<ICustomTileEntity> customTileEntities = CustomTileEntityHandler.getCustomEntitiesInDimension(world);

            for (ICustomTileEntity customTileEntity : customTileEntities) {
                entityListNBT.add(customTileEntity.asTileEntity().serializeNBT());
            }

            data.put(TILES, entityListNBT);
            FileOutputStream fileOutputStream = new FileOutputStream(fileTiles);
            CompressedStreamTools.writeCompressed(data, fileOutputStream);
            fileOutputStream.close();

            log(Level.INFO, "[World Save] CustomTileEntities saved for Dimension: " + dimension);
        } catch (Exception e) {
            log(Level.INFO, "[World Save] Error while saving CustomTileEntities for Dimension: " + dimension);
            e.printStackTrace();
        }
    }

    /**
     * @return Returns VM root directory for CustomTileEntities.
     */
    private static File getVanillaMagicRootDirectory(World world) {
        if (world instanceof ClientWorld) {
            return null;
        }

        File file = Paths.get(
                FMLPaths.GAMEDIR.get().toString(),
                "saves",
                world.getServer().getWorldName(),
                "VanillaMagic").toFile();

        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }
}
