package com.github.sejoslaw.vanillamagic2.common.items;

import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class VMItem implements IVMItem {
    public void addTooltip(ItemStack stack, List<ITextComponent> tooltips) {
        TextUtils.addLine(tooltips, "vmitem.tooltip.registryName", this.getUniqueKey());
    }
}
