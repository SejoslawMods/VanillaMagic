package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.itemupgrades.BaseItemType;
import com.github.sejoslaw.vanillamagic2.common.itemupgrades.ItemUpgrade;
import com.github.sejoslaw.vanillamagic2.common.itemupgrades.ItemUpgradeProcessor;
import com.github.sejoslaw.vanillamagic2.common.itemupgrades.eventcallers.*;
import com.github.sejoslaw.vanillamagic2.common.itemupgrades.types.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class ItemUpgradeRegistry {
    private static final Set<ItemUpgradeProcessor> UPGRADES = new HashSet<>();

    public static void initialize() {
        UPGRADES.add(new ItemUpgradeProcessor(ItemUpgradeEventCallerAutosmelt.class, AutosmeltUpgrade.class).register());
        UPGRADES.add(new ItemUpgradeProcessor(ItemUpgradeEventCallerLifesteal.class, LifestealUpgrade.class).register());
        UPGRADES.add(new ItemUpgradeProcessor(ItemUpgradeEventCallerThor.class, ThorUpgrade.class).register());
        UPGRADES.add(new ItemUpgradeProcessor(ItemUpgradeEventCallerWither.class, WitherUpgrade.class).register());
        UPGRADES.add(new ItemUpgradeProcessor(ItemUpgradeEventCallerMining3x3.class, Mining3x3Upgrade.class).register());
    }

    public static boolean isBase(ItemStack stack) {
        return BaseItemType.TYPES.stream().anyMatch(type -> stack.getItem().getRegistryName().toString().toLowerCase().contains(type.itemType.toLowerCase()));
    }

    public static boolean isIngredient(ItemStack stack) {
        return UPGRADES.stream().anyMatch(proc -> proc.getItemUpgradeEventCaller().itemUpgrade.isValidIngredient(stack));
    }

    public static BaseItemType getBaseItemType(ItemStack stack) {
        return BaseItemType.TYPES
                .stream()
                .filter(type -> stack.getItem().getRegistryName().toString().toLowerCase().contains(type.itemType.toLowerCase()))
                .findFirst()
                .orElse(new BaseItemType(stack.getTranslationKey()));
    }

    public static List<ItemUpgrade> getUpgrades(BaseItemType type) {
        return UPGRADES
                .stream()
                .filter(proc ->
                        Arrays.stream(proc.getItemUpgradeEventCaller().itemUpgrade.getBaseItemTypes())
                        .anyMatch(procType -> procType.itemType.equals(type.itemType)))
                .map(proc -> proc.getItemUpgradeEventCaller().itemUpgrade)
                .collect(Collectors.toList());
    }

    public static void forEach(Consumer<ItemUpgradeProcessor> consumer) {
        UPGRADES.forEach(consumer);
    }

    public static List<ItemUpgrade> getInstalledUpgrades(ItemStack stack) {
        return UPGRADES
                .stream()
                .filter(proc -> {
                    CompoundNBT nbt = stack.getTag();
                    return nbt != null && nbt.keySet().contains(proc.getItemUpgradeEventCaller().itemUpgrade.getUniqueTag());
                })
                .map(proc -> proc.getItemUpgradeEventCaller().itemUpgrade)
                .collect(Collectors.toList());
    }
}
