package com.github.sejoslaw.vanillamagic2.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class CustomItemCrystal implements ICustomItem {
    public Item getBaseItem() {
        return Items.NETHER_STAR;
    }
}
