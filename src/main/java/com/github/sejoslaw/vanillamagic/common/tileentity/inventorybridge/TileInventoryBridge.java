package com.github.sejoslaw.vanillamagic.common.tileentity.inventorybridge;

import com.github.sejoslaw.vanillamagic.api.event.EventInventoryBridge;
import com.github.sejoslaw.vanillamagic.api.exception.NotInventoryException;
import com.github.sejoslaw.vanillamagic.api.inventory.IInventoryWrapper;
import com.github.sejoslaw.vanillamagic.api.inventory.InventoryWrapper;
import com.github.sejoslaw.vanillamagic.api.tileentity.inventorybridge.IInventoryBridge;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.inventory.InventoryHelper;
import com.github.sejoslaw.vanillamagic.common.item.VMItems;
import com.github.sejoslaw.vanillamagic.common.tileentity.CustomTileEntity;
import com.github.sejoslaw.vanillamagic.common.util.*;
import com.github.sejoslaw.vanillamagic.core.VMTileEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TileInventoryBridge extends CustomTileEntity implements IInventoryBridge {
    public static final String REGISTRY_NAME = TileInventoryBridge.class.getName();

    /*
     * Input Inventory.
     */
    @Nullable
    protected IInventoryWrapper inputInvWrapper;

    /**
     * Inventory under this Tile.
     */
    @Nullable
    protected IInventoryWrapper outputInvWrapper;

    protected int inputInvX, inputInvY, inputInvZ, inputInvDim;
    protected int outputInvX, outputInvY, outputInvZ, outputInvDim;

    public TileInventoryBridge() {
        super(VMTileEntities.INVENTORY_BRIDGE);
    }

    @Nullable
    public IInventoryWrapper getInputInventory() {
        return inputInvWrapper;
    }

    @Nullable
    public IInventoryWrapper getOutputInventory() {
        return outputInvWrapper;
    }

    public List<ITextComponent> getAdditionalInfo() {
        List<ITextComponent> list = super.getAdditionalInfo();
        list.add(TextUtil.wrap("Input:  X=" + inputInvX + ", Y=" + inputInvY + ", Z=" + inputInvZ + ", Dim=" + inputInvDim));
        list.add(TextUtil.wrap("Output: X=" + outputInvX + ", Y=" + outputInvY + ", Z=" + outputInvZ + ", Dim=" + outputInvDim));
        return list;
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.putInt("inputInvX", inputInvX);
        compound.putInt("inputInvY", inputInvY);
        compound.putInt("inputInvZ", inputInvZ);
        compound.putInt("inputInvDim", inputInvDim);

        compound.putInt("outputInvX", outputInvX);
        compound.putInt("outputInvY", outputInvY);
        compound.putInt("outputInvZ", outputInvZ);
        compound.putInt("outputInvDim", outputInvDim);

        return compound;
    }

    public void deserializeNBT(CompoundNBT compound) {
        inputInvX = compound.getInt("inputInvX");
        inputInvY = compound.getInt("inputInvY");
        inputInvZ = compound.getInt("inputInvZ");
        inputInvDim = compound.getInt("inputInvDim");
        BlockPos inputPos = new BlockPos(inputInvX, inputInvY, inputInvZ);

        try {
            inputInvWrapper = new InventoryWrapper(WorldUtil.getWorld(this.world.getServer(), inputInvDim), inputPos);
        } catch (NotInventoryException e) {
            BlockPosUtil.printCoords(Level.ERROR, "NotInventoryException at: ", inputPos);
            e.printStackTrace();
        }

        outputInvX = compound.getInt("outputInvX");
        outputInvY = compound.getInt("outputInvY");
        outputInvZ = compound.getInt("outputInvZ");
        outputInvDim = compound.getInt("outputInvDim");
        BlockPos outputPos = new BlockPos(outputInvX, outputInvY, outputInvZ);

        try {
            outputInvWrapper = new InventoryWrapper(WorldUtil.getWorld(this.world.getServer(), outputInvDim), outputPos);
        } catch (NotInventoryException e) {
            BlockPosUtil.printCoords(Level.ERROR, "NotInventoryException at: ", outputPos);
            e.printStackTrace();
        }
    }

    /**
     * Sets the input position based on the ItemInventorySelector NBT saved data.
     */
    public void setPositionFromSelector(PlayerEntity player) throws NotInventoryException {
        setPositionFromSelector(player.inventory);
    }

    public void setPositionFromSelector(PlayerInventory invPlayer) throws NotInventoryException {
        setPositionFromSelector(invPlayer.mainInventory);
    }

    public void setPositionFromSelector(NonNullList<ItemStack> mainInventory) throws NotInventoryException {
        for (ItemStack currentCheckingStack : mainInventory) {
            CompoundNBT currentCheckingStackTag = currentCheckingStack.getOrCreateTag();

            if (!VMItems.isCustomItem(currentCheckingStack, VMItems.INVENTORY_SELECTOR) || (currentCheckingStackTag == null)) {
                continue;
            }

            BlockPos savedPosition = NBTUtil.getBlockPosDataFromNBT(currentCheckingStackTag);
            MinecraftServer server = this.world.getServer();
            World world = WorldUtil.getWorld(server, currentCheckingStackTag.getInt(NBTUtil.NBT_DIMENSION));

            inputInvWrapper = new InventoryWrapper(world, savedPosition);

            inputInvX = savedPosition.getX();
            inputInvY = savedPosition.getY();
            inputInvZ = savedPosition.getZ();

            inputInvDim = world.dimension.getType().getId();
        }
    }

    public void setOutputInventory(World world, BlockPos pos) throws NotInventoryException {
        outputInvWrapper = new InventoryWrapper(world, pos);
        outputInvX = pos.getX();
        outputInvY = pos.getY();
        outputInvZ = pos.getZ();
        outputInvDim = world.dimension.getType().getId();
    }

    public Direction getInputFacing() {
        return Direction.UP;
    }

    /**
     * Currently transporting slot
     */
    int slotNumber = 0;

    public void update() {
        if (inputInvWrapper == null) {
            return;
        }

        IInventory inv = inputInvWrapper.getInventory();

        if (slotNumber >= inv.getSizeInventory()) {
            slotNumber = 0;
        }

        ItemStack transportingStack = inv.getStackInSlot(slotNumber);

        if (ItemStackUtil.isNullStack(transportingStack)) {
            slotNumber++;
            return;
        }

        ItemStack leftItems = InventoryHelper.putStackInInventoryAllSlots(outputInvWrapper.getInventory(), transportingStack, getInputFacing());
        inv.setInventorySlotContents(slotNumber, leftItems);
        slotNumber++;
        EventUtil.postEvent(new EventInventoryBridge(this, world, pos));
    }
}
