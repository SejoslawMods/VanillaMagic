package com.github.sejoslaw.vanillamagic2.common.utils;

import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import com.github.sejoslaw.vanillamagic2.common.json.JsonItemStack;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class ItemStackUtils {
    /**
     * @return ItemStack from JSON Object.
     */
    public static ItemStack getItemStackFromJson(JsonItemStack jsonItemStack) {
        try {
            CompoundNBT nbt = new CompoundNBT();

            nbt.putString("id", jsonItemStack.getId());

            byte count = jsonItemStack.getCount();
            nbt.putByte("Count", count <= 0 ? 1 : count);

            int meta = jsonItemStack.getMeta();
            nbt.putInt("Damage", Math.max(meta, 0));

            return ItemStack.read(nbt);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * @return ItemStacks from JSON Object.
     */
    public static List<ItemStack> getItemStacksFromJson(IJsonService rootService, String key) {
        List<ItemStack> stacks = new ArrayList<>();

        for (IJsonService service : rootService.getList(key)) {
            ItemStack stack = getItemStackFromJson(service.toItemStack());
            stacks.add(stack);
        }

        return stacks;
    }

    /**
     * @return Number of burning ticks.
     */
    public static int getBurnTicks(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack);
    }

    /**
     * @return ItemStacks with smelting result based on given input.
     */
    public static List<ItemStack> smeltItems(PlayerEntity player, List<ItemEntity> stacksToSmelt, int smeltingCost) {
        ItemStack leftHandStack = player.getHeldItemOffhand();
        IWorld world = player.world;
        final int[] ticks = {0};

        return stacksToSmelt
                .stream()
                .map(entity -> {
                    ItemStack stack = entity.getItem();
                    int stackSize = stack.getCount();
                    int ticksToSmeltStack = stackSize * smeltingCost;

                    while (leftHandStack.getCount() > 0 && ticks[0] < ticksToSmeltStack) {
                        ticks[0] += getBurnTicks(leftHandStack);
                        leftHandStack.grow(-1);
                    }

                    ItemStack smeltingResult = getSmeltingResultAsNewStack(stack, world);

                    if (ticks[0] >= ticksToSmeltStack) {
                        smeltingResult.setCount(stack.getCount());
                        entity.remove();
                    } else if (ticks[0] >= smeltingCost) {
                        int howManyCanSmelt = ticks[0] / smeltingCost;
                        stack.grow(-howManyCanSmelt);
                        smeltingResult.setCount(howManyCanSmelt);
                    } else {
                        return ItemStack.EMPTY;
                    }

                    ticks[0] -= ticksToSmeltStack;
                    player.experience += getExperienceFromStack(stack, world);
                    entity.remove();

                    return smeltingResult;
                })
                .filter(stack -> stack != ItemStack.EMPTY)
                .collect(Collectors.toList());
    }

    /**
     * @return Experience value from the given ItemStack.
     */
    public static float getExperienceFromStack(ItemStack stack, IWorld world) {
        AbstractCookingRecipe cookingRecipe = (AbstractCookingRecipe) WorldUtils.asWorld(world).getRecipeManager().getRecipes()
                .stream()
                .filter(recipe -> (recipe.getType() == IRecipeType.SMELTING) && (recipe instanceof AbstractCookingRecipe) && areEqual(recipe.getRecipeOutput(), stack, true))
                .findFirst()
                .orElse(null);
        return cookingRecipe == null ? 0 : cookingRecipe.getExperience();
    }

    /**
     * @return Smelting result based on given ItemStack.
     */
    public static ItemStack getSmeltingResultAsNewStack(ItemStack stackToSmelt, IWorld world) {
        IRecipe<?> recipe = WorldUtils.asWorld(world).getRecipeManager().getRecipes()
                .stream()
                .filter(checkingRecipe -> (checkingRecipe.getType() == IRecipeType.SMELTING) && checkingRecipe.getIngredients().get(0).test(stackToSmelt))
                .findFirst()
                .orElse(null);

        return recipe == null ? ItemStack.EMPTY : recipe.getRecipeOutput().copy();
    }

    /**
     * @return Smeltable ItemStacks available on specified position.
     */
    public static List<ItemEntity> getSmeltables(IWorld world, BlockPos pos) {
        return WorldUtils.getItems(world, pos)
                .stream()
                .filter(itemEntity -> ItemStackUtils.getSmeltingResultAsNewStack(itemEntity.getItem(), world) != ItemStack.EMPTY)
                .collect(Collectors.toList());
    }

    /**
     * @return Returns true if both stacks are the same in terms of Item and size; otherwise false.
     */
    public static boolean areEqual(ItemStack stack1, ItemStack stack2, boolean checkStackSize) {
        boolean equalItem = stack1.getItem() == stack2.getItem();

        if (checkStackSize) {
            return equalItem && stack1.getCount() == stack2.getCount();
        } else {
            return equalItem;
        }
    }
}
