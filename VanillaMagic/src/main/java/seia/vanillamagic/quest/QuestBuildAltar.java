package seia.vanillamagic.quest;

import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.util.AltarUtil;

public class QuestBuildAltar extends Quest
{
	protected int tier;
	
	public void readData(JsonObject jo)
	{
		super.readData(jo);
		this.tier = jo.get("tier").getAsInt();
	}
	
	/**
	 * Returns the Tier of the Altar connected to this Quest.
	 */
	public int getTier()
	{
		return tier;
	}
	
	@SubscribeEvent
	public void placeBlock(PlaceEvent event)
	{
		EntityPlayer player = event.getPlayer();
		Block middleBlock = event.getPlacedBlock().getBlock();
		if (canPlayerGetQuest(player))
			if (middleBlock instanceof BlockCauldron)
				if (AltarUtil.checkAltarTier(player.world, event.getPos(), tier))
					addStat(player);
	}
}