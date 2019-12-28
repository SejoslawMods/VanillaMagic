package com.github.sejoslaw.vanillamagic.common.tileentity.blockabsorber;

import com.github.sejoslaw.vanillamagic.api.event.EventBlockAbsorber;
import com.github.sejoslaw.vanillamagic.api.tileentity.blockabsorber.IBlockAbsorber;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.inventory.InventoryHelper;
import com.github.sejoslaw.vanillamagic.common.tileentity.CustomTileEntity;
import com.github.sejoslaw.vanillamagic.common.util.BlockUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.core.VMTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TileBlockAbsorber extends CustomTileEntity implements IBlockAbsorber {
    public static final String REGISTRY_NAME = TileBlockAbsorber.class.getName();

    public TileBlockAbsorber() {
        super(VMTileEntities.BLOCK_ABSORBER);
    }

    public List<ITextComponent> getAdditionalInfo() {
        List<ITextComponent> list = super.getAdditionalInfo();
        HopperTileEntity connectedHopper = getConnectedHopper();
        BlockPos connectedHopperPos = connectedHopper.getPos();
        list.add(TextUtil.wrap("Saved Hopper position: X=" + connectedHopperPos.getX() + ", Y=" + connectedHopperPos.getY() + ", Z=" + connectedHopperPos.getZ()));
        list.add(TextUtil.wrap("Has connected Hopper: " + (connectedHopper != null)));
        return list;
    }

    /**
     * On each tick this {@link TileEntity} will check for block which is on this position and try to place it in bottom Hopper.
     */
    public void tick() {
        HopperTileEntity connectedHopper = getConnectedHopper();
        BlockState thisState = world.getBlockState(pos);

        if (BlockUtil.areEqual(thisState.getBlock(), Blocks.AIR)) {
            return;
        }

        TileEntity tileAtThisPos = world.getTileEntity(pos);
        if ((tileAtThisPos != null) && (tileAtThisPos instanceof IInventory)) {
            IInventory inv = (IInventory) tileAtThisPos;

            try {
                if (!InventoryHelper.isInventoryEmpty(inv, Direction.DOWN)) {
                    for (int i = 0; i < inv.getSizeInventory(); ++i) {
                        ItemStack stackInSlot = inv.getStackInSlot(i);
                        ItemStack leftItems = InventoryHelper.putStackInInventoryAllSlots(connectedHopper, stackInSlot, getInputFacing());
                        inv.setInventorySlotContents(i, leftItems);
                    }
                }
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
                return;
            }

            try {
                if (!InventoryHelper.isInventoryEmpty(inv, Direction.DOWN)) {
                    return;
                }
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }

        ItemStack thisBlock = new ItemStack(thisState.getBlock());

        if (thisBlock.getItem() == null) {
            return;
        }

        ItemStack leftItems = InventoryHelper.putStackInInventoryAllSlots(connectedHopper, thisBlock, getInputFacing());

        if (ItemStackUtil.isNullStack(leftItems)) {
            MinecraftForge.EVENT_BUS.post(new EventBlockAbsorber(this, world, pos, connectedHopper));
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    public Direction getInputFacing() {
        return Direction.UP;
    }

    public HopperTileEntity getConnectedHopper() {
        return (HopperTileEntity) world.getTileEntity(pos.offset(Direction.DOWN));
    }
}