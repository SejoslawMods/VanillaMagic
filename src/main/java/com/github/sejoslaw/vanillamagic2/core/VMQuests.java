package com.github.sejoslaw.vanillamagic2.core;

import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;
import org.apache.logging.log4j.Level;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class VMQuests {
    public static void initialize() {
        VMFiles.readJson(VMFiles.getQuestsFilePath().toFile(), rootElement -> {
            if (!rootElement.isJsonArray()) {
                VMLogger.log(Level.WARN, "Can't load VanillaMagic Quests from JSON file.");
                return;
            }

            rootElement.getAsJsonArray().forEach(je -> QuestRegistry.parse(je.getAsJsonObject()));

            VMLogger.log(Level.WARN, "VanillaMagic Quests read from JSON file.");
        });
    }
}
