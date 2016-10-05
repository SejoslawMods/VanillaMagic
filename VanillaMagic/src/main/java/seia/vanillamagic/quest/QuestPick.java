package seia.vanillamagic.quest;

import com.google.gson.JsonObject;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import seia.vanillamagic.util.ItemStackHelper;

public class QuestPick extends Quest
{
	protected ItemStack whatToPick;
	
	public void readData(JsonObject jo)
	{
		this.whatToPick = ItemStackHelper.getItemStackFromJSON(jo.get("whatToPick").getAsJsonObject());
		this.icon = whatToPick.copy();
		super.readData(jo);
	}
	
	public ItemStack getWhatToPick()
	{
		return whatToPick;
	}
	
	@SubscribeEvent
	public void pickItem(ItemPickupEvent event)
	{
		EntityPlayer player = event.player;
		if(canPlayerGetAchievement(player))
		{
			EntityItem onGround = event.pickedUp;
			if(whatToPick.getItem().equals(onGround.getEntityItem().getItem()))
			{
				player.addStat(achievement, 1);
			}
		}
	}
}