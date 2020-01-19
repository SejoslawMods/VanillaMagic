package com.github.sejoslaw.vanillamagic.common.item.accelerationcrystal;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.item.CustomItemCrystal;
import com.github.sejoslaw.vanillamagic.common.item.CustomItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemAccelerationCrystal extends CustomItemCrystal {
    public void registerRecipe() {
        CustomItemRegistry.addRecipe(this, new ItemStack(Items.BOOK, 4), new ItemStack(Items.NETHER_STAR, 1));
    }

    public ITextComponent getItemName() {
        return TextUtil.wrap("Acceleration Crystal");
    }
}