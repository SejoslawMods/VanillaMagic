package seia.vanillamagic.quest.fulltreecut;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.quest.Quest;

public class QuestFullTreeCut extends Quest
{
	public QuestFullTreeCut(Achievement required, int posX, int posY, Item itemIcon, 
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
			if(!player.hasAchievement(achievement))
			{
				player.addStat(achievement, 1);
				return;
			}
			else if(player.hasAchievement(achievement))
			{
				if(ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(Items.BLAZE_ROD)))
				{
					if(player.getHeldItemMainhand().getItem() instanceof ItemAxe)
					{
						BlockPos origin = event.getPos();
						if(TreeCutHelper.detectTree(player.worldObj, origin))
						{
							TreeCutHelper.fellTree(player.getHeldItemMainhand(), origin, player);
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