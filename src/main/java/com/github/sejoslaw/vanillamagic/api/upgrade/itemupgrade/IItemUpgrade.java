package com.github.sejoslaw.vanillamagic.api.upgrade.itemupgrade;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * This is the base of the Upgrade System.
 * You can write Your Events in class that implements IItemUpgrade.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IItemUpgrade {
    /**
     * @return Returns ingredient which must be in Cauldron with the required Item.
     */
    ItemStack getIngredient();

    /**
     * This tag MUST BE UNIQUE !!! <br>
     * It is used to recognize the {@link IItemUpgrade}.
     *
     * @return Returns Upgrade unique tag.
     */
    String getUniqueNBTTag();

    /**
     * @return Returns readable name for this upgrade. <br>
     * It will be used as a part of new stack name. <br>
     * For instance: "My Upgrade" or "Awesome Hyper World-Destroying
     * Upgrade".
     */
    String getUpgradeName();

    /**
     * @return Returns if the given stack is an upgrade.
     */
    default boolean containsTag(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }

        CompoundNBT tag = stack.getOrCreateTag();

        return tag.contains(getUniqueNBTTag());
    }
}