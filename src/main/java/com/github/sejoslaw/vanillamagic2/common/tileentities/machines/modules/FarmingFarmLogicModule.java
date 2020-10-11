package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms.AbstractFarmLogicModule;
import com.github.sejoslaw.vanillamagic2.common.utils.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CocoaBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class FarmingFarmLogicModule extends AbstractFarmLogicModule {
    private interface IFarmer {
        boolean isFullyGrown(IWorld world, BlockPos pos);
    }

    private static class Farmer<TBlock extends Block> implements IFarmer {
        private Class<TBlock> clazz;
        private Predicate<BlockState> predicate;

        public Farmer(Class<TBlock> clazz, Predicate<BlockState> predicate) {
            this.clazz = clazz;
            this.predicate = predicate;
        }

        public boolean isFullyGrown(IWorld world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            return this.clazz.isInstance(state.getBlock()) && predicate.test(state);
        }
    }

    private static List<IFarmer> FARMERS = new ArrayList<>();

    static {
        // TODO: Add more farmers
        FARMERS.add(new Farmer<>(CocoaBlock.class, state -> state.get(CocoaBlock.AGE) == 2));
    }

    protected void work(IVMTileMachine machine) {
        this.executeLogic(machine,
                (world, stack, pos) -> FARMERS.stream().anyMatch(farmer -> farmer.isFullyGrown(world, pos)),
                (world, stack, pos) -> BlockUtils.breakBlock(ItemStack.EMPTY, world, this.getFarmer(world), pos));
    }
}
