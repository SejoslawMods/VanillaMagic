package seia.vanillamagic.api.quest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

/**
 * This is the main registry holding data about blocks that cannot be moved using quest Move Block By Book.
 * 
 * @author <a href="mailto:k.dobrzynski94@gmail.com">Krzysztof Dobrzyñski</a> -> https://github.com/Sejoslaw
 */
public class QuestMoveBlockRegistry 
{
	/**
	 * List containing all "non-movable" blocks.
	 */
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
	
	/**
	 * Add block to non-movable list.
	 * 
	 * @param blockToAdd
	 */
	public static void addBlockToMoveBlockBlacklist(Block blockToAdd)
	{
		if (!_QUEST_MOVE_BLOCK_BLACKLIST.contains(blockToAdd)) _QUEST_MOVE_BLOCK_BLACKLIST.add(blockToAdd);
	}
	
	/**
	 * @param block
	 * 
	 * @return Returns TRUE if the given block cannot be move using quest Move Block By Book.
	 */
	public static boolean isBlockOnMoveBlockBlacklist(Block block)
	{
		return _QUEST_MOVE_BLOCK_BLACKLIST.contains(block);
	}
}