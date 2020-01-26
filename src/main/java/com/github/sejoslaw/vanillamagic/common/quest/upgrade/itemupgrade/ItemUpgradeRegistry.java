package com.github.sejoslaw.vanillamagic.common.quest.upgrade.itemupgrade;

import com.github.sejoslaw.vanillamagic.api.upgrade.itemupgrade.IItemUpgrade;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.quest.upgrade.itemupgrade.upgrades.UpgradeAutosmelt;
import com.github.sejoslaw.vanillamagic.common.quest.upgrade.itemupgrade.upgrades.UpgradeLifesteal;
import com.github.sejoslaw.vanillamagic.common.quest.upgrade.itemupgrade.upgrades.UpgradeThor;
import com.github.sejoslaw.vanillamagic.common.quest.upgrade.itemupgrade.upgrades.UpgradeWitherEffect;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.HashSet;
import java.util.Set;

/**
 * This is the base registry which will hold ALL the upgrades for dif ferent
 * items.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class ItemUpgradeRegistry {
    public static final Set<ItemUpgradeRegistryEntry> ENTRIES = new HashSet<>();

    public static void preInit() {
        addUpgrades("pickaxe", new UpgradeAutosmelt());
        addUpgrades("sword", new UpgradeWitherEffect(), new UpgradeLifesteal(), new UpgradeThor());
    }

    public static void addUpgrades(String toolName, IItemUpgrade... upgrades) {
        if (ENTRIES.stream().noneMatch(entry -> entry.toolName.equals(toolName))) {
            addMapping(toolName);
        }

        ItemUpgradeRegistryEntry entry = ENTRIES.stream().filter(e -> e.toolName.equals(toolName)).findAny().get();
        entry.upgrades.addAll(Lists.newArrayList(upgrades));
    }

    private static void addMapping(String toolName) {
        ENTRIES.add(new ItemUpgradeRegistryEntry(toolName));
    }

    public static boolean canGetUpgrade(ItemStack base) {
        return ENTRIES
                .stream()
                .filter(entry -> base.getItem().getRegistryName().getPath().contains(entry.toolName))
                .findAny()
                .orElse(null) != null;
    }

    public static ItemStack getResult(ItemStack base, ItemStack ingredient) {
        for (ItemUpgradeRegistryEntry entry : ENTRIES) {
            if (canGetUpgrade(base)) {
                continue;
            }

            IItemUpgrade upgrade = entry.getUpgrade(ingredient);

            if (upgrade != null) {
                return getResult(base, upgrade);
            }
        }

        return base;
    }

    private static ItemStack getResult(ItemStack base, IItemUpgrade upgrade) {
        String upgradeTag = upgrade.getUniqueNBTTag();
        CompoundNBT baseNbt = base.getOrCreateTag();
        baseNbt.putString(upgradeTag, upgradeTag);
        base.setDisplayName(TextUtil.wrap("[" + upgrade.getUpgradeName() + "] " + base.getDisplayName().getFormattedText()));
        return base;
    }
}