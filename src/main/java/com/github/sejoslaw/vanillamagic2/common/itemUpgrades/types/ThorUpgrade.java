package com.github.sejoslaw.vanillamagic2.common.itemUpgrades.types;

import com.github.sejoslaw.vanillamagic2.common.itemUpgrades.BaseItemType;
import com.github.sejoslaw.vanillamagic2.common.itemUpgrades.ItemUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ThorUpgrade extends ItemUpgrade {
    public BaseItemType[] getBaseItemTypes() {
        return new BaseItemType[] { BaseItemType.SWORD };
    }

    public ItemStack getIngredient() {
        return new ItemStack(Items.CREEPER_HEAD);
    }

    public String getUniqueTag() {
        return "NBT_UPGRADE_THOR";
    }
}
