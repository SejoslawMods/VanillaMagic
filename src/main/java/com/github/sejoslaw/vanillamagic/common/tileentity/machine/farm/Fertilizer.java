package com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm;

import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public enum Fertilizer {
    /**
     * Not a fertilizer. Using this handler class any item can be "used" as a
     * fertilizer. Meaning, fertilizing will always fail.
     */
    NONE(ItemStack.EMPTY) {
        public boolean apply(ItemStack stack, PlayerEntity player, World world, BlockPos pos) {
            return false;
        }
    },

    BONE_MEAL(new ItemStack(Items.WHITE_DYE, 1)) {
        public boolean apply(ItemStack stack, PlayerEntity player, World world, BlockPos pos) {
            ItemUseContext ctx = createItemUseContext(stack, player, world, pos);
            ActionResultType result = stack.onItemUse(ctx);
            return result == ActionResultType.SUCCESS;
        }
    },

    FORESTRY_FERTILIZER_COMPOUND(ForgeRegistries.ITEMS.getValue(new ResourceLocation("Forestry", "fertilizerCompound"))) {
        public boolean apply(ItemStack stack, PlayerEntity player, World world, BlockPos pos) {
            return BONE_MEAL.apply(stack, player, world, pos);
        }
    },

    BOTANIA_FLORAL_FERTILIZER(ForgeRegistries.ITEMS.getValue(new ResourceLocation("Botania", "fertilizer"))) {
        public boolean apply(ItemStack stack, PlayerEntity player, World world, BlockPos pos) {
            BlockPos below = pos.offset(Direction.DOWN);
            Block blockBelow = world.getBlockState(below).getBlock();

            if (blockBelow == Blocks.DIRT || blockBelow == Blocks.GRASS) {
                return BONE_MEAL.apply(stack, player, world, pos);
            }

            return false;
        }

        public boolean applyOnAir() {
            return true;
        }

        public boolean applyOnPlant() {
            return false;
        }
    },

    METALLURGY_FERTILIZER(ForgeRegistries.ITEMS.getValue(new ResourceLocation("Metallurgy", "fertilizer"))) {
        public boolean apply(ItemStack stack, PlayerEntity player, World world, BlockPos pos) {
            return BONE_MEAL.apply(stack, player, world, pos);
        }
    },

    GARDEN_CORE_COMPOST(ForgeRegistries.ITEMS.getValue(new ResourceLocation("GardenCore", "compost_pile"))) {
        public boolean apply(ItemStack stack, PlayerEntity player, World world, BlockPos pos) {
            return BONE_MEAL.apply(stack, player, world, pos);
        }
    },

    MAGICAL_CROPS_FERTILIZER(ForgeRegistries.ITEMS.getValue(new ResourceLocation("magicalcrops", "magicalcrops_MagicalCropFertilizer"))) {
        public boolean apply(ItemStack stack, PlayerEntity player, World world, BlockPos pos) {
            return BONE_MEAL.apply(stack, player, world, pos);
        }
    };

    private static final List<Fertilizer> VALID_FERTILIZERS = new ArrayList<>();

    private ItemStack stack;

    static {
        for (Fertilizer f : values()) {
            if (!ItemStackUtil.isNullStack(f.stack)) {
                VALID_FERTILIZERS.add(f);
            }
        }
    }

    private Fertilizer(Item item) {
        this(new ItemStack(item));
    }

    private Fertilizer(Block block) {
        this(new ItemStack(block));
    }

    private Fertilizer(ItemStack stack) {
        this.stack = ItemStackUtil.isNullStack(stack) ? null : stack;
    }

    /**
     * Returns the singleton instance for the fertilizer that was given as
     * parameter. If the given item is no fertilizer, it will return an instance of
     * Fertilizer.None.
     */
    public static Fertilizer getInstance(ItemStack stack) {
        for (Fertilizer fertilizer : VALID_FERTILIZERS) {
            if (fertilizer.matches(stack)) {
                return fertilizer;
            }
        }

        return NONE;
    }

    /**
     * Returns true if the given item can be used as fertilizer.
     */
    public static boolean isFertilizer(ItemStack stack) {
        return getInstance(stack) != NONE;
    }

    public boolean applyOnAir() {
        return false;
    }

    public boolean applyOnPlant() {
        return true;
    }

    protected boolean matches(ItemStack stack) {
        return ItemStack.areItemsEqualIgnoreDurability(this.stack, stack);
    }

    private static ItemUseContext createItemUseContext(ItemStack stack, PlayerEntity player, World world, BlockPos pos) {
        Vec3d hitVec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
        BlockRayTraceResult rayTraceResult = new BlockRayTraceResult(hitVec, Direction.UP, pos, false);
        return new ItemUseContext(player, Hand.MAIN_HAND, rayTraceResult);
    }

    /**
     * Tries to apply the given item on the given block using the type-specific
     * method. SFX is played on success.
     * <p>
     * If the item was successfully applied, the stackSize will be decreased if
     * appropriate. The caller will need to check for stackSize 0 and null the
     * inventory slot if needed.
     *
     * @return true if the fertilizer was applied
     */
    public abstract boolean apply(ItemStack stack, PlayerEntity player, World world, BlockPos pos);
}