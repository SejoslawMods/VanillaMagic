package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.functions.Consumer3;
import com.github.sejoslaw.vanillamagic2.common.functions.Function3;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.AbstractLogicModule;
import com.github.sejoslaw.vanillamagic2.common.utils.BlockUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class AbstractFarmLogicModule extends AbstractLogicModule {
    private static GameProfile FARMER_GAME_PROFILE = new GameProfile(UUID.randomUUID(), "VM Farmer");

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
        BlockPos workingPos = this.getWorkingPos(machine);

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

    protected void executeLogic(IVMTileMachine machine,
                                Function3<IWorld, ItemStack, BlockPos, Boolean> check,
                                Consumer3<IWorld, ItemStack, BlockPos> consumer) {
        this.useWorld(machine, world ->
            this.useItem(machine, stack ->
                this.useSpace(machine, pos -> check.apply(world, stack, pos), pos -> {
                    consumer.accept(world, stack, pos);
                })));
    }

    protected FakePlayer getFarmer(IWorld world) {
        return FakePlayerFactory.get((ServerWorld) world, FARMER_GAME_PROFILE);
    }
}
