package com.github.sejoslaw.vanillamagic.common.config;

import com.github.sejoslaw.vanillamagic.api.quest.IQuest;
import com.github.sejoslaw.vanillamagic.core.VMLogger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class which operates on additional VM configuration files.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMConfigAchievements {
    public static final String VM_DIRECTORY = "/VanillaMagic/";

    private final String quests = "Quests.json";
    private final String aboutQuests = "About Quests.txt";

    private File modConfigurationDirectory;
    private File modFile;
    private File fileQuests;
    private File fileAboutQuests;

    /**
     * modConfigurationDirectory - config/VanillaMagic/ <br>
     * modFile - vanillamagic-[].jar OR /bin/ (if modding)
     */
    public VMConfigAchievements(File modConfigurationDirectory, File modFile) {
        this.modConfigurationDirectory = modConfigurationDirectory;
        this.modFile = modFile;

        if (!this.modConfigurationDirectory.exists()) {
            this.modConfigurationDirectory.mkdirs();
            VMLogger.logInfo("VanillaMagic config directory created.");
        }

        VMLogger.logInfo("VanillaMagic config directory loaded.");
        this.fileQuests = unzip(this.quests, fileQuests);
        this.fileAboutQuests = unzip(this.aboutQuests, fileAboutQuests);

        readQuests();
    }

    /**
     * Unzip the specified file from JAR - copy into directory from inside of JAR
     * file.
     */
    @SuppressWarnings("resource")
    public File unzip(String fileName, File localFile) {
        localFile = new File(this.modConfigurationDirectory, fileName);

        if (localFile.exists()) {
            return localFile;
        }

        try {
            localFile.createNewFile();

            if (!this.modFile.isDirectory()) {
                JarFile jarFile = new JarFile(this.modFile);
                Enumeration<JarEntry> enumeration = jarFile.entries();

                while (enumeration.hasMoreElements()) {
                    JarEntry file = (JarEntry) enumeration.nextElement();

                    if (file.getName().equals(fileName)) {
                        InputStream is = jarFile.getInputStream(file);
                        FileOutputStream fos = new FileOutputStream(localFile);

                        while (is.available() > 0) {
                            fos.write(is.read());
                        }

                        fos.close();
                        is.close();
                        VMLogger.logInfo(fileName + " created.");

                        return localFile;
                    }
                }
            } else if (this.modFile.isDirectory()) {
                File[] files = this.modFile.listFiles();

                for (File file : files) {
                    if (file.getName().contains(fileName)) {
                        FileInputStream is = new FileInputStream(file);
                        FileOutputStream fos = new FileOutputStream(localFile);

                        while (is.available() > 0) {
                            fos.write(is.read());
                        }

                        fos.close();
                        is.close();
                        VMLogger.logInfo(fileName + " created.");

                        return localFile;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            VMLogger.logInfo("Error while creating " + fileName);
        }

        return localFile;
    }

    /**
     * Read all Quests from JSON file
     */
    public void readQuests() {
        try {
            FileInputStream fis = new FileInputStream(this.fileQuests);
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
            VMLogger.logInfo("Error while loading Quests.json");
        }
    }
}