package com.github.sejoslaw.vanillamagic2.core;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.functions.Consumer4;
import com.github.sejoslaw.vanillamagic2.common.handlers.core.PlayerQuestProgressLoadHandler;
import com.github.sejoslaw.vanillamagic2.common.handlers.core.PlayerQuestProgressSaveHandler;
import com.github.sejoslaw.vanillamagic2.common.handlers.core.VMTileEntityLoadHandler;
import com.github.sejoslaw.vanillamagic2.common.handlers.core.VMTileEntitySaveHandler;
import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import com.github.sejoslaw.vanillamagic2.common.json.JsonService;
import com.github.sejoslaw.vanillamagic2.common.utils.EntityUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class VMFiles {
    public static void initialize() {
        // General Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, VMForgeConfig.COMMON_CONFIG, "VanillaMagicConfig.toml");

        // Quests Config
        unzip(getQuestsFilePath().toFile(), getQuestsFileSourcePath().toFile());

        // VM TileEntities
        VMEvents.register(new VMTileEntitySaveHandler());
        VMEvents.register(new VMTileEntityLoadHandler());

        // Player Quest Progress
        VMEvents.register(new PlayerQuestProgressSaveHandler());
        VMEvents.register(new PlayerQuestProgressLoadHandler());
    }

    public static String getQuestsFileName() {
        return "VanillaMagicQuests.json";
    }

    /**
     * @return Path to the game directory.
     */
    public static Path getGameDir() {
        return FMLPaths.GAMEDIR.get();
    }

    /**
     * @return Path to file with Quests.
     */
    public static Path getQuestsFilePath() {
        return Paths.get(FMLPaths.CONFIGDIR.get().toString(), getQuestsFileName());
    }

    /**
     * @return Path to VanillaMagic directory in IWorld directory.
     */
    public static Path getVMWorldDir(String worldName) {
        String path = getGameDir().toString();

        if (VMEvents.isClient()) {
            path = Paths.get(path, "saves").toString();
        }

        return Paths.get(path, worldName, "VanillaMagic");
    }

    /**
     * @return Path to file with VM Tile Entities in specified IWorld directory.
     */
    public static Path getVMTileEntitiesFilePath(World world) {
        String worldName = WorldUtils.getWorldName(world);
        String dimensionId = String.valueOf(world.getDimension().getType().getId());
        return Paths.get(getVMWorldDir(worldName).toString(), dimensionId, "VanillaMagicTileEntities.dat");
    }

    /**
     * Parses Player data for Quests manipulation.
     */
    public static void parsePlayerQuests(PlayerEntity player, Consumer4<PlayerEntity, String, String, File> consumer) {
        String playerName = EntityUtils.getPlayerName(player);
        String worldName = WorldUtils.getWorldName(player.getEntityWorld());

        Path playerQuestsPath = getPlayerQuestsFilePath(worldName, playerName);
        File playerQuestsFile = playerQuestsPath.toFile();

        consumer.accept(player, worldName, playerName, playerQuestsFile);
    }

    /**
     * @return Path to JSON file with Player's quests in specified IWorld.
     */
    public static Path getPlayerQuestsFilePath(String worldName, String playerName) {
        return Paths.get(getVMWorldDir(worldName).toString(), "players_quests", playerName + ".json");
    }

    /**
     * @return Path to the source from where the Quests file should be read.
     */
    public static Path getQuestsFileSourcePath() {
        Path modsPath = FMLPaths.MODSDIR.get();
        File modsFile = modsPath.toFile();

        if (modsFile.exists()) {
            for (String modFilePath : modsFile.list()) {
                if (modFilePath.toLowerCase().startsWith(VanillaMagic.MODID)) {
                    return Paths.get(modsPath.toString(), modFilePath);
                }
            }
        }

        return Paths.get(modsPath.getParent().getParent().toString(), "src", "main", "resources", getQuestsFileName());
    }

    /**
     * Unzips the source file into the destination file.
     */
    public static void unzip(File destinationFile, File sourceFile) {
        if (destinationFile.exists()) {
            return;
        }

        try {
            destinationFile.createNewFile();

            InputStream is = null;
            FileOutputStream fos = null;

            if (sourceFile.getName().endsWith("jar")) {
                JarFile jarFile = new JarFile(sourceFile);
                Enumeration<JarEntry> enumeration = jarFile.entries();

                while (enumeration.hasMoreElements()) {
                    JarEntry file = enumeration.nextElement();

                    if (file.getName().equals(getQuestsFileName())) {
                        is = jarFile.getInputStream(file);
                        fos = new FileOutputStream(destinationFile);

                        break;
                    }
                }
            } else {
                is = new FileInputStream(sourceFile);
                fos = new FileOutputStream(destinationFile);
            }

            if (is == null) {
                throw new FileNotFoundException(destinationFile.getName() + "file not found or is corrupted. Destination path: " + destinationFile.getAbsolutePath() + "; Source path: " + sourceFile.getAbsolutePath());
            }

            while (is.available() > 0) {
                fos.write(is.read());
            }

            fos.close();
            is.close();
            VMLogger.logInfo(destinationFile.getName() + " created.");
        } catch (Exception e) {
            e.printStackTrace();
            VMLogger.logInfo("Error while creating file: " + destinationFile.getName());
        }
    }

    public static void readJsonArray(File file, Consumer<IJsonService> consumer) {
        readJsonInternal(file, reader -> JsonService.parseArray(reader, consumer));
    }

    public static void readJson(File file, Consumer<IJsonService> consumer) {
        readJsonInternal(file, reader -> JsonService.parse(reader, consumer));
    }

    public static void writeJson(File file, Consumer<OutputStreamWriter> consumer) {
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            consumer.accept(osw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readJsonInternal(File file, Consumer<InputStreamReader> consumer) {
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            FileInputStream fis = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fis);

            consumer.accept(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
