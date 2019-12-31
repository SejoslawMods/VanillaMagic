package com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.handler.CustomTileEntityHandler;
import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.common.quest.QuestMachineActivate;
import com.github.sejoslaw.vanillamagic.common.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestQuarry extends QuestMachineActivate {
    @SubscribeEvent
    public void startQuarry(PlayerInteractEvent.RightClickBlock event) {
        BlockPos quarryPos = event.getPos();
        PlayerEntity player = event.getPlayer();
        ItemStack itemInHand = player.getHeldItemMainhand();

        if (!player.isSneaking() || ItemStackUtil.isNullStack(itemInHand)
                || !itemInHand.getItem().equals(WandRegistry.WAND_BLAZE_ROD.getWandStack().getItem())) {
            return;
        }

        this.checkQuestProgress(player);

        if (!hasQuest(player)) {
            return;
        }

        TileQuarry tileQuarry = new TileQuarry();
        tileQuarry.setup(player.world, quarryPos);

        if (!tileQuarry.checkSurroundings() || !CustomTileEntityHandler.addCustomTileEntity(tileQuarry, tileQuarry.getWorld())) {
            return;
        }

        EntityUtil.addChatComponentMessageNoSpam(player, TextUtil.wrap(tileQuarry.getClass().getSimpleName() + " added"));
    }

    @SubscribeEvent
    public void stopQuarry(BlockEvent.BreakEvent event) {
        BlockPos quarryPos = event.getPos();
        PlayerEntity player = event.getPlayer();
        World world = event.getWorld().getWorld();

        if (!(world.getBlockState(quarryPos).getBlock() instanceof CauldronBlock)) {
            return;
        }

        CustomTileEntityHandler.removeCustomTileEntityAndSendInfoToPlayer(world, quarryPos, player);
    }
}