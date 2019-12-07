package com.github.sejoslaw.vanillamagic.api.item;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * Interface implemented to any class that should be read as Enchanted Bucket.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IEnchantedBucket extends ICustomItem {
    public static final String NBT_ENCHANTED_BUCKET = "NBT_ENCHANTED_BUCKET";
    public static final String NBT_FLUID_NAME = "NBT_FLUID_NAME";

    /**
     * @return Returns fluid which this bucket contains.
     */
    Fluid getFluidInBucket();

    default public ItemStack getItem() {
        Fluid fluidInBucket = this.getFluidInBucket();

        ItemStack stack = getBucket().copy();
        stack.setDisplayName(new StringTextComponent("Enchanted Bucket: " + fluidInBucket.getRegistryName().toString()));

        CompoundNBT stackTag = stack.getOrCreateTag();
        stackTag.putString(NBT_UNIQUE_NAME, getUniqueNBTName());
        stackTag.putString(NBT_ENCHANTED_BUCKET, getUniqueNBTName()); // to let Quest know that we want EnchantedBucket
        stackTag.putString(NBT_FLUID_NAME, fluidInBucket.getRegistryName().toString());

        return stack;
    }

    /**
     * @return Returns crafting ingredient bucket with fluid.
     */
    @Nullable
    default public ItemStack getBucket() {
        try {
            Class<?> clazz = Class.forName("com.github.sejoslaw.vanillamagic.item.enchantedbucket.EnchantedBucketUtil");
            Method method = clazz.getMethod("getResult", Fluid.class);
            return (ItemStack) method.invoke(null, getFluidInBucket());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    default void registerRecipe() {
    }
}