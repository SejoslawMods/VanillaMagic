package com.github.sejoslaw.vanillamagic2.common.itemupgrades.types;

import com.github.sejoslaw.vanillamagic2.common.itemupgrades.BaseItemType;
import com.github.sejoslaw.vanillamagic2.common.itemupgrades.ItemUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class WitherUpgrade extends ItemUpgrade {
    public BaseItemType[] getBaseItemTypes() {
        return new BaseItemType[] { BaseItemType.SWORD };
    }

    public ItemStack getIngredient() {
        return new ItemStack(Items.WITHER_SKELETON_SKULL);
    }

    public String getUniqueTag() {
        return "NBT_UPGRADE_WITHER_EFFECT";
    }
}
