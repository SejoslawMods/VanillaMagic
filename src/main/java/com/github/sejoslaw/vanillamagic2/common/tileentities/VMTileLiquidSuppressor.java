package com.github.sejoslaw.vanillamagic2.common.tileentities;

import com.github.sejoslaw.vanillamagic2.common.registries.TileEntityRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.BlockUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMTileLiquidSuppressor extends VMTileEntity {
    private int ticksRemaining;
    private int blockStateId;

    public VMTileLiquidSuppressor() {
        super(TileEntityRegistry.LIQUID_SUPPRESSOR.get());
    }

    public void initialize(BlockState state, int duration) {
        this.blockStateId = Block.getStateId(state);
        this.resetDuration(duration);
    }

    public void tickTileEntity() {
        this.ticksRemaining--;

        if (this.ticksRemaining <= 0) {
            this.remove();
            this.getWorld().setBlockState(this.getPos(), Block.getStateById(this.blockStateId));
        }
    }

    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT nbt = super.write(compound);
        nbt.putInt(NbtUtils.NBT_TICKS, this.ticksRemaining);
        nbt.putInt(NbtUtils.NBT_BLOCK_STATE, this.blockStateId);
        return nbt;
    }

    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.ticksRemaining = compound.getInt(NbtUtils.NBT_TICKS);
        this.blockStateId = compound.getInt(NbtUtils.NBT_BLOCK_STATE);
    }

    public void resetDuration(int refresh) {
        this.ticksRemaining = refresh;
    }

    public static Block[] getValidBlocks() {
        List<Block> blocks = ForgeRegistries.FLUIDS
                .getValues()
                .stream()
                .map(fluid -> fluid.getDefaultState().getBlockState().getBlock())
                .collect(Collectors.toList());

        return BlockUtils.getValidBlocks(blocks);
    }
}
