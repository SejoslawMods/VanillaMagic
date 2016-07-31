package seia.vanillamagic.quest.fulltreecut;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.utils.spell.EnumWand;

public class QuestFullTreeCut extends Quest
{
	public QuestFullTreeCut(Quest required, int posX, int posY, ItemStack itemIcon,
			String questName, String uniqueName)
	{
		super(required, posX, posY, itemIcon, questName, uniqueName);
	}
	
	@SubscribeEvent
	public void onTreeCut(BreakEvent event)
	{
		EntityPlayer player = event.getPlayer();
		try
		{
			if(player.getHeldItemMainhand().getItem() instanceof ItemAxe)
			{
				if(ItemStack.areItemsEqual(player.getHeldItemOffhand(), EnumWand.BLAZE_ROD.wandItemStack))
				{
					BlockPos origin = event.getPos();
					World world = event.getWorld();
					if(TreeCutHelper.isLog(world, origin))
					{
						origin = origin.offset(EnumFacing.UP);
						if(TreeCutHelper.detectTree(player.worldObj, origin))
						{
							if(!player.hasAchievement(achievement))
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
		catch(Exception e)
		{
		}
	}
}