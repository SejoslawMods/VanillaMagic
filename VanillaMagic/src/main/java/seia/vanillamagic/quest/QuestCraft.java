package seia.vanillamagic.quest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class QuestCraft extends Quest
{
	@SubscribeEvent
	public void onCrafting(PlayerEvent.ItemCraftedEvent event)
	{
		EntityPlayer player = event.player;
		if(!player.hasAchievement(achievement))
		{
			Item item = event.crafting.getItem();
			if(item.equals(achievement.theItemStack.getItem()))
			{
				if(canPlayerGetAchievement(player))
				{
					player.addStat(achievement, 1);
				}
			}
		}
	}
}