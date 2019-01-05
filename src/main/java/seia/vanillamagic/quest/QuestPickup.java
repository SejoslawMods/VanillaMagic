package seia.vanillamagic.quest;

import com.google.gson.JsonObject;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.util.ItemStackUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestPickup extends Quest {
	protected ItemStack whatToPick;

	public void readData(JsonObject jo) {
		this.whatToPick = ItemStackUtil.getItemStackFromJSON(jo.get("whatToPick").getAsJsonObject());
		this.icon = whatToPick.copy();
		super.readData(jo);
	}

	public ItemStack getWhatToPick() {
		return whatToPick;
	}

	@SubscribeEvent
	public void pickItem(EntityItemPickupEvent event) {
		EntityPlayer player = event.getEntityPlayer();

		if (!canPlayerGetQuest(player)) {
			return;
		}

		EntityItem onGround = event.getItem();

		if (!ItemStack.areItemStacksEqual(whatToPick, onGround.getItem()) || hasQuest(player)) {
			return;
		}

		addStat(player);
	}
}