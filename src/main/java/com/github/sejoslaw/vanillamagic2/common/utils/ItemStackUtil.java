package com.github.sejoslaw.vanillamagic2.common.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class ItemStackUtil {
    /**
     * @return ItemStack from JSON Object.
     */
    public static ItemStack getItemStackFromJson(JsonObject jo) {
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
            } else {
                return new ItemStack(item, stackSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @return ItemStacks from JSON Object.
     */
    public static List<ItemStack> getItemStacksFromJson(JsonObject jo, String key) {
        JsonArray ja = jo.get(key).getAsJsonArray();
        List<ItemStack> stacks = new ArrayList<>();

        for (JsonElement je : ja) {
            ItemStack stack = getItemStackFromJson(je.getAsJsonObject());
            stacks.add(stack);
        }

        return stacks;
    }
}
