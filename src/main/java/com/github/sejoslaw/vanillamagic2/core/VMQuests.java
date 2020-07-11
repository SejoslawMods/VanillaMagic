package com.github.sejoslaw.vanillamagic2.core;

import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.nio.file.Path;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class VMQuests {
    public static void initialize() {
        Path questsConfigPath = VMFiles.getQuestsFilePath();
        File questsConfigFile = questsConfigPath.toFile();

        VMFiles.readJson(questsConfigFile, rootElement -> {
            if (!rootElement.isJsonArray()) {
                VMLogger.log(Level.WARN, "Can't load VanillaMagic Quests from JSON file.");
                return;
            }

            rootElement.getAsJsonArray().forEach(je -> QuestRegistry.parse(je.getAsJsonObject()));

            VMLogger.log(Level.WARN, "VanillaMagic Quests read from JSON file.");
        });
    }
}
