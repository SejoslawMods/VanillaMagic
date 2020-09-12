package com.github.sejoslaw.vanillamagic2.common.items;

import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class VMItem implements IVMItem {
    protected Item baseItem;

    public Item getBaseItem() {
        return this.baseItem;
    }

    public void setBaseItem(Item item) {
        this.baseItem = item;
    }

    public void addTooltip(ItemStack stack, List<ITextComponent> tooltips) {
        TextUtils.addLine(tooltips, "vmitem.tooltip.registryName", this.getUniqueKey());
    }
}
