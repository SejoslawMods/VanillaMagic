package seia.vanillamagic.quest;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.utils.AltarChecker;

public class QuestBuildAltar extends Quest
{
	protected int tier;
	
	public QuestBuildAltar(Achievement required, int posX, int posY, Item itemIcon, String questName, String uniqueName, 
			int tier)
	{
		super(required, posX, posY, itemIcon, questName, uniqueName);
		this.tier = tier;
	}
	
	public int getTier()
	{
		return tier;
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
				BlockSnapshot block = event.getBlockSnapshot();
				if(AltarChecker.checkAltarTier(block.getWorld(), block.getPos(), tier))
				{
					player.addStat(achievement, 1);
				}
			}
		}
	}
}