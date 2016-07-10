package seia.vanillamagic.quest.craft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import seia.vanillamagic.quest.Quest;

public class QuestCraft extends Quest
{
	public QuestCraft(Achievement required, int posX, int posY, Item itemIcon, String questName, String uniqueName) 
	{
		super(required, posX, posY, itemIcon, questName, uniqueName);
	}
	
	@SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event)
	{
		EntityPlayer player = event.player;
		if(!player.hasAchievement(achievement))
		{
			Item item = event.crafting.getItem();
			if(item.equals(achievement.theItemStack.getItem()))
			{
				player.addStat(achievement, 1);
			}
		}
	}
}