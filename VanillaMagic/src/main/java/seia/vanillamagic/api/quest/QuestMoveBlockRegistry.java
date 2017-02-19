package seia.vanillamagic.api.quest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class QuestMoveBlockRegistry 
{
	private static final List<Block> _QUEST_MOVE_BLOCK_BLACKLIST = new ArrayList<Block>();
	
	private QuestMoveBlockRegistry()
	{
	}
	
	static
	{
		// Vanilla Blacklisted Blocks
		addBlockToMoveBlockBlacklist(Blocks.PORTAL); // Nether Portal
		addBlockToMoveBlockBlacklist(Blocks.END_PORTAL);
		addBlockToMoveBlockBlacklist(Blocks.END_PORTAL_FRAME);
		addBlockToMoveBlockBlacklist(Blocks.END_GATEWAY);
		
		addBlockToMoveBlockBlacklist(Blocks.CAULDRON); // To prevent in-mod errors.
	}
	
	public static void addBlockToMoveBlockBlacklist(Block blockToAdd)
	{
		if(!_QUEST_MOVE_BLOCK_BLACKLIST.contains(blockToAdd))
		{
			_QUEST_MOVE_BLOCK_BLACKLIST.add(blockToAdd);
		}
	}
	
	public static boolean isBlockOnMoveBlockBlacklist(Block block)
	{
		return _QUEST_MOVE_BLOCK_BLACKLIST.contains(block);
	}
}