package com.github.sejoslaw.vanillamagic.common.config;

import com.github.sejoslaw.vanillamagic.api.quest.IQuest;
import com.github.sejoslaw.vanillamagic.core.VMLogger;
import com.github.sejoslaw.vanillamagic.core.VanillaMagic;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMConfigQuests {
    public static final String QUESTS_FILENAME = "VanillaMagicQuests.json";

    public static void initialize() {
        Path currentRuntimePath = FMLPaths.GAMEDIR.get();
        Path destinationQuestsFilePath = Paths.get(currentRuntimePath.toString(), "config", QUESTS_FILENAME);
        File destinationQuestsFile = destinationQuestsFilePath.toFile();
        Path sourceQuestsFilePath = getSourceQuestsFilePath(currentRuntimePath);
        File sourceQuestsFile = sourceQuestsFilePath.toFile();

        unzip(destinationQuestsFile, sourceQuestsFile);
        readQuests(destinationQuestsFile);
    }

    private static Path getSourceQuestsFilePath(Path currentRuntimePath) {
        Path modsPath = Paths.get(currentRuntimePath.toString(), "mods");
        File modsFile = modsPath.toFile();

        if (modsFile.exists()) {
            for (String modFilePath : modsFile.list()) {
                if (modFilePath.toLowerCase().startsWith(VanillaMagic.MODID)) {
                    return Paths.get(modFilePath);
                }
            }
        }

        return Paths.get(modsPath.getParent().toString(), "src", "main", "resurces", QUESTS_FILENAME);
    }

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

                    if (file.getName().equals(QUESTS_FILENAME)) {
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
                throw new FileNotFoundException("Quests file not found or is corrupted. Destination path: " + destinationFile.getAbsolutePath() + "; Source path: " + sourceFile.getAbsolutePath());
            }

            while (is.available() > 0) {
                fos.write(is.read());
            }

            fos.close();
            is.close();
            VMLogger.logInfo(QUESTS_FILENAME + " created.");
        } catch (IOException e) {
            e.printStackTrace();
            VMLogger.logInfo("Error while creating " + QUESTS_FILENAME);
        }
    }

    /**
     * Read all Quests from JSON file
     */
    private static void readQuests(File destinationFile) {
        try {
            FileInputStream fis = new FileInputStream(destinationFile);
            InputStreamReader reader = new InputStreamReader(fis);
            JsonParser parser = new JsonParser();
            JsonElement js = parser.parse(reader);

            if (!js.isJsonArray()) {
                VMLogger.log(Level.WARN, "Can't load VanillaMagic Quests from JSON file.");
                return;
            }

            JsonArray ja = js.getAsJsonArray();

            for (JsonElement je : ja) {
                JsonObject jo = je.getAsJsonObject();
                Class<?> className = Class.forName(jo.get("questClass").getAsString());
                IQuest quest = (IQuest) className.newInstance();
                quest.readData(jo);
            }

            VMLogger.log(Level.WARN, "VanillaMagic Quests read from JSON file.");
        } catch (Exception e) {
            e.printStackTrace();
            VMLogger.logInfo("Error while loading VanillaMagicQuests.json");
        }
    }
}
