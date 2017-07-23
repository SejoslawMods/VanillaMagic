package seia.vanillamagic.quest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.core.VanillaMagic;

public class QuestMineBlock extends Quest
{
	protected List<Block> blocksToBeMine;
	
	public void readData(JsonObject jo)
	{
		super.readData(jo);
		List<Block> blocksToBeMine = new ArrayList<Block>();
		JsonElement listJSON = jo.get("blocksToBeMine");
		if (listJSON != null)
		{
			if (listJSON.isJsonArray())
			{
				JsonArray ja = listJSON.getAsJsonArray();
				for (JsonElement je : ja) blocksToBeMine.add(Block.getBlockById(je.getAsInt()));
				this.blocksToBeMine = blocksToBeMine;
			}
		}
		else
		{
			String className = jo.get("blocksToBeMineClass").getAsString();
			String listName = jo.get("blocksToBeMineList").getAsString();
			try
			{
				Class<?> clazz = Class.forName(className);
				Field field = clazz.getField(listName);
				this.blocksToBeMine = (List<Block>) field.get(null);
			}
			catch(Exception e)
			{
				VanillaMagic.LOGGER.log(Level.ERROR, e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public List<Block> getBlocksToBeMine()
	{
		return blocksToBeMine;
	}
	
	@SubscribeEvent
	public void onBreakBlock(BreakEvent event)
	{
		EntityPlayer player = event.getPlayer();
		if (canPlayerGetQuest(player))
		{
			Block block = event.getState().getBlock();
			if (block == null) return; // this should never happen, right ?
			if (blocksToBeMine == null) return;
			
			for (int i = 0; i < blocksToBeMine.size(); ++i)
			{
				if (Block.isEqualTo(block, blocksToBeMine.get(i)))
				{
					addStat(player);
					return;
				}
			}
		}
	}
}