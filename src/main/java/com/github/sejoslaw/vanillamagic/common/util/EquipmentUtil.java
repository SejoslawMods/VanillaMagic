package com.github.sejoslaw.vanillamagic.common.util;

import com.github.sejoslaw.vanillamagic.core.VMLogger;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which store various methods connected with Equipment and Inventory.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class EquipmentUtil {
    public static final List<Item> HELMETS = new ArrayList<>();
    public static final List<Item> CHEST_PLATES = new ArrayList<>();
    public static final List<Item> LEGGINGS = new ArrayList<>();
    public static final List<Item> BOOTS = new ArrayList<>();
    public static final List<Item> WEAPONS = new ArrayList<>();

    private EquipmentUtil() {
    }

    static {
        Iterable<Item> items = ForgeRegistries.ITEMS.getValues();

        fillList(items, HELMETS, new String[]{"helmet", "hat"});
        fillList(items, CHEST_PLATES, new String[]{"chestplate"});
        fillList(items, LEGGINGS, new String[]{"leggings"});
        fillList(items, BOOTS, new String[]{"boots"});
        fillList(items, WEAPONS, new String[]{"sword", "bow"});
    }

    private static void fillList(Iterable<Item> items, List<Item> registry, String[] names) {
        for (Item item : items) {
            for (String name : names) {
                if (item.getRegistryName().toString().contains(name)) {
                    registry.add(item);
                    break;
                }
            }
        }

        VMLogger.logInfo("Readded [" + names[0] + "]: " + registry.size());
    }
}