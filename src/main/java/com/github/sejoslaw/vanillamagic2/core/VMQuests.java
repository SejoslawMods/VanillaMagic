package com.github.sejoslaw.vanillamagic2.core;

import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;
import org.apache.logging.log4j.Level;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class VMQuests {
    public static void initialize() {
        VMFiles.readJson(VMFiles.getQuestsFilePath().toFile(), service -> {
            service.getList("").forEach(QuestRegistry::readQuest);
            VMLogger.log(Level.WARN, "VanillaMagic Quests read from JSON file.");
        });
    }
}
