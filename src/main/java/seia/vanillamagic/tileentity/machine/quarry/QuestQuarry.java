package seia.vanillamagic.tileentity.machine.quarry;

import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.handler.CustomTileEntityHandler;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.quest.QuestMachineActivate;
import seia.vanillamagic.util.EntityUtil;
import seia.vanillamagic.util.ItemStackUtil;

public class QuestQuarry extends QuestMachineActivate
{
	@SubscribeEvent
	public void startQuarry(RightClickBlock event)
	{
		BlockPos quarryPos = event.getPos();
		EntityPlayer player = event.getEntityPlayer();
		ItemStack itemInHand = player.getHeldItemMainhand();
		if (!player.isSneaking()) return;
		if (ItemStackUtil.isNullStack(itemInHand)) return;
		
		if (itemInHand.getItem().equals(WandRegistry.WAND_BLAZE_ROD.getWandStack().getItem()))
		{
			if (canAddStat(player)) addStat(player);
			
			if (hasQuest(player))
			{
				TileQuarry tileQuarry = new TileQuarry();
				tileQuarry.init(player.world, quarryPos);
				if (tileQuarry.checkSurroundings())
					if (CustomTileEntityHandler.addCustomTileEntity(tileQuarry, player.dimension))
						EntityUtil.addChatComponentMessageNoSpam(player, tileQuarry.getClass().getSimpleName() + " added");
			}
		}
	}

	@SubscribeEvent
	public void stopQuarry(BreakEvent event)
	{
		BlockPos quarryPos = event.getPos();
		EntityPlayer player = event.getPlayer();
		World world = event.getWorld();
		if (world.getBlockState(quarryPos).getBlock() instanceof BlockCauldron)
			CustomTileEntityHandler.removeCustomTileEntityAndSendInfoToPlayer(world, quarryPos, player);
	}
}