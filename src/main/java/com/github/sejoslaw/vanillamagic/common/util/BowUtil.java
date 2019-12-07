package com.github.sejoslaw.vanillamagic.common.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import javax.annotation.Nullable;

/**
 * Class which store various methods connected with Bow.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class BowUtil {
    private BowUtil() {
    }

    public static boolean isArrow(@Nullable ItemStack stack) {
        return !ItemStackUtil.isNullStack(stack) && stack.getItem() instanceof ArrowItem;
    }

    @Nullable
    public static ItemStack findAmmo(PlayerEntity player) {
        if (isArrow(player.getHeldItem(Hand.OFF_HAND))) {
            return player.getHeldItem(Hand.OFF_HAND);
        } else if (isArrow(player.getHeldItem(Hand.MAIN_HAND))) {
            return player.getHeldItem(Hand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (isArrow(itemstack)) {
                    return itemstack;
                }
            }

            return ItemStackUtil.NULL_STACK;
        }
    }
}