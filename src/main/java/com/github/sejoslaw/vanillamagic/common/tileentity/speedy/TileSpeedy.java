package com.github.sejoslaw.vanillamagic.common.tileentity.speedy;

import com.github.sejoslaw.vanillamagic.api.event.EventSpeedy;
import com.github.sejoslaw.vanillamagic.api.exception.NotInventoryException;
import com.github.sejoslaw.vanillamagic.api.inventory.IInventoryWrapper;
import com.github.sejoslaw.vanillamagic.api.inventory.InventoryWrapper;
import com.github.sejoslaw.vanillamagic.api.math.Box;
import com.github.sejoslaw.vanillamagic.api.tileentity.speedy.ISpeedy;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.config.VMConfig;
import com.github.sejoslaw.vanillamagic.core.VMItems;
import com.github.sejoslaw.vanillamagic.common.tileentity.CustomTileEntity;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.common.util.NBTUtil;
import com.github.sejoslaw.vanillamagic.core.VMTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TileSpeedy extends CustomTileEntity implements ISpeedy {
    public static final String REGISTRY_NAME = TileSpeedy.class.getName();

    /**
     * How many ticks in one block.
     */
    protected int ticks;
    /**
     * Working space box.
     *
     * @see {@link Box}
     */
    protected Box radiusBox;
    /**
     * Size of the Speedy.
     */
    protected int size;

    public TileSpeedy() {
        super(VMTileEntities.SPEEDY);
    }

    public void init(World world, BlockPos pos) {
        super.init(world, pos);

        this.ticks = VMConfig.TILE_SPEEDY_TICKS.get();
        this.size = VMConfig.TILE_SPEEDY_SIZE.get();

        this.setBox();

        world.addParticle(ParticleTypes.END_ROD, pos.getX(), pos.getY(), pos.getZ(), 100, 100, 100);
    }

    private void setBox() {
        this.radiusBox = new Box(this.pos);

        this.radiusBox.resizeX(this.size);
        this.radiusBox.resizeY(1);
        this.radiusBox.resizeZ(this.size);
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public Box getRadiusBox() {
        return radiusBox;
    }

    public void setRadiusBox(Box radiusBox) {
        this.radiusBox = radiusBox;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public IInventoryWrapper getInventoryWithCrystal() {
        try {
            return new InventoryWrapper(world, getPos().offset(Direction.UP));
        } catch (NotInventoryException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean containsCrystal() {
        IInventory inv = getInventoryWithCrystal().getInventory();

        if (inv != null) {
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                ItemStack stack = inv.getStackInSlot(i);

                if (VMItems.isCustomItem(stack, VMItems.ACCELERATION_CRYSTAL)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void tick() {
        if (this.ticks <= 0) {
            return;
        }

        if (this.containsCrystal()) {
            this.tickNeighbors();
        }

        EventUtil.postEvent(new EventSpeedy(this, world, pos));
    }

    private void tickNeighbors() {
        for (int x = radiusBox.xMin; x <= radiusBox.xMax; ++x) {
            for (int y = radiusBox.yMin; y <= radiusBox.yMax; ++y) {
                for (int z = radiusBox.zMin; z <= radiusBox.zMax; ++z) {
                    this.tickBlock(new BlockPos(x, y, z));
                }
            }
        }
    }

    private void tickBlock(BlockPos pos) {
        if (!world.isAirBlock(pos)) {
            BlockState blockState = this.world.getBlockState(pos);
            TileEntity tile = world.getTileEntity(pos);
            Random rand = new Random();

            for (int i = 0; i < ticks; ++i) {
                if (tile instanceof ITickableTileEntity) {
                    ((ITickableTileEntity) tile).tick();
                } else {
                    blockState.tick(world, pos, rand);
                }
            }
        }
    }

    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.putInt(NBTUtil.NBT_SPEEDY_TICKS, ticks);
        return tag;
    }

    public void read(CompoundNBT tag) {
        super.read(tag);
        this.ticks = tag.getInt(NBTUtil.NBT_SPEEDY_TICKS);
    }

    public List<ITextComponent> getAdditionalInfo() {
        List<ITextComponent> list = super.getAdditionalInfo();
        list.add(TextUtil.wrap("Size: " + size));
        list.add(TextUtil.wrap("Number of ticks to boost: " + ticks));
        list.add(TextUtil.wrap("Min pos: X=" + radiusBox.xMin + ", Y=" + radiusBox.yMin + ", Z=" + radiusBox.zMin));
        list.add(TextUtil.wrap("Max pos: X=" + radiusBox.xMax + ", Y=" + radiusBox.yMax + ", Z=" + radiusBox.zMax));
        return list;
    }

    public boolean isPrepared() {
        return this.containsCrystal();
    }
}