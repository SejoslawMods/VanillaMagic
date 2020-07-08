package com.github.sejoslaw.vanillamagic.common.handler;

import com.github.sejoslaw.vanillamagic.common.item.CustomItemRecipe;
import com.github.sejoslaw.vanillamagic.common.item.CustomItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class OnGroundCraftingHandler {
    @SubscribeEvent
    public void craftOnGround(PlayerInteractEvent.RightClickBlock event) {
        ItemStack rightHandStack = event.getItemStack();
        Direction faceClicked = event.getFace();

        if (rightHandStack.getItem() != Items.STICK || faceClicked == null) {
            return;
        }

        World world = event.getWorld();
        BlockPos clickedPos = event.getPos().offset(faceClicked);

        for (CustomItemRecipe recipe : CustomItemRegistry.RECIPES) {
            if (recipe.isValid(world, clickedPos)) {
                recipe.spawn(world, clickedPos);
                return;
            }
        }
    }
}
