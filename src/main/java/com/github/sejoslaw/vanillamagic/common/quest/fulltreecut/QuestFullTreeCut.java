package com.github.sejoslaw.vanillamagic.common.quest.fulltreecut;

import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.common.quest.Quest;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestFullTreeCut extends Quest {
    /**
     * When tree is cut down it should break all the logs above the destroyed block.
     */
    @SubscribeEvent
    public void onTreeCut(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        ItemStack rightHand = player.getHeldItemMainhand();
        ItemStack leftHand = player.getHeldItemOffhand();

        if (ItemStackUtil.isNullStack(rightHand) ||
                ItemStackUtil.isNullStack(leftHand) ||
                !(rightHand.getItem() instanceof AxeItem) ||
                !ItemStack.areItemsEqual(leftHand, WandRegistry.WAND_BLAZE_ROD.getWandStack())) {
            return;
        }

        BlockPos origin = event.getPos();
        World world = event.getWorld().getWorld();

        if (!TreeCutHelper.isLog(world, origin)) {
            return;
        }

        origin = origin.offset(Direction.UP);

        if (!TreeCutHelper.detectTree(player.world, origin)) {
            return;
        }

        checkQuestProgress(player);

        if (!hasQuest(player)) {
            return;
        }

        TreeCutHelper.fellTree(player.getHeldItemMainhand(), origin, player);
    }
}