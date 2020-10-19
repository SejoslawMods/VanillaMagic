package com.github.sejoslaw.vanillamagic2.common.items;

import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IVMItem {
    /**
     * @return Base item for this VM Item.
     */
    Item getBaseItem();

    /**
     * Sets base item.
     */
    void setBaseItem(Item item);

    /**
     * @return Custom item in a form of ItemStack.
     */
    default ItemStack getStack() {
        ItemStack stack = new ItemStack(this.getBaseItem());
        stack.setDisplayName(TextUtils.translate("vmitem." + this.getUniqueKey() + ".displayName"));
        stack.getOrCreateTag().putString(NbtUtils.NBT_VM_ITEM_UNIQUE_NAME, this.getUniqueKey());
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
        if (stack.getTag() == null) {
            return false;
        }

        return stack.getTag().getString(NbtUtils.NBT_VM_ITEM_UNIQUE_NAME).equals(this.getUniqueKey());
    }

    /**
     * Adds appropriate tooltips to the list.
     */
    default void addTooltip(ItemStack stack, List<ITextComponent> tooltips) {
    }
}
