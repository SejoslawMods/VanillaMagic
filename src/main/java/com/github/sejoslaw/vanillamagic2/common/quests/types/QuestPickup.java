package com.github.sejoslaw.vanillamagic2.common.quests.types;

import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
import net.minecraft.item.ItemStack;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestPickup extends Quest {
    public ItemStack whatToPickStack;

    public void readData(IJsonService jsonService) {
        super.readData(jsonService);

        this.whatToPickStack = ItemStackUtils.getItemStackFromJson(jsonService.getItemStack("whatToPickStack"));
    }
}
