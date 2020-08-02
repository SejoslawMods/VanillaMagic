package com.github.sejoslaw.vanillamagic2.common.tileentities;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.items.IVMItem;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import com.github.sejoslaw.vanillamagic2.core.VMTiles;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMTileAccelerant extends VMTileEntity {
    private final int ticks;
    private final List<BlockPos> tickPoses = new ArrayList<>();

    private BlockPos invPos;
    private IVMItem item;

    public VMTileAccelerant() {
        super(VMTiles.ACCELERANT);

        this.ticks = VMForgeConfig.TILE_ACCELERANT_TICKS.get();
    }

    public void initialize(World world, BlockPos pos) {
        super.initialize(world, pos);

        this.invPos = this.getPos().offset(Direction.UP);
        this.item = ItemRegistry.ACCELERATION_CRYSTAL;

        int size = VMForgeConfig.TILE_ACCELERANT_SIZE.get();

        for (int x = this.getPos().getX() - size; x <= this.getPos().getX() + size; ++x) {
            for (int y = this.getPos().getY() - size; y <= this.getPos().getY() + size; ++y) {
                for (int z = this.getPos().getZ() - size; z <= this.getPos().getZ() + size; ++z) {
                    this.tickPoses.add(new BlockPos(x, y, z));
                }
            }
        }
    }

    public void tick() {
        if (!this.hasCrystal()) {
            return;
        }

        for (BlockPos pos : this.tickPoses) {
            WorldUtils.tick(this.getWorld(), pos, this.ticks, this.getWorld().rand);
        }
    }

    private boolean hasCrystal() {
        IInventory inv = WorldUtils.getInventory(this.getWorld(), this.invPos);

        if (inv == null) {
            return false;
        }

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);

            if (this.item.isVMItem(stack)) {
                return true;
            }
        }

        return false;
    }
}
