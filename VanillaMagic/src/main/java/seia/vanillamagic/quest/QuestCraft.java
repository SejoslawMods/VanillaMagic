package seia.vanillamagic.quest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class QuestCraft extends Quest
{
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event)
	{
		EntityPlayer player = event.player;
		if (!hasQuest(player))
		{
			Item item = event.crafting.getItem();
			if (item.equals(this.getIcon().getItem()))
				checkQuestProgress(player);
		}
	}
}