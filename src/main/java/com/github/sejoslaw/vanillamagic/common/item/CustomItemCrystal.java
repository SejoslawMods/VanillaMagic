package com.github.sejoslaw.vanillamagic.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

/**
 * Class which represents basic Nether Star based Custom Item.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class CustomItemCrystal extends CustomItem {
    public Item getBaseItem() {
        return Items.NETHER_STAR;
    }
}