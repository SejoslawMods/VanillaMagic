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
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
        CompoundNBT nbt = new CompoundNBT();

        nbt.putString("id", jsonItemStack.getId());

        byte count = jsonItemStack.getCount();
        nbt.putByte("Count", count <= 0 ? 1 : count);

        int meta = jsonItemStack.getMeta();
        nbt.putInt("Damage", Math.max(meta, 0));

        return ItemStack.read(nbt);
    }

    /**
     * @return ItemStacks from JSON Object.
     */
    public static List<ItemStack> getItemStacksFromJson(IJsonService rootService, String key) {
        List<ItemStack> stacks = new ArrayList<>();

        for (IJsonService service : rootService.getList(key)) {
            ItemStack stack = getItemStackFromJson(service.getItemStack(key));
            stacks.add(stack);
        }

        return stacks;
    }

    /**
     * @return ItemStacks with smelting result based on given input.
     */
    public static List<ItemStack> smeltItems(PlayerEntity player, List<ItemEntity> stacksToSmelt, int smeltingCost) {
        ItemStack leftHandStack = player.getHeldItemOffhand();
        World world = player.world;
        final int[] ticks = {0};

        return stacksToSmelt
                .stream()
                .map(entity -> {
                    ItemStack stack = entity.getItem();
                    int stackSize = stack.getCount();
                    int ticksToSmeltStack = stackSize * smeltingCost;

                    while (leftHandStack.getCount() > 0 && ticks[0] < ticksToSmeltStack) {
                        ticks[0] += AbstractFurnaceTileEntity.getBurnTimes().getOrDefault(stack.getItem(), 0);
                        stack.grow(-1);
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
    public static float getExperienceFromStack(ItemStack stack, World world) {
        AbstractCookingRecipe cookingRecipe = (AbstractCookingRecipe) world.getRecipeManager().getRecipes()
                .stream()
                .filter(recipe -> (recipe.getType() == IRecipeType.SMELTING) && (recipe instanceof AbstractCookingRecipe) && ItemStack.areItemStacksEqual(recipe.getRecipeOutput(), stack))
                .findFirst()
                .orElse(null);
        return cookingRecipe == null ? 0 : cookingRecipe.getExperience();
    }

    /**
     * @return Smelting result based on given ItemStack.
     */
    public static ItemStack getSmeltingResultAsNewStack(ItemStack stackToSmelt, World world) {
        IRecipe<?> recipe = world.getRecipeManager().getRecipes()
                .stream()
                .filter(checkingRecipe -> (checkingRecipe.getType() == IRecipeType.SMELTING) && checkingRecipe.getIngredients().get(0).test(stackToSmelt))
                .findFirst()
                .orElse(null);

        return recipe == null ? ItemStack.EMPTY : recipe.getRecipeOutput();
    }

    /**
     * @return Smeltable ItemStacks available on specified position.
     */
    public static List<ItemEntity> getSmeltables(World world, BlockPos pos) {
        return WorldUtils.getItems(world, pos)
                .stream()
                .map(itemEntity -> getSmeltingResultAsNewStack(itemEntity.getItem(), world))
                .filter(stack -> stack != ItemStack.EMPTY)
                .map(stack -> new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack))
                .collect(Collectors.toList());
    }
}
