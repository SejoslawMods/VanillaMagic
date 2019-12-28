package com.github.sejoslaw.vanillamagic.common.item;

import com.github.sejoslaw.vanillamagic.api.item.ICustomItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;

/**
 * Basic ICustomItem implementation.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class CustomItem implements ICustomItem {
    public ItemStack getItem() {
        ItemStack stack = new ItemStack(this.getBaseItem());
        stack.setDisplayName(this.getItemName());

        CompoundNBT stackTag = stack.getOrCreateTag();
        stackTag.putString(NBT_UNIQUE_NAME, getUniqueNBTName());

        return stack;
    }

    /**
     * @return Returns the name on this item to be displayed.
     */
    public abstract ITextComponent getItemName();

    /**
     * @return Returns item connected with this CustomItem.
     */
    public abstract Item getBaseItem();
}