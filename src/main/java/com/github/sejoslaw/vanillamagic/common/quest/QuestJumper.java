package com.github.sejoslaw.vanillamagic.common.quest;

import com.github.sejoslaw.vanillamagic.api.event.EventJumper;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.util.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestJumper extends Quest {
    private static final ItemStack REQUIRED_RIGHT_HAND = new ItemStack(Items.COMPASS);
    private static final ItemStack REQUIRED_LEFT_HAND = new ItemStack(Items.BOOK);
    private static final ItemStack POSITION_HOLDER = new ItemStack(Items.ENCHANTED_BOOK);

    @SubscribeEvent
    public void saveBlockPosToBook(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        ItemStack leftHand = player.getHeldItemOffhand();

        if (!ItemStackUtil.checkItemsInHands(player, REQUIRED_LEFT_HAND, REQUIRED_RIGHT_HAND) || (ItemStackUtil.getStackSize(leftHand) > 1) || !player.isSneaking() || !canPlayerGetQuest(player)) {
            return;
        }

        World world = event.getWorld();
        BlockPos posToSave = event.getPos().offset(event.getFace());

        if (EventUtil.postEvent(new EventJumper.SavePosition.Before(player, world, posToSave))) {
            return;
        }

        player.setHeldItem(Hand.OFF_HAND, writeDataToBook(world.getDimension().getType(), posToSave));

        if (!hasQuest(player)) {
            addStat(player);
        }

        EntityUtil.addChatComponentMessageNoSpam(TextUtil.wrap("Position saved: " + TextUtil.constructPositionString(world.getDimension().getType(), posToSave)));
        EventUtil.postEvent(new EventJumper.SavePosition.After(player, world, posToSave));
    }

    public static ItemStack writeDataToBook(DimensionType dimensionType, BlockPos pos) {
        ItemStack bookWithData = POSITION_HOLDER.copy();
        bookWithData.setDisplayName(TextUtil.wrap("Jumper's Book: " + TextUtil.constructPositionString(dimensionType, pos)));
        bookWithData.setTag(NBTUtil.setBlockPosDataToNBT(bookWithData.getOrCreateTag(), pos, dimensionType));
        return bookWithData;
    }

    @SubscribeEvent
    public void teleportPlayer(PlayerInteractEvent.RightClickItem event) {
        PlayerEntity player = event.getPlayer();
        ItemStack rightHand = player.getHeldItemMainhand();

        if (!ItemStackUtil.checkItemsInHands(player, REQUIRED_RIGHT_HAND, POSITION_HOLDER) || !player.isSneaking()) {
            return;
        }

        CompoundNBT bookData = rightHand.getOrCreateTag();
        BlockPos teleportPos = NBTUtil.getBlockPosDataFromNBT(bookData);

        if (teleportPos == null) {
            return;
        }

        int dimId = NBTUtil.getDimensionFromNBT(bookData);

        if (EventUtil.postEvent(new EventJumper.Teleport.Before(player, event.getWorld(), teleportPos, dimId))) {
            return;
        }

        MinecraftServer server = player.getServer();
        World world = WorldUtil.getWorld(server, dimId);
        TeleportUtil.teleportEntity(player, teleportPos, world);

        EntityUtil.addChatComponentMessageNoSpam(TextUtil.wrap("Teleported to: " + TextUtil.constructPositionString(world.getDimension().getType(), teleportPos)));
        EventUtil.postEvent(new EventJumper.Teleport.After(player, event.getWorld(), teleportPos, dimId));
    }

    @SubscribeEvent
    public void showSavedPosTooltip(ItemTooltipEvent event) {
        ItemStack jumperBookStack = event.getItemStack();
        CompoundNBT jumperBookTagCompound = jumperBookStack.getOrCreateTag();

        BlockPos teleportPos = NBTUtil.getBlockPosDataFromNBT(jumperBookTagCompound);

        if (teleportPos == null) {
            return;
        }

        int dimId = NBTUtil.getDimensionFromNBT(jumperBookTagCompound);
        String info = TextUtil.constructPositionString(DimensionType.getById(dimId), teleportPos);
        event.getToolTip().add(TextUtil.wrap("Jumper's Book: " + info));
    }
}