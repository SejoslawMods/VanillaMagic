package com.github.sejoslaw.vanillamagic.common.tileentity.inventorybridge;

import com.github.sejoslaw.vanillamagic.api.exception.NotInventoryException;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.handler.CustomTileEntityHandler;
import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.common.quest.Quest;
import com.github.sejoslaw.vanillamagic.common.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestInventoryBridge extends Quest {
    /**
     * ItemStack required in left hand to create {@link TileInventoryBridge}
     */
    public final ItemStack requiredLeftHand = new ItemStack(Blocks.WHITE_STAINED_GLASS);

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        ItemStack rightHand = player.getHeldItemMainhand();
        ItemStack leftHand = player.getHeldItemOffhand();

        if (ItemStackUtil.isNullStack(rightHand)
                || !WandRegistry.areWandsEqual(WandRegistry.WAND_BLAZE_ROD.getWandStack(), rightHand)
                || ItemStackUtil.isNullStack(leftHand) || !ItemStack.areItemsEqual(leftHand, requiredLeftHand)) {
            return;
        }

        if (!player.isSneaking()) {
            return;
        }

        World world = event.getWorld();
        BlockPos clickedPos = event.getPos();
        TileEntity clickedInventory = world.getTileEntity(clickedPos);

        if (!(clickedInventory instanceof IInventory)) {
            return;
        }

        this.checkQuestProgress(player);

        if (!hasQuest(player)) {
            return;
        }

        TileInventoryBridge tile = new TileInventoryBridge();
        tile.setup(player.world, clickedPos.offset(Direction.UP));

        try {
            tile.setPositionFromSelector(player);
        } catch (NotInventoryException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println(e.position.toString());
            return;
        }

        try {
            tile.setOutputInventory(world, clickedPos);
        } catch (NotInventoryException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println(e.position.toString());
            return;
        }

        if (!CustomTileEntityHandler.addCustomTileEntity(tile, player.world)) {
            return;
        }

        EntityUtil.addChatComponentMessageNoSpam(TextUtil.wrap(tile.getClass().getSimpleName() + " added"));
        ItemStackUtil.decreaseStackSize(leftHand, 1);

        if (ItemStackUtil.getStackSize(leftHand) != 0) {
            return;
        }

        player.setItemStackToSlot(EquipmentSlotType.OFFHAND, null);
    }

    @SubscribeEvent
    public void onBridgeDestroyed(BlockEvent.BreakEvent event) {
        BlockPos inventoryPos = event.getPos();
        World world = event.getWorld().getWorld();
        TileEntity inventoryTile = world.getTileEntity(inventoryPos);

        if (!(inventoryTile instanceof IInventory)) {
            return;
        }

        BlockPos customTilePos = inventoryPos.offset(Direction.UP);
        CustomTileEntityHandler.removeCustomTileEntityAndSendInfoToPlayer(world, customTilePos, event.getPlayer());
    }
}