package com.github.sejoslaw.vanillamagic2.common.itemupgrades.types;

import com.github.sejoslaw.vanillamagic2.common.itemupgrades.BaseItemType;
import com.github.sejoslaw.vanillamagic2.common.itemupgrades.ItemUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class AutosmeltUpgrade extends ItemUpgrade {
    public BaseItemType[] getBaseItemTypes() {
        return new BaseItemType[] { BaseItemType.PICKAXE };
    }

    public ItemStack getIngredient() {
        return new ItemStack(Items.MAGMA_CREAM);
    }

    public String getUniqueTag() {
        return "NBT_UPGRADE_AUTOSMELT";
    }
}
