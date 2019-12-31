package com.github.sejoslaw.vanillamagic.common.tileentity.machine;

import com.github.sejoslaw.vanillamagic.api.event.EventMachine;
import com.github.sejoslaw.vanillamagic.api.exception.NotInventoryException;
import com.github.sejoslaw.vanillamagic.api.inventory.IInventoryWrapper;
import com.github.sejoslaw.vanillamagic.api.inventory.InventoryWrapper;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.IMachine;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.config.VMConfig;
import com.github.sejoslaw.vanillamagic.common.inventory.InventoryHelper;
import com.github.sejoslaw.vanillamagic.common.tileentity.CustomTileEntity;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.common.util.NBTUtil;
import com.github.sejoslaw.vanillamagic.common.util.SmeltingUtil;
import com.github.sejoslaw.vanillamagic.core.VMLogger;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.dimension.DimensionType;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Base Machine definition.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class TileMachine extends CustomTileEntity implements IMachine {
    protected BlockPos workingPos;
    protected BlockPos startPos;

    protected ItemStack shouldBeInLeftHand;
    protected ItemStack shouldBeInRightHand;

    protected int radius = 4;
    protected int oneOperationCost = VMConfig.TILE_MACHINE_ONE_OPERATION_COST.get();
    protected int ticks = 0;
    protected int maxTicks = VMConfig.TILE_MACHINE_MAX_TICKS.get();
    protected int delay = 0;
    protected int delayInTicks = 0;

    protected boolean isActive = false;
    protected boolean finished = false;
    protected boolean needsFuel = true;

    protected IInventoryWrapper inventoryInput;
    protected IInventoryWrapper inventoryOutput;

    protected BlockPos chestPosInput;
    protected BlockPos chestPosOutput;

    public TileMachine(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    /**
     * This should check if the Machine is build correctly.
     */
    public abstract boolean checkSurroundings();

    /**
     * This method is for a Machine work. Each Machine have to know when to decrease
     * ticks. For instance: I don't want Quarry to decrease ticks if it hits Air or
     * Bedrock. <br>
     * In this method Machine should, for instance: move workingPos.
     */
    public abstract void doWork();

    public boolean inventoryOutputHasSpace() {
        IInventoryWrapper outInv = getOutputInventory();

        if (outInv == null) {
            return false;
        }

        return !InventoryHelper.isInventoryFull(outInv.getInventory(), getOutputDirection());
    }

    public void init() {
        super.init();

        checkSurroundings();
        setWorkRadius(radius);
        setWorkingPos(pos);
    }

    public void tick() {
        if (delay < delayInTicks) {
            delay++;
            return;
        }

        delay = 0;

        if (!world.getChunkProvider().isChunkLoaded(new ChunkPos(pos))
                || this.getWorkingPos() == null
                || !world.getChunkProvider().isChunkLoaded(new ChunkPos(this.getWorkingPos()))
                || this.getInputInventory() == null
                || this.getOutputInventory() == null
                || !this.inventoryOutputHasSpace()) {
            return;
        }

        this.showBoundingBox();

        if (needsFuel) {
            this.checkFuel();
        }

        if (ticks < oneOperationCost || EventUtil.postEvent(new EventMachine.Work(this, world, pos))) {
            return;
        }

        isActive = true;

        if (ticks < oneOperationCost) {
            isActive = false;
            return;
        }

        ticks -= oneOperationCost;

        this.performAdditionalOperations();
        this.doWork();
    }

    public IInventoryWrapper getInputInventory() {
        return inventoryInput;
    }

    public void setInputInventory(IInventoryWrapper inv) {
        this.inventoryInput = inv;
    }

    public IInventoryWrapper getOutputInventory() {
        return inventoryOutput;
    }

    public void setOutputInventory(IInventoryWrapper inv) {
        this.inventoryOutput = inv;
    }

    /**
     * Method which is execute before the right doWork()
     */
    protected void performAdditionalOperations() {
    }

    /**
     * Additional method for showing the box on which Machine operates.
     */
    public void showBoundingBox() {
    }

    public boolean isWorkFinished() {
        return finished;
    }

    public BlockPos getWorkingPos() {
        return workingPos;
    }

    public void setWorkingPos(BlockPos newPos) {
        this.workingPos = newPos;
    }

    public BlockPos getStartPos() {
        return startPos;
    }

    public void setNewStartPos(BlockPos newStartPos) {
        this.startPos = newStartPos;
    }

    public int getWorkRadius() {
        return radius;
    }

    public void setWorkRadius(int newRadius) {
        this.radius = newRadius;
    }

    public int getOneOperationCost() {
        return oneOperationCost;
    }

    public int getCurrentTicks() {
        return ticks;
    }

    public void setCurrentTicks(int ticks) {
        this.ticks = ticks;
    }

    public int getMaxTicks() {
        return maxTicks;
    }

    public boolean isActive() {
        return isActive;
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();

        compound.putInt(NBTUtil.NBT_MACHINE_POS_X, pos.getX());
        compound.putInt(NBTUtil.NBT_MACHINE_POS_Y, pos.getY());
        compound.putInt(NBTUtil.NBT_MACHINE_POS_Z, pos.getZ());

        compound.putInt(NBTUtil.NBT_DIMENSION, world.getDimension().getType().getId());

        compound.putInt(NBTUtil.NBT_WORKING_POS_X, workingPos.getX());
        compound.putInt(NBTUtil.NBT_WORKING_POS_Y, workingPos.getY());
        compound.putInt(NBTUtil.NBT_WORKING_POS_Z, workingPos.getZ());

        compound.putInt(NBTUtil.NBT_RADIUS, radius);
        compound.putInt(NBTUtil.NBT_ONE_OPERATION_COST, oneOperationCost);
        compound.putInt(NBTUtil.NBT_TICKS, ticks);
        compound.putInt(NBTUtil.NBT_MAX_TICKS, maxTicks);
        compound.putBoolean(NBTUtil.NBT_IS_ACTIVE, isActive);
        compound.putBoolean(NBTUtil.NBT_NEEDS_FUEL, needsFuel);

        compound.putInt(NBTUtil.NBT_START_POS_X, startPos.getX());
        compound.putInt(NBTUtil.NBT_START_POS_Y, startPos.getY());
        compound.putInt(NBTUtil.NBT_START_POS_Z, startPos.getZ());

        compound.putInt(NBTUtil.NBT_CHEST_POS_INPUT_X, chestPosInput.getX());
        compound.putInt(NBTUtil.NBT_CHEST_POS_INPUT_Y, chestPosInput.getY());
        compound.putInt(NBTUtil.NBT_CHEST_POS_INPUT_Z, chestPosInput.getZ());

        compound.putInt(NBTUtil.NBT_CHEST_POS_OUTPUT_X, chestPosOutput.getX());
        compound.putInt(NBTUtil.NBT_CHEST_POS_OUTPUT_Y, chestPosOutput.getY());
        compound.putInt(NBTUtil.NBT_CHEST_POS_OUTPUT_Z, chestPosOutput.getZ());

        return compound;
    }

    public void deserializeNBT(CompoundNBT compound) {
        int machinePosX = compound.getInt(NBTUtil.NBT_MACHINE_POS_X);
        int machinePosY = compound.getInt(NBTUtil.NBT_MACHINE_POS_Y);
        int machinePosZ = compound.getInt(NBTUtil.NBT_MACHINE_POS_Z);

        this.pos = new BlockPos(machinePosX, machinePosY, machinePosZ);

        int workingPosX = compound.getInt(NBTUtil.NBT_WORKING_POS_X);
        int workingPosY = compound.getInt(NBTUtil.NBT_WORKING_POS_Y);
        int workingPosZ = compound.getInt(NBTUtil.NBT_WORKING_POS_Z);

        this.workingPos = new BlockPos(workingPosX, workingPosY, workingPosZ);

        this.radius = compound.getInt(NBTUtil.NBT_RADIUS);
        this.oneOperationCost = compound.getInt(NBTUtil.NBT_ONE_OPERATION_COST);
        this.ticks = compound.getInt(NBTUtil.NBT_TICKS);
        this.maxTicks = compound.getInt(NBTUtil.NBT_MAX_TICKS);
        this.isActive = compound.getBoolean(NBTUtil.NBT_IS_ACTIVE);
        this.needsFuel = compound.getBoolean(NBTUtil.NBT_NEEDS_FUEL);

        int startPosX = compound.getInt(NBTUtil.NBT_START_POS_X);
        int startPosY = compound.getInt(NBTUtil.NBT_START_POS_Y);
        int startPosZ = compound.getInt(NBTUtil.NBT_START_POS_Z);

        this.startPos = new BlockPos(startPosX, startPosY, startPosZ);

        int chestPosInputX = compound.getInt(NBTUtil.NBT_CHEST_POS_INPUT_X);
        int chestPosInputY = compound.getInt(NBTUtil.NBT_CHEST_POS_INPUT_Y);
        int chestPosInputZ = compound.getInt(NBTUtil.NBT_CHEST_POS_INPUT_Z);

        this.chestPosInput = new BlockPos(chestPosInputX, chestPosInputY, chestPosInputZ);

        int chestPosOutputX = compound.getInt(NBTUtil.NBT_CHEST_POS_OUTPUT_X);
        int chestPosOutputY = compound.getInt(NBTUtil.NBT_CHEST_POS_OUTPUT_Y);
        int chestPosOutputZ = compound.getInt(NBTUtil.NBT_CHEST_POS_OUTPUT_Z);

        this.chestPosOutput = new BlockPos(chestPosOutputX, chestPosOutputY, chestPosOutputZ);

        try {
            if (inventoryInput == null) {
                inventoryInput = new InventoryWrapper(world, chestPosInput);
            }
            if (inventoryOutput == null) {
                inventoryOutput = new InventoryWrapper(world, chestPosOutput);
            }
        } catch (NotInventoryException e) {
            VMLogger.log(Level.ERROR, this.getClass().getSimpleName() + " - error when converting to IInventory at position: " + e.position.toString());
        }
    }

    public void checkFuel() {
        if (ticks >= maxTicks) {
            return;
        }

        if (inventoryInput != null) {
            IInventory invInput = getInputInventory().getInventory();
            Object[] returnedStack = SmeltingUtil.getFuelFromInventoryAndDelete(invInput);

            if (returnedStack == null) {
                return;
            }

            ItemStack fuelToAdd = (ItemStack) returnedStack[0];

            if (ItemStackUtil.isIInventory(fuelToAdd)) {
                try {
                    InventoryHelper.insertStack(null, invInput, fuelToAdd, (int) returnedStack[1], Direction.DOWN);
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
                return;
            }
            ticks += SmeltingUtil.countTicks(fuelToAdd);
        } else {
            List<ItemEntity> fuelsInCauldron = SmeltingUtil.getFuelFromCauldron(world, pos);

            if (fuelsInCauldron.size() == 0) {
                return;
            }

            for (ItemEntity entityItem : fuelsInCauldron) {
                ItemStack stack = entityItem.getItem();
                ticks += SmeltingUtil.countTicks(stack);

                if (ticks >= oneOperationCost) {
                    entityItem.remove();
                }
            }
        }
    }

    public ItemStack getActivationStackLeftHand() {
        return shouldBeInLeftHand;
    }

    public void setActivationStackLeftHand(ItemStack stack) {
        this.shouldBeInLeftHand = stack;
    }

    public ItemStack getActivationStackRightHand() {
        return shouldBeInRightHand;
    }

    public void setActivationStackRightHand(ItemStack stack) {
        this.shouldBeInRightHand = stack;
    }

    public List<ITextComponent> getAdditionalInfo() {
        List<ITextComponent> list = new ArrayList<>();

        DimensionType dimType = this.world.getDimension().getType();

        list.add(TextUtil.wrap("Machine name: " + getClass().getSimpleName()));
        list.add(TextUtil.wrap("Machine position: " + TextUtil.constructPositionString(dimType, pos)));
        list.add(TextUtil.wrap("Start position: " + TextUtil.constructPositionString(dimType, startPos)));
        list.add(TextUtil.wrap("Working position: " + TextUtil.constructPositionString(dimType, workingPos)));
        list.add(TextUtil.wrap("One operation cost: " + oneOperationCost));
        list.add(TextUtil.wrap("Fuel left: " + ticks));
        list.add(TextUtil.wrap("Max fuel: " + maxTicks));

        return list;
    }
}