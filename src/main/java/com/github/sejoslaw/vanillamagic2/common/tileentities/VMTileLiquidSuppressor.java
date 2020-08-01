package com.github.sejoslaw.vanillamagic2.common.tileentities;

import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import com.github.sejoslaw.vanillamagic2.core.VMTiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMTileLiquidSuppressor extends VMTileEntity {
    private int ticksRemaining;
    private int blockStateId;

    public VMTileLiquidSuppressor() {
        super(VMTiles.LIQUID_SUPPRESSOR);
    }

    public void initialize(BlockState state, int duration) {
        this.blockStateId = Block.getStateId(state);
        this.resetDuration(duration);
    }

    public void tick() {
        if (this.getWorld().isRemote()) {
            return;
        }

        this.ticksRemaining--;

        if (this.ticksRemaining <= 0) {
            this.spawnContainedBlock();
        }
    }

    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT nbt = super.write(compound);
        nbt.putInt(NbtUtils.NBT_TICKS, this.ticksRemaining);
        nbt.putInt(NbtUtils.NBT_BLOCK_STATE, this.blockStateId);
        return nbt;
    }

    public void read(CompoundNBT compound) {
        super.read(compound);
        this.ticksRemaining = compound.getInt(NbtUtils.NBT_TICKS);
        this.blockStateId = compound.getInt(NbtUtils.NBT_BLOCK_STATE);
    }

    public void resetDuration(int refresh) {
        this.ticksRemaining = refresh;
    }

    private void spawnContainedBlock() {
        this.remove();
        this.getWorld().setBlockState(this.getPos(), Block.getStateById(this.blockStateId));
    }
}
