package com.github.sejoslaw.vanillamagic2.common.quests.types;

import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.utils.AltarUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.Collection;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestBuildAltar extends Quest {
    public void readData(IJsonService jsonService) {
        super.readData(jsonService);

        ItemStack altarBlockStack = ItemStackUtils.getItemStackFromJson(jsonService.getItemStack("altarBlock"));
        Block altarBlock = Block.getBlockFromItem(altarBlockStack.getItem());
        AltarUtils.BLOCKS.put(this.altarTier, altarBlock);
    }

    public void fillTooltip(Collection<String> lines) {
        super.fillTooltip(lines);

        this.addLine(lines, "quest.tooltip.altarBlock", this.getTooltip(AltarUtils.BLOCKS.get(this.altarTier)));
    }
}
