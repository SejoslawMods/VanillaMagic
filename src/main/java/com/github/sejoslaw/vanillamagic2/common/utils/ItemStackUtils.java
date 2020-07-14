package com.github.sejoslaw.vanillamagic2.common.utils;

import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import com.github.sejoslaw.vanillamagic2.common.json.JsonItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class ItemStackUtils {
    /**
     * @return ItemStack from JSON Object.
     */
    public static ItemStack getItemStackFromJson(JsonItemStack jsonItemStack) {
        try {
            int id = jsonItemStack.getId();
            int stackSize = jsonItemStack.getStackSize();

            if (stackSize < 0) {
                stackSize = 1;
            }

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
    public static List<ItemStack> getItemStacksFromJson(IJsonService rootService, String key) {
        List<ItemStack> stacks = new ArrayList<>();

        for (IJsonService service : rootService.getList(key)) {
            ItemStack stack = getItemStackFromJson(service.getItemStack(key));
            stacks.add(stack);
        }

        return stacks;
    }
}
