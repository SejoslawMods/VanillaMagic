package com.github.sejoslaw.vanillamagic2.common.quests.types;

import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import net.minecraft.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestMineBlock extends Quest {
    public List<Block> blocksToMine;

    public void readData(IJsonService jsonService) {
        super.readData(jsonService);

        String blockName = jsonService.getString("blocksToMine").toLowerCase();

        this.blocksToMine = ForgeRegistries.BLOCKS
                .getEntries()
                .stream()
                .filter(entry -> entry.getKey().toString().toLowerCase().contains(blockName))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
