package seia.vanillamagic.quest.fulltreecut;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.util.ItemStackHelper;

public class QuestFullTreeCut extends Quest
{
	@SubscribeEvent
	public void onTreeCut(BreakEvent event)
	{
		EntityPlayer player = event.getPlayer();
		ItemStack rightHand = player.getHeldItemMainhand();
		if(ItemStackHelper.isNullStack(rightHand))
		{
			return;
		}
		if(rightHand.getItem() instanceof ItemAxe)
		{
			ItemStack leftHand = player.getHeldItemOffhand();
			if(ItemStackHelper.isNullStack(leftHand))
			{
				return;
			}
			if(ItemStack.areItemsEqual(leftHand, WandRegistry.WAND_BLAZE_ROD.getWandStack()))
			{
				BlockPos origin = event.getPos();
				World world = event.getWorld();
				if(TreeCutHelper.isLog(world, origin))
				{
					origin = origin.offset(EnumFacing.UP);
					if(TreeCutHelper.detectTree(player.world, origin))
					{
						if(canPlayerGetAchievement(player))
						{
							player.addStat(achievement, 1);
						}
						if(player.hasAchievement(achievement))
						{
							TreeCutHelper.fellTree(player.getHeldItemMainhand(), origin, player);
						}
					}
				}
			}
		}
	}
}