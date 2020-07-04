package com.github.sejoslaw.vanillamagic.common.tileentity.speedy;

import com.github.sejoslaw.vanillamagic.common.handler.CustomTileEntityHandler;
import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.common.quest.Quest;
import com.github.sejoslaw.vanillamagic.common.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.core.VMItems;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestSpeedy extends Quest {
	@SubscribeEvent
	public void placeSpeedy(RightClickBlock event) {
		PlayerEntity player = event.getPlayer();
		World world = event.getWorld();
		ItemStack leftHand = player.getHeldItemOffhand();
		ItemStack rightHand = player.getHeldItemMainhand();
		BlockPos clickedPos = event.getPos();

		if (!(world.getBlockState(clickedPos).getBlock() instanceof CauldronBlock)
				|| !VMItems.isCustomItem(leftHand, VMItems.ACCELERATION_CRYSTAL)
				|| !WandRegistry.areWandsEqual(rightHand, WandRegistry.WAND_BLAZE_ROD.getWandStack())
				|| !player.isSneaking()) {
			return;
		}

		this.checkQuestProgress(player);

		if (!this.hasQuest(player)) {
			return;
		}

		TileSpeedy speedy = new TileSpeedy();
		speedy.init(player.world, clickedPos);

		if (!speedy.containsCrystal() || !CustomTileEntityHandler.addCustomTileEntity(speedy, world)) {
			return;
		}

		EntityUtil.addChatComponentMessage(player, speedy.getClass().getSimpleName() + " added.");
	}
}