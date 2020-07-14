package com.github.sejoslaw.vanillamagic2.core;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.functions.Consumer3;
import com.github.sejoslaw.vanillamagic2.common.handlers.PlayerQuestProgressLoadHandler;
import com.github.sejoslaw.vanillamagic2.common.handlers.PlayerQuestProgressSaveHandler;
import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import com.github.sejoslaw.vanillamagic2.common.json.JsonService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
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
        unzip(VMFiles.getQuestsFilePath().toFile(), VMFiles.getQuestsFileSourcePath().toFile());

        // Custom Tile Entities
//        MinecraftForge.EVENT_BUS.register(new CustomTileEntitySaveHandler());
//        MinecraftForge.EVENT_BUS.register(new CustomTileEntityLoadHandler());

        // Player Quest Progress
        MinecraftForge.EVENT_BUS.register(new PlayerQuestProgressSaveHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerQuestProgressLoadHandler());
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
     * @param worldName
     * @return Path to VanillaMagic directory in World directory.
     */
    public static Path getVMWorldDir(String worldName) {
        return Paths.get(FMLPaths.GAMEDIR.get().toString(), "saves", worldName, "VanillaMagic");
    }

    /**
     * @param worldName
     * @return Path to file with Custom Tile Entities in specified World directory.
     */
    public static Path getCustomTileEntitiesFilePath(String worldName) {
        return Paths.get(getVMWorldDir(worldName).toString(), "VanillaMagicTileEntities.dat");
    }

    /**
     * Parses Player data for Quests manipulation.
     *
     * @param player
     */
    public static void parsePlayerQuests(PlayerEntity player, Consumer3<String, String, File> consumer) {
        String playerName = player.getName().getFormattedText();
        String worldName = player.world.getServer().getWorldName();
        Path playerQuestsPath = VMFiles.getPlayerQuestsFilePath(worldName, playerName);
        File playerQuestsFile = playerQuestsPath.toFile();

        consumer.accept(worldName, playerName, playerQuestsFile);
    }

    /**
     * @param worldName
     * @param playerName
     * @return Path to JSON file with Player's quests in specified World.
     */
    public static Path getPlayerQuestsFilePath(String worldName, String playerName) {
        return Paths.get(getVMWorldDir(worldName).toString(), "players_quests", playerName + ".json");
    }

    /**
     * @return Path to the source from where the Quests file should be read.
     */
    public static Path getQuestsFileSourcePath() {
        Path modsPath = Paths.get(VMFiles.getGameDir().toString(), "mods");
        File modsFile = modsPath.toFile();

        if (modsFile.exists()) {
            for (String modFilePath : modsFile.list()) {
                if (modFilePath.toLowerCase().startsWith(VanillaMagic.MODID)) {
                    return Paths.get(modFilePath);
                }
            }
        }

        return Paths.get(modsPath.getParent().getParent().toString(), "src", "main", "resources", VMFiles.getQuestsFileName());
    }

    /**
     * Unzips the source file into the destination file.
     *
     * @param destinationFile
     * @param sourceFile
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

    public static void readJson(File file, Consumer<IJsonService> consumer) {
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            FileInputStream fis = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fis);
            JsonParser parser = new JsonParser();
            JsonElement rootElement = parser.parse(reader);

            consumer.accept(new JsonService(rootElement.getAsJsonObject()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeJson(File file, Consumer<JsonWriter> consumer) {
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            JsonWriter writer = new JsonWriter(osw);

            consumer.accept(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
