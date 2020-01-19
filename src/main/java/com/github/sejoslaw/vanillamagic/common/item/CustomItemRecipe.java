package com.github.sejoslaw.vanillamagic.common.item;

import com.github.sejoslaw.vanillamagic.api.item.ICustomItem;
import com.github.sejoslaw.vanillamagic.common.util.CauldronUtil;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class CustomItemRecipe {
    public final ICustomItem output;
    public final ItemStack[] ingredients;

    public CustomItemRecipe(ICustomItem output, ItemStack[] ingredients) {
        this.output = output;
        this.ingredients = ingredients;
    }

    public boolean isValid(World world, BlockPos pos) {
        boolean[] checkedItems = new boolean[this.ingredients.length];
        List<ItemEntity> entitiesInCauldron = CauldronUtil.getItemsInCauldron(world, pos);

        for (ItemEntity itemEntity : entitiesInCauldron) {
            ItemStack stackInCauldron = itemEntity.getItem();

            for (int j = 0; j < this.ingredients.length; ++j) {
                ItemStack ingredientStack = this.ingredients[j];

                if (stackInCauldron.getItem() == ingredientStack.getItem() && stackInCauldron.getCount() == ingredientStack.getCount()) {
                    checkedItems[j] = true;
                }
            }
        }

        for (boolean checkedItem : checkedItems) {
            if (!checkedItem) {
                return false;
            }
        }

        return true;
    }
}
