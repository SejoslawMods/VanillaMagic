package seia.vanillamagic.item.itemupgrade;

import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.quest.Quest;

public class QuestItemUpgrade extends Quest
{
	@SubscribeEvent
	public void craftItemWithUpgrade(RightClickBlock event)
	{
	}
	
	@SubscribeEvent
	public void onBlockBreak(BreakEvent event)
	{
	}
}