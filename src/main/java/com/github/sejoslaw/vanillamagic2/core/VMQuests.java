package com.github.sejoslaw.vanillamagic2.core;

import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class VMQuests {
    public static void initialize() {
        VMFiles.readJsonArray(VMFiles.getQuestsFilePath().toFile(), QuestRegistry::readQuest);
    }
}
