package com.github.sejoslaw.vanillamagic.core;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMFiles {
    /**
     * @return Path to file with Quests.
     */
    public static Path getQuestsFilePath() {
        return Paths.get(FMLPaths.CONFIGDIR.get().toString(), "VanillaMagicQuests.json");
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
     * @param worldName
     * @param playerName
     * @return Path to JSON file with Player's quests in specified World.
     */
    public static Path getPlayerQuestsFilePath(String worldName, String playerName) {
        return Paths.get(getVMWorldDir(worldName).toString(), "players_quests", playerName + ".json");
    }
}
