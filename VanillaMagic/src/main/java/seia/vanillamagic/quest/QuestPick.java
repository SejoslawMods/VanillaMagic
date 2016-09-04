package seia.vanillamagic.quest;

import com.google.gson.JsonObject;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import seia.vanillamagic.utils.ItemStackHelper;

public class QuestPick extends Quest
{
	public ItemStack whatToPick;
	
//	public QuestPick(Quest required, int posX, int posY, String questName, String uniqueName,
//			ItemStack whatToPick)
//	{
//		this(required, posX, posY, whatToPick, questName, uniqueName,
//				whatToPick);
//	}
//	
//	public QuestPick(Quest required, int posX, int posY, ItemStack itemIcon, String questName, String uniqueName,
//			ItemStack whatToPick)
//	{
//		super(required, posX, posY, itemIcon, questName, uniqueName);
//		this.whatToPick = whatToPick;
//	}
	
	public void readData(JsonObject jo)
	{
		super.readData(jo);
		this.whatToPick = ItemStackHelper.getItemStackFromJSON(jo.get("whatToPick").getAsJsonObject());
	}
	
	@SubscribeEvent
	public void pickItem(ItemPickupEvent event)
	{
		EntityPlayer player = event.player;
		if(!player.hasAchievement(achievement))
		{
			EntityItem onGround = event.pickedUp;
			if(whatToPick.getItem().equals(onGround.getEntityItem().getItem()))
			{
				player.addStat(achievement, 1);
			}
		}
	}
}