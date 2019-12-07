package com.github.sejoslaw.vanillamagic.common.util;

import com.github.sejoslaw.vanillamagic.api.quest.IQuest;
import com.github.sejoslaw.vanillamagic.quest.QuestSmeltOnAltar;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class which store various methods connected with smelting.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class SmeltingUtil {
    private SmeltingUtil() {
    }

    public static boolean isBlockOre(Block block) {
        if (block instanceof OreBlock) { // Coal, Diamond, Lapis, Emerald, Quartz, etc.
            return true;
        }

        return block.getRegistryName().toString().contains("ore");
    }

    public static List<ItemEntity> getOresInCauldron(World world, BlockPos cauldronPos) {
        List<ItemEntity> smeltablesInCauldron = getSmeltable(world, cauldronPos);

        return smeltablesInCauldron
                .stream()
                .filter(item -> item.getItem().getItem().getRegistryName().toString().contains("ore"))
                .collect(Collectors.toList());
    }

    /**
     * @return Returns the all fuelStacks from the inventory
     */
    public static List<ItemStack> getFuelFromInventory(IInventory inv) {
        List<ItemStack> fuels = new ArrayList<>();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stackInSlot = inv.getStackInSlot(i);

            if (isItemFuel(stackInSlot)) {
                fuels.add(stackInSlot);
            }
        }

        return fuels;
    }

    /**
     * @return Returns the first fuelStack from the inventory.<br>
     * Indexes:<br>
     * 0 - ItemStack<br>
     * 1 - index (slot)
     */
    @Nullable
    public static Object[] getFuelFromInventoryAndDelete(IInventory inv) {
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stackInSlot = inv.getStackInSlot(i);

            if (isItemFuel(stackInSlot)) {
                return new Object[]{inv.removeStackFromSlot(i), i};
            }
        }

        return null;
    }

    /**
     * @return Returns the smeltables ItemEntitys from the ALL entities in cauldron
     * BlockPos
     */
    public static List<ItemEntity> getSmeltable(List<ItemEntity> entitiesInCauldron) {
        List<ItemEntity> itemsToSmelt = new ArrayList<ItemEntity>();

        for (int i = 0; i < entitiesInCauldron.size(); ++i) {
            ItemEntity entityItemInCauldron = entitiesInCauldron.get(i);
            ItemStack smeltResult = getSmeltingResultAsNewStack(entityItemInCauldron.getItem(), entityItemInCauldron.world);

            if (!ItemStackUtil.isNullStack(smeltResult)) {
                itemsToSmelt.add(entityItemInCauldron);
            }
        }

        return itemsToSmelt;
    }

    public static List<ItemEntity> getSmeltable(World world, BlockPos cauldronPos) {
        return getSmeltable(CauldronUtil.getItemsInCauldron(world, cauldronPos));
    }

    /**
     * @return Returns the all fuel from the Cauldron position
     */
    public static List<ItemEntity> getFuelFromCauldron(World world, BlockPos cauldronPos) {
        List<ItemEntity> itemsInCauldron = CauldronUtil.getItemsInCauldron(world, cauldronPos);
        List<ItemEntity> fuels = new ArrayList<ItemEntity>();

        if (itemsInCauldron.size() == 0) {
            return fuels;
        }

        for (int i = 0; i < itemsInCauldron.size(); ++i) {
            ItemEntity entityItemInCauldron = itemsInCauldron.get(i);
            ItemStack stack = entityItemInCauldron.getItem();

            if (isItemFuel(stack)) {
                fuels.add(entityItemInCauldron);
            }
        }

        return fuels;
    }

    public static int countTicks(ItemStack stackOffHand) {
        return ItemStackUtil.getStackSize(stackOffHand) * getItemBurnTimeTicks(stackOffHand);
    }

    /**
     * @return e.g. will return 1600 if the item was Coal. Won't care about
     * stackSize
     */
    public static int getItemBurnTimeTicks(ItemStack fuel) {
        return AbstractFurnaceTileEntity.getBurnTimes().getOrDefault(fuel.getItem(), 0);
    }

    public static boolean isItemFuel(ItemStack stackInOffHand) {
        return AbstractFurnaceTileEntity.isFuel(stackInOffHand);
    }

    public static ItemStack getSmeltingResultAsNewStack(ItemStack stackToSmelt, World world) {
        IRecipe<?> recipe = world.getRecipeManager().getRecipes()
                .stream()
                .filter(x -> x.getType() == IRecipeType.SMELTING)
                .filter(x -> {
                    NonNullList<Ingredient> ingredients = x.getIngredients();
                    Ingredient smeltingIngredient = ingredients.get(0);
                    return smeltingIngredient.test(stackToSmelt);
                })
                .findFirst()
                .orElse(null);

        return recipe == null ? ItemStack.EMPTY : recipe.getRecipeOutput().copy();
    }

    public static float getExperienceToAddFromWholeStack(ItemStack stack, World world) {
        AbstractCookingRecipe cookingRecipe = world.getRecipeManager().getRecipes()
                .stream()
                .filter(recipe -> recipe.getType() == IRecipeType.SMELTING)
                .filter(recipe -> recipe instanceof AbstractCookingRecipe)
                .map(recipe -> (AbstractCookingRecipe)recipe)
                .filter(recipe -> ItemStack.areItemStacksEqual(recipe.getRecipeOutput(), stack))
                .findFirst()
                .orElse(null);

        return cookingRecipe == null ? 0 : cookingRecipe.getExperience();
    }

    public static List<ItemEntity> countAndSmelt_OneByOneItemFromOffHand(PlayerEntity player, List<ItemEntity> itemsToSmelt, BlockPos cauldronPos, IQuest requiredQuest, boolean spawnSmelted) {
        if (requiredQuest.canPlayerGetQuest(player)) {
            QuestUtil.addStat(player, requiredQuest);
        }

        if (!QuestUtil.hasQuestUnlocked(player, requiredQuest)) {
            return new ArrayList<>();
        }

        List<ItemEntity> smelted = new ArrayList<ItemEntity>();
        World world = player.world;
        int ticks = 0;
        ItemStack rightHand = player.getHeldItemOffhand();

        for (int i = 0; i < itemsToSmelt.size(); ++i) {
            ItemEntity entityItemToSmelt = itemsToSmelt.get(i);
            ItemStack entityItemToSmeltStack = entityItemToSmelt.getItem();

            int entityItemToSmeltStackSize = ItemStackUtil.getStackSize(entityItemToSmeltStack);
            int ticksToSmeltStack = entityItemToSmeltStackSize * QuestSmeltOnAltar.ONE_ITEM_SMELT_TICKS;

            ItemStack smeltResult = null;
            ItemEntity smeltResultItemEntity = null;

            while ((ItemStackUtil.getStackSize(rightHand) > 0) && (ticks < ticksToSmeltStack)) {
                ticks += getItemBurnTimeTicks(rightHand);
                ItemStackUtil.decreaseStackSize(rightHand, 1);
            }

            if (ticks >= ticksToSmeltStack) {
                smeltResult = getSmeltingResultAsNewStack(entityItemToSmeltStack, world);
                ItemStackUtil.setStackSize(smeltResult, ItemStackUtil.getStackSize(entityItemToSmeltStack));
                smeltResultItemEntity = new ItemEntity(world, cauldronPos.getX() + 0.5D, cauldronPos.getY(), cauldronPos.getZ() + 0.5D, smeltResult);
                entityItemToSmelt.remove();
            } else if (ticks >= QuestSmeltOnAltar.ONE_ITEM_SMELT_TICKS) {
                int howManyCanSmelt = ticks / QuestSmeltOnAltar.ONE_ITEM_SMELT_TICKS;
                ItemStackUtil.decreaseStackSize(entityItemToSmeltStack, howManyCanSmelt);
                smeltResult = getSmeltingResultAsNewStack(entityItemToSmeltStack, world);
                ItemStackUtil.setStackSize(smeltResult, howManyCanSmelt);
                smeltResultItemEntity = new ItemEntity(world, cauldronPos.getX() + 0.5D, cauldronPos.getY(), cauldronPos.getZ() + 0.5D, smeltResult);
            } else {
                break;
            }

            if (spawnSmelted) {
                world.addEntity(smeltResultItemEntity);
            }

            smelted.add(smeltResultItemEntity);
            ticks -= ticksToSmeltStack;

            // TODO: Fix the experience after smelting.
            float experienceToAdd = getExperienceToAddFromWholeStack(entityItemToSmeltStack, world);
            player.experience += experienceToAdd;
        }

        return smelted;
    }
}