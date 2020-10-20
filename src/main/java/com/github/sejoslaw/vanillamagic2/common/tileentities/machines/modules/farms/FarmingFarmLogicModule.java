package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.utils.BlockUtils;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
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
        private final Class<TBlock> clazz;
        private final Predicate<BlockState> predicate;
        private final boolean canBreak;

        public Farmer(Class<TBlock> clazz, Predicate<BlockState> predicate) {
            this(clazz, predicate, true);
        }

        public Farmer(Class<TBlock> clazz, Predicate<BlockState> predicate, boolean canBreak) {
            this.clazz = clazz;
            this.predicate = predicate;
            this.canBreak = canBreak;
        }

        public boolean isFullyGrown(IWorld world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            return this.clazz.isInstance(state.getBlock()) && this.checkPredicate(state) && this.canBreak;
        }

        public boolean checkPredicate(BlockState state) {
            try {
                return this.predicate.test(state);
            } catch (Exception ex) {
                return false;
            }
        }
    }

    private List<IFarmer> farmers = new ArrayList<>();

    public FarmingFarmLogicModule() {
        this.addFarmer(AbstractTopPlantBlock.class, AbstractTopPlantBlock.AGE);
        this.addFarmer(BambooBlock.class, BambooBlock.PROPERTY_AGE);
        this.addFarmer(BeetrootBlock.class, BeetrootBlock.BEETROOT_AGE);
        this.addFarmer(CactusBlock.class, CactusBlock.AGE);
        this.addFarmer(CocoaBlock.class, CocoaBlock.AGE);
        this.addFarmer(CropsBlock.class, CropsBlock.AGE);
        this.addFarmer(NetherWartBlock.class, NetherWartBlock.AGE);
        this.addFarmer(SeaPickleBlock.class, SeaPickleBlock.PICKLES);
        this.addFarmer(StemBlock.class, StemBlock.AGE, false);
        this.addFarmer(StemGrownBlock.class);
        this.addFarmer(SugarCaneBlock.class, SugarCaneBlock.AGE);
        this.addFarmer(SweetBerryBushBlock.class, SweetBerryBushBlock.AGE);
        this.addFarmer(BeetrootBlock.class, BeetrootBlock.BEETROOT_AGE);
    }

    protected void work(IVMTileMachine machine) {
        this.executeLogic(machine,
                (world, stack, pos) -> farmers.stream().anyMatch(farmer -> farmer.isFullyGrown(world, pos)),
                (world, stack, pos) -> BlockUtils.breakBlock(ItemStack.EMPTY, world, this.getFakePlayer(world), pos));
    }

    private <T extends Block> void addFarmer(Class<T> clazz, IntegerProperty prop) {
        this.addFarmer(clazz, prop, true);
    }

    private <T extends Block> void addFarmer(Class<T> clazz, IntegerProperty prop, boolean canBreak) {
        farmers.add(new Farmer<>(clazz, state -> state.get(prop) == BlockUtils.getMaxValue(prop), canBreak));
    }

    private <T extends Block> void addFarmer(Class<T> clazz) {
        farmers.add(new Farmer<>(clazz, state -> true));
    }
}
