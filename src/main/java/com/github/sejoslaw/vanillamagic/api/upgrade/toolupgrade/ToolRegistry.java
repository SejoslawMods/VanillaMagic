package com.github.sejoslaw.vanillamagic.api.upgrade.toolupgrade;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * This registry holds all the possible Tool to Tool upgrades.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ToolRegistry {
    /**
     * Describes a single crafting / tool upgrade.
     */
    static final class ItemEntry implements Comparable<ItemEntry> {
        public final ItemStack toolIn;
        public final ItemStack ingredient;
        public final ItemStack toolOut;

        ItemEntry(ItemStack toolIn, ItemStack ingredient, ItemStack toolOut) {
            this.toolIn = toolIn;
            this.ingredient = ingredient;
            this.toolOut = toolOut;
        }

        public int compareTo(ItemEntry ie) {
            if (ItemStack.areItemsEqualIgnoreDurability(toolIn, ie.toolIn))
                if (ItemStack.areItemsEqualIgnoreDurability(ingredient, ie.ingredient))
                    if (ItemStack.areItemsEqualIgnoreDurability(toolOut, ie.toolOut))
                        return 0;
            return -1;
        }

        public String toString() {
            return "ToolRegistry.Entry[toolIn=" + toolIn + ", ingredient=" + ingredient + ", toolOut=" + toolOut + "]";
        }
    }

    private static final List<ItemEntry> ENTRYS = new ArrayList<>();

    private ToolRegistry() {
    }

    /**
     * Initialize the registry (add vanilla stacks to registry). Used in
     * VanillaMagic preInit.
     */
    public static void preInit() {
        // Axe
        add(Items.WOODEN_AXE, Blocks.STONE, Items.STONE_AXE);
        add(Items.STONE_AXE, Items.IRON_INGOT, Items.IRON_AXE);
        add(Items.IRON_AXE, Items.GOLD_INGOT, Items.GOLDEN_AXE);
        add(Items.GOLDEN_AXE, Items.DIAMOND, Items.DIAMOND_AXE);

        // Pickaxe
        add(Items.WOODEN_PICKAXE, Blocks.STONE, Items.STONE_PICKAXE);
        add(Items.STONE_PICKAXE, Items.IRON_INGOT, Items.IRON_PICKAXE);
        add(Items.IRON_PICKAXE, Items.GOLD_INGOT, Items.GOLDEN_PICKAXE);
        add(Items.GOLDEN_PICKAXE, Items.DIAMOND, Items.DIAMOND_PICKAXE);

        // Sword
        add(Items.WOODEN_SWORD, Blocks.STONE, Items.STONE_SWORD);
        add(Items.STONE_SWORD, Items.IRON_INGOT, Items.IRON_SWORD);
        add(Items.IRON_SWORD, Items.GOLD_INGOT, Items.GOLDEN_SWORD);
        add(Items.GOLDEN_SWORD, Items.DIAMOND, Items.DIAMOND_SWORD);

        // Hoe
        add(Items.WOODEN_HOE, Blocks.STONE, Items.STONE_HOE);
        add(Items.STONE_HOE, Items.IRON_INGOT, Items.IRON_HOE);
        add(Items.IRON_HOE, Items.GOLD_INGOT, Items.GOLDEN_HOE);
        add(Items.GOLDEN_HOE, Items.DIAMOND, Items.DIAMOND_HOE);

        // Shovel
        add(Items.WOODEN_SHOVEL, Blocks.STONE, Items.STONE_SHOVEL);
        add(Items.STONE_SHOVEL, Items.IRON_INGOT, Items.IRON_SHOVEL);
        add(Items.IRON_SHOVEL, Items.GOLD_INGOT, Items.GOLDEN_SHOVEL);
        add(Items.GOLDEN_SHOVEL, Items.DIAMOND, Items.DIAMOND_SHOVEL);
    }

    /**
     * Add new tool upgrade -> Item + Block = Item
     *
     * @param toolIn
     * @param ingredient
     * @param toolOut
     */
    public static void add(Item toolIn, Block ingredient, Item toolOut) {
        add(toolIn, Item.getItemFromBlock(ingredient), toolOut);
    }

    /**
     * Add new tool upgrade -> Item + Item = Item
     *
     * @param toolIn
     * @param ingredient
     * @param toolOut
     */
    public static void add(Item toolIn, Item ingredient, Item toolOut) {
        add(new ItemStack(toolIn), new ItemStack(ingredient), new ItemStack(toolOut));
    }

    /**
     * Add new tool upgrade -> ItemStack + ItemStack = ItemStack
     *
     * @param toolIn
     * @param ingredient
     * @param toolOut
     */
    public static void add(ItemStack toolIn, ItemStack ingredient, ItemStack toolOut) {
        ItemEntry ie = new ItemEntry(toolIn, ingredient, toolOut);

        for (ItemEntry entry : ENTRYS) {
            if (ie.compareTo(entry) == 0) {
                return;
            }
        }

        ENTRYS.add(ie);
    }

    /**
     * @param index
     * @return Returns the basic Tool at given index from list.
     */
    public static ItemStack getBaseTool(int index) {
        return ENTRYS.get(index).toolIn;
    }

    /**
     * @param index
     * @return Returns the ingredient at given index from list.
     */
    public static ItemStack getIngredient(int index) {
        return ENTRYS.get(index).ingredient;
    }

    /**
     * @param index
     * @return Returns the result Tool at given index from list.
     */
    public static ItemStack getResultTool(int index) {
        return ENTRYS.get(index).toolOut;
    }

    /**
     * @return Returns the number of registered Tool Upgrades - size of the list.
     */
    public static int size() {
        return ENTRYS.size();
    }

    /**
     * @param baseTool
     * @return Returns the ingredient connected with given base Tool. NULL if given
     * Tool has no crafting.
     */
    @Nullable
    public static ItemStack getIngredient(ItemStack baseTool) {
        for (ItemEntry ie : ENTRYS) {
            if (ItemStack.areItemsEqualIgnoreDurability(baseTool, ie.toolIn)) {
                return ie.ingredient;
            }
        }

        return null;
    }

    /**
     * @param baseTool
     * @param ingredient
     * @return Returns the registered result based on given base Tool and
     * ingredient. NULL if there is no result for given arguments.
     */
    @Nullable
    public static ItemStack getResult(ItemStack baseTool, ItemStack ingredient) {
        for (ItemEntry ie : ENTRYS) {
            if (ItemStack.areItemsEqualIgnoreDurability(baseTool, ie.toolIn)
                    && ItemStack.areItemsEqualIgnoreDurability(ingredient, ie.ingredient)) {
                return ie.toolOut;
            }
        }

        return null;
    }
}