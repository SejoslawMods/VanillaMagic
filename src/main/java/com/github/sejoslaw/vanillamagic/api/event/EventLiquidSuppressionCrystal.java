package com.github.sejoslaw.vanillamagic.api.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.github.sejoslaw.vanillamagic.api.item.ICustomItem;

/**
 * Base class for all actions that will happen using Liquid Suppression
 * Crystal.<br>
 * In this case getPos will return the position of the liquid.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventLiquidSuppressionCrystal extends EventCustomItem.OnUseByPlayer {
    private final ItemStack crystalStack;

    public EventLiquidSuppressionCrystal(PlayerEntity player, World world, ItemStack crystalStack, BlockPos blockPos, ICustomItem liquidSuppressionCrystal) {
        super(liquidSuppressionCrystal, player, world, blockPos);
        this.crystalStack = crystalStack;
    }

    /**
     * @return Returns ItemLiquidCrystal. (This should be Player's off-hand.)
     */
    public ItemStack getCrystal() {
        return crystalStack;
    }

    /**
     * This Event is fired before Liquid Suppression Crystal creates the air block.
     */
    public static class CreateAirBlock extends EventLiquidSuppressionCrystal {
        public CreateAirBlock(PlayerEntity player, World world, ItemStack crystalStack, BlockPos blockPos, ICustomItem liquidSuppressionCrystal) {
            super(player, world, crystalStack, blockPos, liquidSuppressionCrystal);
        }
    }

    /**
     * This Event is fired to reset the duration of TileLiquidSuppression.
     */
    public static class UseOnTileEntity extends EventLiquidSuppressionCrystal {
        private final TileEntity tile;

        public UseOnTileEntity(PlayerEntity player, World world, ItemStack crystalStack, BlockPos blockPos, ICustomItem liquidSuppressionCrystal, TileEntity tile) {
            super(player, world, crystalStack, blockPos, liquidSuppressionCrystal);
            this.tile = tile;
        }

        /**
         * @return Returns TileEntity with reseted duration. This is mainly the
         * TileLiquidSuppression.
         */
        public TileEntity getTileEntity() {
            return tile;
        }
    }
}