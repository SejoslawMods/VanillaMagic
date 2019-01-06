package seia.vanillamagic.tileentity.speedy;

import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.handler.CustomTileEntityHandler;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.util.EntityUtil;
import seia.vanillamagic.util.WorldUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestSpeedy extends Quest {
	@SubscribeEvent
	public void placeSpeedy(RightClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
		ItemStack leftHand = player.getHeldItemOffhand();
		ItemStack rightHand = player.getHeldItemMainhand();
		BlockPos clickedPos = event.getPos();

		if (!(world.getBlockState(clickedPos).getBlock() instanceof BlockCauldron)
				|| !VanillaMagicItems.isCustomItem(leftHand, VanillaMagicItems.ACCELERATION_CRYSTAL)
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

		if (!speedy.containsCrystal()
				|| !CustomTileEntityHandler.addCustomTileEntity(speedy, WorldUtil.getDimensionID(world))) {
			return;
		}

		EntityUtil.addChatComponentMessageNoSpam(player, speedy.getClass().getSimpleName() + " added.");
	}
}