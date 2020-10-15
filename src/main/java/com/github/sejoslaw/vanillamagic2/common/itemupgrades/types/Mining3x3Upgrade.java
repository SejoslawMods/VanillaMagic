package com.github.sejoslaw.vanillamagic2.common.itemupgrades.types;

import com.github.sejoslaw.vanillamagic2.common.itemupgrades.BaseItemType;
import com.github.sejoslaw.vanillamagic2.common.itemupgrades.ItemUpgrade;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class Mining3x3Upgrade extends ItemUpgrade {
    public BaseItemType[] getBaseItemTypes() {
        return new BaseItemType[] { BaseItemType.PICKAXE };
    }

    public ItemStack getIngredient() {
        return new ItemStack(Blocks.CRAFTING_TABLE);
    }

    public String getUniqueTag() {
        return "NBT_UPGRADE_MINING_3X3";
    }
}
