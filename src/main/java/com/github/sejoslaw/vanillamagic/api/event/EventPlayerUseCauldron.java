package com.github.sejoslaw.vanillamagic.api.event;

import java.util.List;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This Event is fired when Player try to use Cauldron in Vanilla Magic-way.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventPlayerUseCauldron extends EventPlayerOnWorld {
    private final BlockPos cauldronPos;

    public EventPlayerUseCauldron(PlayerEntity player, World world, BlockPos cauldronPos) {
        super(player, world);
        this.cauldronPos = cauldronPos;
    }

    /**
     * @return Returns position of the Cauldron.
     */
    public BlockPos getCauldronPos() {
        return cauldronPos;
    }

    /**
     * This Event is fired when Player use Cauldron with some kind of input inside
     * in Cauldron.
     */
    public static class FromInput extends EventPlayerUseCauldron {
        private final List<ItemEntity> input;

        public FromInput(PlayerEntity player, World world, BlockPos cauldronPos, List<ItemEntity> input) {
            super(player, world, cauldronPos);
            this.input = input;
        }

        /**
         * @return Returns list with all input items.
         */
        public List<ItemEntity> getInputItems() {
            return input;
        }
    }

    /**
     * This Event is fired BEFORE Player craft something on Altar.
     */
    public static class CraftOnAltar extends FromInput {
        private final ItemStack[] result;

        public CraftOnAltar(PlayerEntity player, World world, BlockPos cauldronPos, List<ItemEntity> ingredients, ItemStack[] result) {
            super(player, world, cauldronPos, ingredients);
            this.result = result;
        }

        /**
         * @return Returns array of all ItemStack (results) that will be spawned after
         * the crafting is done.
         */
        public ItemStack[] getCraftingResult() {
            return result;
        }
    }

    /**
     * This Event is fired when Player try to double ores in Cauldron.
     */
    public static class OreMultiplier extends FromInput {
        public OreMultiplier(PlayerEntity player, World world, BlockPos cauldronPos, List<ItemEntity> oresInCauldron) {
            super(player, world, cauldronPos, oresInCauldron);
        }
    }

    /**
     * This Event is fired when Player try to smelt items on Altar.
     */
    public static class SmeltOnAltar extends FromInput {
        public SmeltOnAltar(PlayerEntity player, World world, BlockPos cauldronPos, List<ItemEntity> itemsToSmelt) {
            super(player, world, cauldronPos, itemsToSmelt);
        }
    }
}