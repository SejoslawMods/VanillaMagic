package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IMachineModule;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class AbstractMachineModule implements IMachineModule {
    protected boolean hasKey(IVMTileMachine machine, String key) {
        return machine.getTileData().hasUniqueId(key);
    }

    protected int getInt(IVMTileMachine machine, String key) {
        return machine.getTileData().getInt(key);
    }

    protected Direction getDirection(IVMTileMachine machine, String key) {
        return Direction.byIndex(machine.getTileData().getInt(key));
    }

    protected void setInt(IVMTileMachine machine, int value, String key) {
        machine.getTileData().putInt(key, value);
    }

    protected BlockPos getWorkingPos(IVMTileMachine machine) {
        return this.getPos(machine, NbtUtils.NBT_MODULE_WORKING_POS);
    }

    protected void setWorkingPos(IVMTileMachine machine, BlockPos pos) {
        this.setPos(machine, pos, NbtUtils.NBT_MODULE_WORKING_POS);
    }

    protected BlockPos getStartPos(IVMTileMachine machine) {
        return this.getPos(machine, NbtUtils.NBT_MODULE_START_POS);
    }

    protected void setStartPos(IVMTileMachine machine, BlockPos pos) {
        this.setPos(machine, pos, NbtUtils.NBT_MODULE_START_POS);
    }

    protected BlockPos getInputStoragePos(IVMTileMachine machine) {
        return this.getPos(machine, NbtUtils.NBT_MODULE_STORAGE_INPUT_POS);
    }

    protected void setInputStoragePos(IVMTileMachine machine, BlockPos pos) {
        this.setPos(machine, pos, NbtUtils.NBT_MODULE_STORAGE_INPUT_POS);
    }

    protected BlockPos getOutputStoragePos(IVMTileMachine machine) {
        return this.getPos(machine, NbtUtils.NBT_MODULE_STORAGE_OUTPUT_POS);
    }

    protected void setOutputStoragePos(IVMTileMachine machine, BlockPos pos) {
        this.setPos(machine, pos, NbtUtils.NBT_MODULE_STORAGE_OUTPUT_POS);
    }

    protected boolean getHasEnergy(IVMTileMachine machine) {
        return machine.getTileData().getBoolean(NbtUtils.NBT_MODULE_HAS_ENERGY);
    }

    protected void setHasEnergy(IVMTileMachine machine, boolean value) {
        machine.getTileData().putBoolean(NbtUtils.NBT_MODULE_HAS_ENERGY, value);
    }

    protected BlockPos getEnergySourcePos(IVMTileMachine machine) {
        return this.getPos(machine, NbtUtils.NBT_MODULE_ENERGY_SOURCE_POS);
    }

    protected void setEnergySourcePos(IVMTileMachine machine, BlockPos pos) {
        this.setPos(machine, pos, NbtUtils.NBT_MODULE_ENERGY_SOURCE_POS);
    }

    protected BlockPos getPos(IVMTileMachine machine, String key) {
        return BlockPos.fromLong(machine.getTileData().getLong(key));
    }

    protected void setPos(IVMTileMachine machine, BlockPos pos, String key) {
        machine.getTileData().putLong(key, pos.toLong());
    }
}
