package com.github.sejoslaw.vanillamagic.core;

import com.github.sejoslaw.vanillamagic.api.quest.QuestList;
import com.github.sejoslaw.vanillamagic.api.tileentity.ICustomTileEntity;
import com.github.sejoslaw.vanillamagic.api.util.IAdditionalInfoProvider;
import com.github.sejoslaw.vanillamagic.common.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.common.util.QuestUtil;
import com.github.sejoslaw.vanillamagic.handler.CustomTileEntityHandler;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

/**
 * VM Debug Tools. Right-Click with Clock to show additional information, 64x
 * Command Block in left and right hand and right-click in air to get all
 * Quests, etc.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMDebug {
    /**
     * ItemStack required in offhand to enable debug / get all Quests
     */
    public static final ItemStack DEBUG_OFF_HAND_ITEMSTACK = new ItemStack(Blocks.COMMAND_BLOCK, 64);

    /**
     * ItemStack required in mainhand to enable debug / get all Quests
     */
    public static final ItemStack DEBUG_MAIN_HAND_ITEMSTACK = DEBUG_OFF_HAND_ITEMSTACK;

    /**
     * 64x Command Block in left and right hand and right-click in air to get all
     * the Quests
     */
    @SubscribeEvent
    public void activateDebug(RightClickItem event) {
        PlayerEntity player = event.getPlayer();

        if (ItemStackUtil.checkItemsInHands(player, DEBUG_OFF_HAND_ITEMSTACK, DEBUG_MAIN_HAND_ITEMSTACK)) {
            activate(player);
        }
    }

    /**
     * Give all Quests to specified Player.
     */
    public static void activate(PlayerEntity player) {
        if (!(player instanceof ServerPlayerEntity)) {
            return;
        }

        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        Iterable<Advancement> advancements = Minecraft.getInstance().getIntegratedServer().getAdvancementManager().getAllAdvancements();

        advancements.forEach(advancement -> {
            AdvancementProgress progress = serverPlayer.getAdvancements().getProgress(advancement);
            progress.getRemaningCriteria().forEach(criterion -> serverPlayer.getAdvancements().grantCriterion(advancement, criterion));
        });

        QuestList.getQuests().forEach(quest -> QuestUtil.addStat(serverPlayer, quest));
    }

    int showTime = 1; // hue hue

    /**
     * Method used for showing additional information of the CustomTileEntitys. <br>
     * Right-click with Clock on IAdditionalInfoProvider.
     */
    @SubscribeEvent
    public void showTileEntityInfo(RightClickBlock event) {
        World world = event.getWorld();
        if (world.isRemote) {
            return;
        }

        if (showTime == 1) {
            showTime++;
        } else {
            showTime = 1;
            return;
        }

        PlayerEntity player = event.getPlayer();
        ItemStack stackRightHand = player.getHeldItemMainhand();
        if (ItemStackUtil.isNullStack(stackRightHand)) {
            return;
        }

        if (stackRightHand.getItem().equals(Items.CLOCK)) {
            BlockPos tilePos = event.getPos();
            ICustomTileEntity customTile = CustomTileEntityHandler.getCustomTileEntity(tilePos, world);
            if (customTile == null) {
                return;
            }

            TileEntity tile = customTile.getTileEntity();
            if ((tile instanceof ICustomTileEntity)
                    && (customTile.getTileEntity() instanceof IAdditionalInfoProvider)) {
                List<ITextComponent> info = ((IAdditionalInfoProvider) customTile.getTileEntity()).getAdditionalInfo();
                EntityUtil.addChatComponentMessageNoSpam(player, info.toArray(new ITextComponent[info.size()]));
            }
        }
    }
}