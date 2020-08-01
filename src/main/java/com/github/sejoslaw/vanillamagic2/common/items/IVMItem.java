package com.github.sejoslaw.vanillamagic2.common.items;

import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IVMItem {
    /**
     * @return The array of ingredients for crafting this Custom Item.
     */
    default List<ItemStack> getIngredients() {
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(new ItemStack(this.getBaseItem()));
        return stacks;
    }

    /**
     * @return Base item of this Custom Item.
     */
    Item getBaseItem();

    /**
     * @return Custom item in a form of ItemStack.
     */
    default ItemStack getStack() {
        ItemStack stack = new ItemStack(this.getBaseItem());
        stack.setDisplayName(TextUtils.translate("item." + this.getUniqueKey() + ".displayName"));

        CompoundNBT data = stack.getOrCreateTag();
        data.putString(NbtUtils.NBT_VM_ITEM_UNIQUE_NAME, this.getUniqueKey());

        return stack;
    }

    /**
     * @return The key by which this Custom Item can be uniquely identified (also with translations).
     */
    default String getUniqueKey() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    /**
     * @return True if the specified ItemStack is current VM Item.
     */
    default boolean isVMItem(ItemStack stack) {
        return stack.getOrCreateTag().getString(NbtUtils.NBT_VM_ITEM_UNIQUE_NAME).equals(this.getUniqueKey());
    }
}
