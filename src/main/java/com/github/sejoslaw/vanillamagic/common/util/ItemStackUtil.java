package com.github.sejoslaw.vanillamagic.common.util;

import com.github.sejoslaw.vanillamagic.core.VMLogger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * Class which store various methods connected with ItemStack.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class ItemStackUtil {
    /**
     * new ItemStack((Item)null);
     */
    public static final ItemStack NULL_STACK = ItemStack.EMPTY;

    private ItemStackUtil() {
    }

    /**
     * @return Returns TRUE if Player holds right items in hands.
     */
    public static boolean checkItemsInHands(PlayerEntity player, ItemStack shouldHaveInOffHand, ItemStack shouldHaveInMainHand) {
        if (player == null) {
            return false;
        }

        ItemStack offHand = player.getHeldItemOffhand();

        if ((shouldHaveInOffHand != null) && (isNullStack(offHand)
                || ItemStackUtil.getStackSize(offHand) != ItemStackUtil.getStackSize(shouldHaveInOffHand)
                || offHand.getItem() != shouldHaveInOffHand.getItem()
                || offHand.getTag() != shouldHaveInOffHand.getTag())) {
            return false;
        }

        ItemStack mainHand = player.getHeldItemMainhand();

        if ((shouldHaveInMainHand != null) && (isNullStack(mainHand)
                || ItemStackUtil.getStackSize(mainHand) != ItemStackUtil.getStackSize(shouldHaveInMainHand)
                || mainHand.getItem() != shouldHaveInMainHand.getItem()
                || mainHand.getTag() != shouldHaveInMainHand.getTag())) {
            return false;
        }

        return true;
    }

    /**
     * @return Returns ItemStack from JSON Object.
     */
    @Nullable
    public static ItemStack getItemStackFromJSON(JsonObject jo) {
        try {
            int id = jo.get("id").getAsInt();
            int stackSize = (jo.get("stackSize") != null ? jo.get("stackSize").getAsInt() : 1);

            Item item = null;
            BlockState blockState = null;

            try {
                item = Item.getItemById(id);
            } catch (Exception e) {
                blockState = Block.getStateById(id);
            }

            if (item == null) {
                return new ItemStack(blockState.getBlock(), stackSize);
            } else if (blockState == null) {
                return new ItemStack(item, stackSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @return Returns readded ItemStack array from JSON Object.
     */
    public static ItemStack[] getItemStackArrayFromJSON(JsonObject jo, String key) {
        JsonArray ja = jo.get(key).getAsJsonArray();
        ItemStack[] tab = new ItemStack[ja.size()];

        for (int i = 0; i < tab.length; ++i) {
            JsonElement je = ja.get(i);
            ItemStack stack = getItemStackFromJSON(je.getAsJsonObject());
            tab[i] = stack;
        }

        return tab;
    }

    /**
     * @return Returns TRUE if given stack is an inventory.
     */
    public static boolean isIInventory(ItemStack stack) {
        if (ItemStackUtil.isNullStack(stack)) {
            return false;
        }

        Item itemFromStack = stack.getItem();
        Block blockFromStack = Block.getBlockFromItem(itemFromStack);

        if (blockFromStack == Blocks.AIR) {
            return false;
        }

        BlockState blockFromStackState = blockFromStack.getDefaultState();
        TileEntity tileFromStack = blockFromStack.createTileEntity(blockFromStackState, null);

        return ((tileFromStack instanceof IInventory) || (tileFromStack instanceof IItemHandler));
    }

    // ========== StackSize Operations ==========

    /**
     * @return Will return empty stack if the
     * {@link ItemStack} should be understand as Empty.
     */
    public static ItemStack loadItemStackFromNBT(CompoundNBT tag) {
        ItemStack stack = ItemStack.read(tag);

        if (stack.isEmpty()) {
            return NULL_STACK;
        }

        return stack;
    }

    /**
     * @return Returns the old ItemStack.stackSize
     */
    public static int getStackSize(ItemStack stack) {
        if (stack == null) {
            return 0;
        }

        return stack.getCount();
    }

    /**
     * This method will sets the ItemStack.stackSize to the given value.<br>
     *
     * @return Returns the given stack.
     */
    public static void setStackSize(ItemStack stack, int value) {
        if (stack == null) {
            return;
        }

        stack.setCount(value);
    }

    /**
     * This method will increase the ItemStack.stackSize of the given stack.<br>
     *
     * @return Returns the given stack.
     */
    public static void increaseStackSize(ItemStack stack, int value) {
        if (stack == null) {
            return;
        }

        stack.grow(value);
    }

    /**
     * This method will decrease the ItemStack.stackSize of the given stack.<br>
     *
     * @return Returns the given stack.
     */
    public static void decreaseStackSize(ItemStack stack, int value) {
        if (stack == null) {
            return;
        }

        stack.shrink(value);
    }

    /**
     * @return Returns true if the given ItemStack is {@link #NULL_STACK}
     */
    public static boolean isNullStack(ItemStack stack) {
        if (stack == null || stack == NULL_STACK) {
            return true;
        }

        return stack.isEmpty();
    }

    /**
     * Prints stack data.
     */
    public static void printStack(ItemStack stack) {
        VMLogger.logInfo("Printing ItemStack data...");

        VMLogger.logInfo("Item: " + stack.getItem());
        VMLogger.logInfo("StackSize: " + getStackSize(stack));
        VMLogger.logInfo("Meta: " + stack.toString());
    }
}