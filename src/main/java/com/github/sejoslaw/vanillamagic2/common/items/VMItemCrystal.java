package com.github.sejoslaw.vanillamagic2.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class VMItemCrystal extends VMItem {
    public Item getBaseItem() {
        return Items.NETHER_STAR;
    }
}
