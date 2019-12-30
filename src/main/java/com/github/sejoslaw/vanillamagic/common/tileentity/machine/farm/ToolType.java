package com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm;

import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public enum ToolType {
    HOE {
        boolean match(ItemStack item) {
            return (item.getItem() instanceof HoeItem);
        }
    },
    AXE {
        boolean match(ItemStack item) {
            if ((item.getItem() instanceof AxeItem) || (item.getItem().getHarvestLevel(item, net.minecraftforge.common.ToolType.AXE, null, null) >= 0)) {
                return true;
            }

            return false;
        }
    },
    SHEARS {
        boolean match(ItemStack item) {
            return item.getItem() instanceof ShearsItem;
        }
    },
    NONE {
        boolean match(ItemStack item) {
            return false;
        }
    };

    public final boolean itemMatches(ItemStack stack) {
        if (ItemStackUtil.isNullStack(stack)) {
            return false;
        }

        return match(stack);
    }

    abstract boolean match(ItemStack item);

    public static boolean isTool(ItemStack stack) {
        for (ToolType type : values()) {
            if (type.itemMatches(stack)) {
                return true;
            }
        }

        return false;
    }

    public static ToolType getToolType(ItemStack stack) {
        for (ToolType type : values()) {
            if (type.itemMatches(stack)) {
                return type;
            }
        }

        return NONE;
    }
}