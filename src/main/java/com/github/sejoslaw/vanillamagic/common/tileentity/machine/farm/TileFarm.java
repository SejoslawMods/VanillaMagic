package com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm;

import com.github.sejoslaw.vanillamagic.api.event.EventFarm;
import com.github.sejoslaw.vanillamagic.api.exception.NotInventoryException;
import com.github.sejoslaw.vanillamagic.api.inventory.InventoryWrapper;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IFarm;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IHarvestResult;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.inventory.InventoryHelper;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.TileMachine;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.core.VMLogger;
import com.github.sejoslaw.vanillamagic.core.VMTileEntities;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import org.apache.logging.log4j.Level;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TileFarm extends TileMachine implements IFarm {
    public static final String REGISTRY_NAME = TileFarm.class.getName();

    private int farmSaplingReserveAmount = 32;

    public TileFarm() {
        super(VMTileEntities.FARM);
    }

    public void init() {
        super.init();

        if (this.startPos == null) {
            this.startPos = new BlockPos(pos.getX() + radius, pos.getY(), pos.getZ() + radius);
        }

        if (this.chestPosInput == null) {
            this.chestPosInput = pos.offset(Direction.UP);
        }

        if (this.chestPosOutput == null) {
            this.chestPosOutput = pos.offset(Direction.DOWN);
        }

        try {
            if (this.inventoryInput == null) {
                this.inventoryInput = new InventoryWrapper(world, this.chestPosInput);
            }

            if (this.inventoryOutput == null) {
                this.inventoryOutput = new InventoryWrapper(world, this.chestPosOutput);
            }
        } catch (NotInventoryException e) {
            VMLogger.log(Level.ERROR, this.getClass().getSimpleName() + " - error when converting to IInventory at position: " + e.position.toString());
        }
    }

    public boolean checkSurroundings() {
        try {
            if (this.chestPosInput == null) {
                this.chestPosInput = pos.offset(Direction.UP);
            }

            if (this.chestPosOutput == null) {
                this.chestPosOutput = pos.offset(Direction.DOWN);
            }

            return (this.world.getTileEntity(chestPosInput) instanceof IInventory) && (this.world.getTileEntity(chestPosOutput) instanceof IInventory);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void doWork() {
        this.doTick();

        EventUtil.postEvent(new EventFarm.Work(this, world, pos));
    }

    public boolean tillBlock(BlockPos plantingLocation) {
        BlockPos dirtLoc = plantingLocation.offset(Direction.DOWN);
        Block dirtBlock = getBlock(dirtLoc);

        if (isTillable(dirtBlock)) {
            if (!hasHoe()) {
                return false;
            }

            damageHoe(1, dirtLoc);
            world.setBlockState(dirtLoc, Blocks.FARMLAND.getDefaultState());

            SoundType soundType = world.getBlockState(plantingLocation).getSoundType();
            world.playSound(dirtLoc.getX() + 0.5F, dirtLoc.getY() + 0.5F, dirtLoc.getZ() + 0.5F,
                    SoundEvents.BLOCK_GRASS_STEP, SoundCategory.BLOCKS,
                    soundType.volume, soundType.pitch, false);

            return true;
        } else {
            return dirtBlock == Blocks.FARMLAND;
        }
    }

    public int getMaxLootingValue() {
        int result = 0;
        IInventory inv = getInputInventory().getInventory();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);

            int level = getLooting(stack);

            if (level > result) {
                result = level;
            }
        }
        return result;
    }

    public boolean hasHoe() {
        return hasTool(ToolType.HOE);
    }

    public boolean hasAxe() {
        return hasTool(ToolType.AXE);
    }

    public boolean hasShears() {
        return hasTool(ToolType.SHEARS);
    }

    public void damageHoe(int damage, BlockPos pos) {
        damageTool(ToolType.HOE, pos, damage);
    }

    public void damageAxe(Block block, BlockPos pos) {
        damageTool(ToolType.AXE, pos, 1);
    }

    public void damageShears(Block block, BlockPos pos) {
        damageTool(ToolType.SHEARS, pos, 1);
    }

    public boolean hasTool(ToolType type) {
        return getTool(type) != null;
    }

    public ItemStack getTool(ToolType type) {
        IInventory inv = getInputInventory().getInventory();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);

            if (!ItemStackUtil.isNullStack(stack) && (type.itemMatches(stack) && ItemStackUtil.getStackSize(stack) > 0)) {
                return stack;
            }
        }

        return null;
    }

    public void damageTool(ToolType type, BlockPos pos, int damage) {
        ItemStack tool = getTool(type);

        if (ItemStackUtil.isNullStack(tool)) {
            return;
        }

        boolean canDamage = canDamage(tool);

        if (type == ToolType.AXE) {
            tool.setDamage(tool.getDamage() + damage);
        } else if (type == ToolType.HOE) {
            BlockState state = world.getBlockState(pos);
            Block stateBlock = state.getBlock();

            if (!world.isAirBlock(pos.up())) {
                return;
            }

            if (!world.isRemote && isTillable(stateBlock)) {
                world.setBlockState(pos, Blocks.FARMLAND.getDefaultState(), 11);
                tool.setDamage(tool.getDamage() + damage);
            }
        } else if (canDamage) {
            tool.setDamage(tool.getDamage() + damage);
        }

        if (ItemStackUtil.getStackSize(tool) == 0 || (canDamage && tool.getDamage() >= tool.getMaxDamage())) {
            destroyTool(type);
        }
    }

    private boolean canDamage(ItemStack stack) {
        return !ItemStackUtil.isNullStack(stack) && stack.isDamageable() && stack.getItem().isDamageable();
    }

    private void destroyTool(ToolType type) {
        IInventory inv = getInputInventory().getInventory();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);

            if (type.itemMatches(stack) && ItemStackUtil.getStackSize(stack) == 0) {
                inv.setInventorySlotContents(i, ItemStackUtil.NULL_STACK);
                markDirty();
                return;
            }
        }
    }

    private int getLooting(ItemStack stack) {
        return Math.max(EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack), EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack));
    }

    public Block getBlock(int x, int y, int z) {
        return getBlock(new BlockPos(x, y, z));
    }

    public Block getBlock(BlockPos posIn) {
        return getBlockState(posIn).getBlock();
    }

    public BlockState getBlockState(BlockPos posIn) {
        return world.getBlockState(posIn);
    }

    public boolean isAir(BlockPos pos) {
        return getBlockState(pos).isAir(this.world, pos);
    }

    protected void doTick() {
        workingPos = getNextCoord();
        BlockState bs = getBlockState(workingPos);
        Block block = bs.getBlock();

        if (isAir(workingPos)) {
            bs = getBlockState(workingPos);
            block = bs.getBlock();
        }

        if (isOutputFull() || isAir(workingPos)) {
            return;
        }

        IHarvestResult harvest = FarmersRegistry.harvestBlock(this, workingPos, block, bs);

        if (harvest == null || harvest.getDrops() == null) {
            return;
        }

        for (ItemEntity ei : harvest.getDrops()) {
            if (ei != null) {
                insertHarvestDrop(ei);
            }
        }
    }

    private boolean isOutputFull() {
        return !this.inventoryOutputHasSpace();
    }

    public boolean hasSeed(ItemStack seeds) {
        IInventory inv = getInputInventory().getInventory();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);

            if (!ItemStackUtil.isNullStack(stack) && ItemStackUtil.getStackSize(stack) > 1 && stack.isItemEqual(seeds)) {
                return true;
            }
        }

        return false;
    }

    public int isLowOnSaplings(BlockPos pos) {
        IInventory inv = getInputInventory().getInventory();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);

            if (!ItemStackUtil.isNullStack(stack)) {
                Block blockSapling = Block.getBlockFromItem(stack.getItem());

                if ((blockSapling instanceof SaplingBlock)) {
                    return 90 * (farmSaplingReserveAmount - (ItemStackUtil.isNullStack(stack) ? 0 : ItemStackUtil.getStackSize(stack))) / farmSaplingReserveAmount;
                }
            }
        }

        return 0;
    }

    public ItemStack takeSeedFromSupplies(ItemStack stack, BlockPos forBlock) {
        return takeSeedFromSupplies(stack, forBlock, true);
    }

    public ItemStack takeSeedFromSupplies(ItemStack stack, BlockPos forBlock, boolean matchMetadata) {
        if (ItemStackUtil.isNullStack(stack) || forBlock == null) {
            return ItemStackUtil.NULL_STACK;
        }

        IInventory inv = getInputInventory().getInventory();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack invStack = inv.getStackInSlot(i);

            if (!ItemStackUtil.isNullStack(invStack)
                    && (matchMetadata ? invStack.isItemEqual(stack) : invStack.getItem() == stack.getItem())
                    && (ItemStackUtil.getStackSize(stack) > 1)) {
                ItemStack result = stack.copy();
                ItemStackUtil.setStackSize(result, 1);
                stack = stack.copy();
                ItemStackUtil.decreaseStackSize(stack, 1);

                if (ItemStackUtil.getStackSize(stack) == 0) {
                    stack = ItemStackUtil.NULL_STACK;
                }

                this.getInputInventory().getInventory().setInventorySlotContents(i, stack);
                return result;
            }
        }

        return ItemStackUtil.NULL_STACK;
    }

    private void insertHarvestDrop(Entity entity) {
        if (world.isRemote && !(entity instanceof ItemEntity) && !entity.isAlive()) {
            return;
        }

        ItemEntity item = (ItemEntity) entity;
        ItemStack stack = item.getItem().copy();
        int numInserted = insertResult(stack);
        ItemStackUtil.decreaseStackSize(stack, numInserted);
        item.setItem(stack);

        if (ItemStackUtil.getStackSize(stack) == 0) {
            item.remove();
        }
    }

    private int insertResult(ItemStack stack) {
        ItemStack left = InventoryHelper.putStackInInventoryAllSlots(getOutputInventory().getInventory(), stack, Direction.DOWN);

        if (ItemStackUtil.isNullStack(left)) {
            return 0;
        }

        return ItemStackUtil.getStackSize(left);
    }

    private BlockPos getNextCoord() {
        int size = radius;
        BlockPos loc = getPos();

        if (workingPos == null) {
            workingPos = new BlockPos(loc.getX() - size, loc.getY(), loc.getZ() - size);
            return workingPos;
        }

        int nextX = workingPos.getX() + 1;
        int nextZ = workingPos.getZ();

        if (nextX > loc.getX() + size) {
            nextX = loc.getX() - size;
            nextZ += 1;

            if (nextZ > loc.getZ() + size) {
                nextX = loc.getX() - size;
                nextZ = loc.getZ() - size;
            }
        }

        return workingPos = new BlockPos(nextX, workingPos.getY(), nextZ);
    }

    public Direction getOutputDirection() {
        return Direction.DOWN;
    }

    public void clearInputInventory() {
        IInventory inv = getInputInventory().getInventory();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);

            if (!ItemStackUtil.isNullStack(stack) && (ItemStackUtil.getStackSize(stack) <= 0)) {
                inv.setInventorySlotContents(i, ItemStackUtil.NULL_STACK);
            }
        }
    }

    public List<ITextComponent> getAdditionalInfo() {
        List<ITextComponent> list = super.getAdditionalInfo();
        list.add(TextUtil.wrap("Radius: " + radius));
        return list;
    }

    public boolean isTillable(Block block) {
        return block == Blocks.GRASS
                || block == Blocks.GRASS_PATH
                || block == Blocks.DIRT
                || block == Blocks.COARSE_DIRT
                || block == Blocks.PODZOL;
    }
}