package seia.vanillamagic.quest.fulltreecut;

import java.util.Stack;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import seia.vanillamagic.event.EventExtraBlockBreak;

/**
 * Class which contains various methods to help cut tree.
 */
public class TreeCutHelper 
{
	private TreeCutHelper()
	{
	}
	
	/**
	 * @return Returns TRUE if the block at specified position is a log.
	 */
	public static boolean isLog(World world, BlockPos pos)
	{
		return world.getBlockState(pos).getBlock().isWood(world, pos);
	}
	
	/**
	 * @return Returns TRUE if the tree was detected.
	 */
	public static boolean detectTree(World world, BlockPos origin)
	{
		BlockPos pos = null;
		Stack<BlockPos> candidates = new Stack<BlockPos>();
		candidates.add(origin);
		while (!candidates.isEmpty()) 
		{
			BlockPos candidate = candidates.pop();
			if (((pos == null) || (candidate.getY() > pos.getY())) && isLog(world, candidate))
			{
				pos = candidate.up();
				// go up
				while (isLog(world, pos)) pos = pos.up(); 
				
				// check if we still have a way diagonally up
				candidates.add(pos.north());
				candidates.add(pos.east());
				candidates.add(pos.south());
				candidates.add(pos.west());
			}
		}
	    // not even one match, so there were no logs.
		if (pos == null) return false;
		
		// check if there were enough leaves around the last position
		// pos now contains the block above the topmost log
		// we want at least 5 leaves in the surrounding 26 blocks
		int d = 3;
		int o = -1; // -(d-1)/2
		int leaves = 0;
		for (int x = 0; x < d; ++x) 
		{
			for (int y = 0; y < d; ++y) 
			{
				for (int z = 0; z < d; ++z) 
				{
					BlockPos leaf = pos.add(o + x, o + y, o + z);
					IBlockState state = world.getBlockState(leaf);
					if (state.getBlock().isLeaves(state, world, leaf)) 
						if (++leaves >= 5) 
							return true;
				}
			}
		}
		// not enough leaves
		return false;
	}
	
	/**
	 * Start tree breaking.
	 */
	public static boolean fellTree(ItemStack itemstack, BlockPos start, EntityPlayer player)
	{
		if (player.world.isRemote) return true;
		
		EventExtraBlockBreak event = EventExtraBlockBreak.fireEvent(itemstack, player, player.getEntityWorld().getBlockState(start), 3, 3, 3, 1);
		int speed = Math.round((event.width * event.height * event.depth) / 27f);
		if (event.distance > 0) speed = event.distance + 1;
		
		MinecraftForge.EVENT_BUS.register(new TreeChopTask(itemstack, start, player, speed));
		return true;
	}
}	