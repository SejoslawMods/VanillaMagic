package com.github.sejoslaw.vanillamagic.quest.fulltreecut;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.quest.Quest;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestFullTreeCut extends Quest {
	/**
	 * When tree is cut down it should break all the logs above the destroyed block.
	 */
	@SubscribeEvent
	public void onTreeCut(BreakEvent event) {
		PlayerEntity player = event.getPlayer();
		ItemStack rightHand = player.getHeldItemMainhand();
		ItemStack leftHand = player.getHeldItemOffhand();

		if (ItemStackUtil.isNullStack(rightHand) || ItemStackUtil.isNullStack(leftHand)
				|| !(rightHand.getItem() instanceof ItemAxe)) {
			return;
		}

		if (!ItemStack.areItemsEqual(leftHand, WandRegistry.WAND_BLAZE_ROD.getWandStack())) {
			return;
		}

		BlockPos origin = event.getPos();
		World world = event.getWorld();

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