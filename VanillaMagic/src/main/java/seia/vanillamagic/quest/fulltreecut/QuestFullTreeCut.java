package seia.vanillamagic.quest.fulltreecut;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
			if(player.getHeldItemMainhand().getItem() instanceof ItemAxe)
			{
				if(ItemStack.areItemsEqual(player.getHeldItemOffhand(), new ItemStack(Items.BLAZE_ROD)))
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