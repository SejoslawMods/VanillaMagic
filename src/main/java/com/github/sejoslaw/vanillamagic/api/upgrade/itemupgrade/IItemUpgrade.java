package com.github.sejoslaw.vanillamagic.api.upgrade.itemupgrade;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;

/**
 * This is the base of the Upgrade System.
 * You can write Your Events in class that implements IItemUpgrade.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IItemUpgrade {
    public static final String NBT_ITEM_UPGRADE_TAG = "NBT_ITEM_UPGRADE_TAG";
    public static final String NBT_ITEM_CONTAINS_UPGRADE = "NBT_ITEM_CONTAINS_UPGRADE";

    /**
     * @return Returns the "base" stack with all the NBT data written.
     */
    default ItemStack getResult(ItemStack base) {
        ItemStack result = base.copy();
        result.setDisplayName(new StringTextComponent(result.getDisplayName() + "+ Upgrade: " + getUpgradeName()));
        CompoundNBT stackTag = result.getOrCreateTag();
        stackTag.putString(NBT_ITEM_UPGRADE_TAG, getUniqueNBTTag());
        stackTag.putBoolean(NBT_ITEM_CONTAINS_UPGRADE, true);
        return result;
    }

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
    default public boolean containsTag(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }

        CompoundNBT tag = stack.getOrCreateTag();

        if (tag == null) {
            return false;
        }

        return tag.getString(NBT_ITEM_UPGRADE_TAG).equals(getUniqueNBTTag());
    }
}