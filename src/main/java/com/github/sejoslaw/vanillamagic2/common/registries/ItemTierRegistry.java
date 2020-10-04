package com.github.sejoslaw.vanillamagic2.common.registries;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class ItemTierRegistry {
    private static class TierEntry {
        public final int tier;
        public final String tierType;
        public final List<ItemStack> stacks;

        public TierEntry(int tier, String tierType, List<ItemStack> stacks) {
            this.tier = tier;
            this.tierType = tierType;
            this.stacks = stacks;
        }
    }

    private static final Set<TierEntry> ENTRIES = new HashSet<>();
    private static final TierEntry EMPTY = new TierEntry(-1, "EMPTY", new ArrayList<>());

    public static void initialize() {
        add(1, "wooden", Items.AIR);
        add(2, "stone", Blocks.STONE);
        add(3, "iron", Items.IRON_INGOT);
        add(4, "golden", Items.GOLD_INGOT);
        add(5, "diamond", Items.DIAMOND);
        add(6, "netherite", Items.NETHERITE_INGOT);
    }

    public static void add(int tier, String tierType, Block block) {
        add(tier, tierType, Item.BLOCK_TO_ITEM.get(block));
    }

    public static void add(int tier, String tierType, Item item) {
        add(tier, tierType, new ItemStack(item));
    }

    public static void add(int tier, String tierType, ItemStack stack) {
        TierEntry te = find(tier, tierType);

        if (te == null) {
            te = new TierEntry(tier, tierType, new ArrayList<>());
            ENTRIES.add(te);
        }

        te.stacks.add(stack);
    }

    public static boolean isBase(ItemStack item) {
        return ENTRIES
                .stream()
                .anyMatch(entry -> item.getItem().getRegistryName().getPath().toLowerCase().contains(entry.tierType) &&
                                   entry.stacks.stream().noneMatch(stack -> stack.getItem() == item.getItem()));
    }

    public static boolean isIngredient(ItemStack item) {
        return !isBase(item) && getTier(item) != EMPTY.tier;
    }

    public static int getTier(ItemStack item) {
        return ENTRIES
                .stream()
                .filter(entry -> item.getItem().getRegistryName().getPath().startsWith(entry.tierType) ||
                                 entry.stacks.stream().anyMatch(ingredient -> ingredient.getItem() == item.getItem()))
                .findFirst()
                .orElse(EMPTY)
                .tier;
    }

    public static String getTierType(int tier) {
        return ENTRIES
                .stream()
                .filter(entry -> entry.tier == tier).findFirst().orElse(EMPTY).tierType;
    }

    public static Map<Integer, List<ItemStack>> getTiers() {
        return ENTRIES
                .stream()
                .collect(Collectors.toMap(entry -> entry.tier, entry -> entry.stacks));
    }

    public static List<ItemStack> getIngredients(int tier) {
        return ENTRIES
                .stream()
                .filter(entry -> entry.tier == tier)
                .findFirst()
                .orElse(EMPTY)
                .stacks;
    }

    private static TierEntry find(int tier, String tierType) {
        return ENTRIES
                .stream()
                .filter(entry -> entry.tier == tier && entry.tierType.equals(tierType))
                .findFirst()
                .orElse(null);
    }
}
