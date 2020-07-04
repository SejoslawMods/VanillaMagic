package com.github.sejoslaw.vanillamagic.common.tileentity.blockabsorber;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.handler.CustomTileEntityHandler;
import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.common.quest.Quest;
import com.github.sejoslaw.vanillamagic.common.util.BlockUtil;
import com.github.sejoslaw.vanillamagic.common.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.IHopper;
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
public class QuestBlockAbsorber extends Quest {
    /**
     * ItemStack required in left hand to create {@link TileBlockAbsorber}
     */
    public final ItemStack requiredLeftHand = new ItemStack(Blocks.GLASS);

    @SubscribeEvent
    public void onRightClickHopper(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        ItemStack rightHand = player.getHeldItemMainhand();
        ItemStack leftHand = player.getHeldItemOffhand();

        if (ItemStackUtil.isNullStack(rightHand)
                || !WandRegistry.areWandsEqual(WandRegistry.WAND_BLAZE_ROD.getWandStack(), rightHand)
                || ItemStackUtil.isNullStack(leftHand) || !ItemStack.areItemsEqual(leftHand, requiredLeftHand)
                || !player.isSneaking()) {
            return;
        }

        BlockPos clickedPos = event.getPos();
        TileEntity clickedHopper = player.world.getTileEntity(clickedPos);

        if ((clickedHopper == null)
                || !(clickedHopper instanceof IHopper)
                || !(clickedHopper instanceof HopperTileEntity)) {
            return;
        }

        this.checkQuestProgress(player);

        if (!hasQuest(player)) {
            return;
        }

        TileBlockAbsorber tile = new TileBlockAbsorber();
        tile.setup(player.world, clickedPos.offset(Direction.UP));

        if (!CustomTileEntityHandler.addCustomTileEntity(tile, player.world)) {
            return;
        }

        EntityUtil.addChatComponentMessageNoSpam(player, TextUtil.wrap(tile.getClass().getSimpleName() + " added"));
        ItemStackUtil.decreaseStackSize(leftHand, 1);

        if (ItemStackUtil.getStackSize(leftHand) != 0) {
            return;
        }

        player.setItemStackToSlot(EquipmentSlotType.OFFHAND, null);
    }

    @SubscribeEvent
    public void onHopperDestroyed(BlockEvent.BreakEvent event) {
        BlockPos hopperPos = event.getPos();
        World world = event.getWorld().getWorld();
        BlockState hopperState = world.getBlockState(hopperPos);

        if (!BlockUtil.areEqual(hopperState.getBlock(), Blocks.HOPPER)) {
            return;
        }

        BlockPos customTilePos = hopperPos.offset(Direction.UP);
        CustomTileEntityHandler.removeCustomTileEntityAndSendInfoToPlayer(world, customTilePos, event.getPlayer());
    }
}