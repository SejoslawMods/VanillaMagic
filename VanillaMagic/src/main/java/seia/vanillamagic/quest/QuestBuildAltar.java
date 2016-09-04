package seia.vanillamagic.quest;

import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.utils.AltarChecker;

public class QuestBuildAltar extends Quest
{
	public int tier;
	
//	public QuestBuildAltar(Quest required, int posX, int posY, String questName, String uniqueName, 
//			int tier)
//	{
//		this(required, posX, posY, new ItemStack(Items.CAULDRON), questName, uniqueName,
//				tier);
//	}
//	
//	public QuestBuildAltar(Quest required, int posX, int posY, ItemStack itemIcon, String questName, String uniqueName, 
//			int tier)
//	{
//		super(required, posX, posY, itemIcon, questName, uniqueName);
//		this.tier = tier;
//	}
	
	public void readData(JsonObject jo)
	{
		super.readData(jo);
		this.tier = jo.get("tier").getAsInt();
	}
	
	@SubscribeEvent
	public void placeBlock(BlockEvent.PlaceEvent event)
	{
		EntityPlayer player = event.getPlayer();
		BlockPos middlePos = event.getBlockSnapshot().getPos();
		Block middleBlock = event.getPlacedBlock().getBlock();
		if(!player.hasAchievement(achievement))
		{
			if(middleBlock instanceof BlockCauldron)
			{
				//BlockSnapshot block = event.getBlockSnapshot();
				if(AltarChecker.checkAltarTier(player.worldObj, event.getPos(), tier))//if(AltarChecker.checkAltarTier(block.getWorld(), block.getPos(), tier))
				{
					player.addStat(achievement, 1);
				}
			}
		}
	}
}