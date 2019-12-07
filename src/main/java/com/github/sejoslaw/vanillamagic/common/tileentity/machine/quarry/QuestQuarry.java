package com.github.sejoslaw.vanillamagic.tileentity.machine.quarry;

import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.handler.CustomTileEntityHandler;
import com.github.sejoslaw.vanillamagic.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.quest.QuestMachineActivate;
import com.github.sejoslaw.vanillamagic.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestQuarry extends QuestMachineActivate {
	@SubscribeEvent
	public void startQuarry(RightClickBlock event) {
		BlockPos quarryPos = event.getPos();
		PlayerEntity player = event.getPlayerEntity();
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
		tileQuarry.init(player.world, quarryPos);

		if (!tileQuarry.checkSurroundings()
				|| !CustomTileEntityHandler.addCustomTileEntity(tileQuarry, player.dimension)) {
			return;
		}

		EntityUtil.addChatComponentMessageNoSpam(player, tileQuarry.getClass().getSimpleName() + " added");
	}

	@SubscribeEvent
	public void stopQuarry(BreakEvent event) {
		BlockPos quarryPos = event.getPos();
		PlayerEntity player = event.getPlayer();
		World world = event.getWorld();

		if (!(world.getBlockState(quarryPos).getBlock() instanceof BlockCauldron)) {
			return;
		}

		CustomTileEntityHandler.removeCustomTileEntityAndSendInfoToPlayer(world, quarryPos, player);
	}
}