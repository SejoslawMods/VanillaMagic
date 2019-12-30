package com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.IMachine;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IFarm extends IMachine {
    /**
     * @return True if a specified position is an Air.
     */
    boolean isAir(BlockPos pos);

    /**
     * @return True if Farm has got specified seed in input inventory.
     */
    boolean hasSeed(ItemStack seeds);

    /**
     * @return ItemStack taken from input inventory
     */
    ItemStack takeSeedFromSupplies(ItemStack seeds, BlockPos forBlock);

    /**
     * @return True if the input inventory contains any Hoe.
     */
    boolean hasHoe();

    /**
     * @return True if the input inventory contains any Axe.
     */
    boolean hasAxe();

    /**
     * @return True if the input inventory contains any Shears.
     */
    boolean hasShears();

    /**
     * @return Max value for looting from items in input inventory.
     */
    int getMaxLootingValue();

    /**
     * Damages How in input inventory.
     */
    void damageHoe(int damage, BlockPos pos);

    /**
     * Damages Axe in input inventory.
     */
    void damageAxe(Block block, BlockPos pos);

    /**
     * Damages Shears in input inventory.
     */
    void damageShears(Block block, BlockPos pos);

    /**
     * @return True if the block beneath the specified location can be tilled (changed into Farmland).
     */
    boolean tillBlock(BlockPos plantingLocation);

    /**
     * @return True if the block can be tilled.
     */
    boolean isTillable(Block block);

    /**
     * Clears Farm input inventory.
     */
    void clearInputInventory();

    /**
     * @return <=0 - break no leaves for saplings 50 - break half the leaves for saplings 90- break 90% of the leaves for saplings
     */
    int isLowOnSaplings(BlockPos pos);
}