package seia.vanillamagic.quest;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.util.ListHelper;

public class QuestMineBlock extends Quest
{
	protected List<Block> blocksToBeMine;
	
	public void readData(JsonObject jo)
	{
		super.readData(jo);
		List<Block> blocksToBeMine = new ArrayList<Block>();
		JsonElement listJSON = jo.get("blocksToBeMine");
		if(listJSON != null)
		{
			if(listJSON.isJsonArray())
			{
				JsonArray ja = listJSON.getAsJsonArray();
				for(JsonElement je : ja)
				{
					blocksToBeMine.add(Block.getBlockById(je.getAsInt()));
				}
				this.blocksToBeMine = blocksToBeMine;
			}
		}
		else
		{
			this.blocksToBeMine = ListHelper.getList(jo.get("blocksToBeMineClass").getAsString(), jo.get("blocksToBeMineList").getAsString());
		}
	}
	
	public List<Block> getBlocksToBeMine()
	{
		return blocksToBeMine;
	}
	
	@SubscribeEvent
	public void onBreakBlock(BlockEvent.BreakEvent event)
	{
		EntityPlayer player = event.getPlayer();
		if(canPlayerGetAchievement(player))
		{
			Block block = event.getState().getBlock();
			if(block == null) // this should never happen, right ?
			{
				return;
			}
			for(int i = 0; i < blocksToBeMine.size(); i++)
			{
				if(Block.isEqualTo(block, blocksToBeMine.get(i)))
				{
					player.addStat(achievement, 1);
					return;
				}
			}
		}
	}
}