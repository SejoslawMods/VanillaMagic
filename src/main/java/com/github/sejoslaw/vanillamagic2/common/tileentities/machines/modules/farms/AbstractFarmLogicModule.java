package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.AbstractLogicModule;
import com.github.sejoslaw.vanillamagic2.common.utils.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.IPlantable;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class AbstractFarmLogicModule extends AbstractLogicModule {
    public void setup(IVMTileMachine machine) {
        BlockPos inventoryPos = machine.getPos().offset(Direction.UP);

        this.setEnergySourcePos(machine, inventoryPos);
        this.setInputStoragePos(machine, inventoryPos);
        this.setOutputStoragePos(machine, inventoryPos);
        this.setWorkingPos(machine, this.getFarmStartPos(machine));
    }

    protected boolean checkStructure(IVMTileMachine machine) {
        return machine.getWorld().getTileEntity(this.getInputStoragePos(machine)) instanceof IInventory;
    }

    protected IInventory getInventory(IVMTileMachine machine) {
        return (IInventory) machine.getWorld().getTileEntity(this.getInputStoragePos(machine));
    }

    protected int getSize(IVMTileMachine machine) {
        IInventory inv = this.getInventory(machine);
        int diamondBlockCount = 1 + inv.count(Items.DIAMOND_BLOCK);
        return diamondBlockCount * VMForgeConfig.FARM_SIZE.get();
    }

    protected void useWorld(IVMTileMachine machine, Consumer<IWorld> consumer) {
        consumer.accept(machine.getWorld());
    }

    protected void useItem(IVMTileMachine machine,
                           Consumer<ItemStack> consumer) {
        IInventory inv = this.getInventory(machine);

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);

            if (stack.getItem() != Items.AIR) {
                consumer.accept(stack);
            }
        }
    }

    protected void useSpace(IVMTileMachine machine,
                            Predicate<BlockPos> check,
                            Consumer<BlockPos> consumer) {
        int size = this.getSize(machine);
        int maxX = machine.getPos().getX() + size;
        int maxZ = machine.getPos().getZ() + size;
        BlockPos startPos = this.getFarmStartPos(machine);
        BlockPos workingPos = this.getWorkingPos(machine);

        workingPos = workingPos.add(1, 0, 0);

        if (workingPos.getX() > maxX) {
            workingPos = new BlockPos(startPos.getX(), startPos.getY(), workingPos.getZ() + 1);
        }

        if (workingPos.getZ() > maxZ) {
            workingPos = startPos;
        }

        this.setWorkingPos(machine, workingPos);

        if (check.test(workingPos)) {
            consumer.accept(workingPos);
        }
    }

    protected boolean isSupportedBlock(ItemStack stack) {
        Block block = BlockUtils.getBlockFromItem(stack.getItem());
        return block instanceof IPlantable || block instanceof IGrowable;
    }

    protected BlockPos getFarmStartPos(IVMTileMachine machine) {
        int size = this.getSize(machine);
        BlockPos machinePos = machine.getPos();
        return new BlockPos(machinePos.getX() - size, machinePos.getY(), machinePos.getZ() - size);
    }
}
