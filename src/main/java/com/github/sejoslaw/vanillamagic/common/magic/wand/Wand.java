package com.github.sejoslaw.vanillamagic.common.magic.wand;

import net.minecraft.item.ItemStack;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;

/**
 * Basic Wand implementation.
 */
public class Wand implements IWand {
    /**
     * Wand unique ID.
     */
    private final int wandID;
    /**
     * Wand ItemStack - each Wand should have different WandStack.
     */
    private final ItemStack wandStack;
    /**
     * Name of the Wand.
     */
    private final String wandName;

    public Wand(int wandID, ItemStack wandStack, String wandName) {
        this.wandID = wandID;
        this.wandStack = wandStack;
        this.wandName = wandName;

        WandRegistry.addWand(this);
    }

    public int getWandID() {
        return wandID;
    }

    public ItemStack getWandStack() {
        return wandStack;
    }

    public String getWandName() {
        return wandName;
    }

    public boolean canWandDoWork(int wandID) {
        if (this.wandID == wandID) {
            return true;
        }

        return false;
    }
}