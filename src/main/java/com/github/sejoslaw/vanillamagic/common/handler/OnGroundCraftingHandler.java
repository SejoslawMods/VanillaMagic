package com.github.sejoslaw.vanillamagic.common.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class OnGroundCraftingHandler {
    public static class OnGroundCraftingEntry {
        public final ItemStack[] ingredients;
        public final ItemStack output;

        public OnGroundCraftingEntry(ItemStack output, ItemStack... ingredients) {
            this.output = output;
            this.ingredients = ingredients;
        }
    }

    public static final Set<OnGroundCraftingEntry> ENTRIES = new HashSet<>();

    @SubscribeEvent
    public void craftOnGround(PlayerInteractEvent.RightClickBlock event) {
        ItemStack rightHandStack = event.getItemStack();
        Direction faceClicked = event.getFace();

        if (rightHandStack.getItem() != Items.STICK || faceClicked == null) {
            return;
        }

        World world = event.getWorld();
        BlockPos clickedPos = event.getPos().offset(faceClicked);

        AxisAlignedBB aabb = new AxisAlignedBB(
                clickedPos.getX() - 0.5D,
                clickedPos.getY(),
                clickedPos.getZ() - 0.5D,
                clickedPos.getX() + 0.5D,
                clickedPos.getY(),
                clickedPos.getZ() + 0.5D
        );

        List<ItemEntity> itemsOnGround = world.getEntitiesWithinAABB(ItemEntity.class, aabb);
        ItemStack output = this.getOutput(itemsOnGround);

        if (output == null) {
            return;
        }

        itemsOnGround.forEach(Entity::remove);

        ItemEntity entity = new ItemEntity(world, clickedPos.getX(), clickedPos.getY(), clickedPos.getZ());
        entity.setItem(output);
        world.addEntity(entity);
    }

    private ItemStack getOutput(List<ItemEntity> itemsOnGround) {
        for (OnGroundCraftingEntry entry : ENTRIES) {
            if (this.containsAll(entry.ingredients, itemsOnGround)) {
                return entry.output.copy();
            }
        }

        return null;
    }

    private boolean containsAll(ItemStack[] ingredients, List<ItemEntity> itemsOnGround) {
        for (ItemEntity stackOnGround : itemsOnGround) {
            if (Arrays.stream(ingredients).noneMatch(ingredient -> ItemStack.areItemStacksEqual(ingredient, stackOnGround.getItem()))) {
                return false;
            }
        }

        return true;
    }

    public static void addRecipe(ItemStack output, ItemStack... ingredients) {
        ENTRIES.add(new OnGroundCraftingEntry(output, ingredients));
    }
}
