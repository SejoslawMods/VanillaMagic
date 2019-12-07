package com.github.sejoslaw.vanillamagic.api.event;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import com.github.sejoslaw.vanillamagic.api.item.IEnchantedBucket;

/**
 * Event connected with Enchanted Bucket.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventEnchantedBucket extends EventCustomItem.OnUseByPlayer.OnTileEntity {
    private final IEnchantedBucket bucket;
    private final FluidStack fluidStack;

    public EventEnchantedBucket(IEnchantedBucket customItem, PlayerEntity player, World world, BlockPos blockPos, TileEntity tile, FluidStack fluidStack) {
        super(customItem, player, world, blockPos, tile);
        this.bucket = customItem;
        this.fluidStack = fluidStack;
    }

    /**
     * @return Returns the bucket which will be used.
     */
    public IEnchantedBucket getEnchantedBucket() {
        return bucket;
    }

    /**
     * @return Returns the FluidStack which will be added to IFluidHandler. <br>
     * NULL if used on Cauldron on spawn on World.
     */
    @Nullable
    public FluidStack getFluidStack() {
        return fluidStack;
    }

    /**
     * This Event is fired when Player used Enchanted Bucket to fill Fluid Handler.
     */
    public static class FillFluidHandler extends EventEnchantedBucket {
        private final IFluidHandler handler;

        public FillFluidHandler(IEnchantedBucket bucket, PlayerEntity player, World world, BlockPos blockPos, TileEntity tile, IFluidHandler fluidHandler, FluidStack fluidStack) {
            super(bucket, player, world, blockPos, tile, fluidStack);
            this.handler = fluidHandler;
        }

        /**
         * @return Returns the TileEntity but as IFluidHandler.
         */
        public IFluidHandler getFluidHandler() {
            return handler;
        }

        /**
         * This Event is fired when IFluidHandler is filled using
         * {@link CapabilityFluidHandler#FLUID_HANDLER_CAPABILITY}
         */
        public static class UsingCapability extends FillFluidHandler {
            public UsingCapability(IEnchantedBucket bucket, PlayerEntity player, World world, BlockPos blockPos, TileEntity tile, IFluidHandler fluidHandler, FluidStack fluidStack) {
                super(bucket, player, world, blockPos, tile, fluidHandler, fluidStack);
            }
        }
    }

    /**
     * This Event is fired when Player used Enchanted Bucket to fill Fluid Tank.
     */
    public static class FillFluidTank extends EventEnchantedBucket {
        private final IFluidTank fluidTank;

        public FillFluidTank(IEnchantedBucket bucket, PlayerEntity player, World world, BlockPos blockPos, TileEntity tile, IFluidTank fluidTank, FluidStack fluidStack) {
            super(bucket, player, world, blockPos, tile, fluidStack);
            this.fluidTank = fluidTank;
        }

        /**
         * @return Returns the TileEntity but as IFluidTank.
         */
        public IFluidTank getFluidTank() {
            return fluidTank;
        }
    }

    /**
     * This Event is fired when Player used Enchanted Bucket to fill Cauldron.
     */
    public static class FillCauldron extends EventEnchantedBucket {
        public FillCauldron(IEnchantedBucket bucket, PlayerEntity player, World world, BlockPos blockPos) {
            super(bucket, player, world, blockPos, null, null);
        }
    }

    /**
     * This Event is fired when Player used Enchanted Bucket to spawn liquid.
     */
    public static class SpawnLiquid extends EventEnchantedBucket {
        public SpawnLiquid(IEnchantedBucket bucket, PlayerEntity player, World world, BlockPos blockPos) {
            super(bucket, player, world, blockPos, null, null);
        }
    }
}