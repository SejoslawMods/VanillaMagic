package seia.vanillamagic.quest.fulltreecut;

import java.util.Queue;
import java.util.Set;

import com.google.common.collect.Lists;

import gnu.trove.set.hash.THashSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import seia.vanillamagic.util.ToolHelper;

/**
 * Class which describes single tree fall mechanics.
 */
public class TreeChopTask 
{
	public final World world;
	public final EntityPlayer player;
	public final ItemStack tool;
	public final int blocksPerTick;
	
	public Queue<BlockPos> blocks = Lists.newLinkedList();
	public Set<BlockPos> visited = new THashSet<BlockPos>();

	public TreeChopTask(ItemStack tool, BlockPos start, EntityPlayer player, int blocksPerTick)
	{
		this.world = player.getEntityWorld();
		this.player = player;
		this.tool = tool;
		this.blocksPerTick = blocksPerTick;
		this.blocks.add(start);
	}
	
	/**
	 * Break logs.
	 */
	@SubscribeEvent
	public void chopChop(WorldTickEvent event)
	{
		if(event.side.isClient()) 
		{
			finish();
			return;
		}      
		if(event.world == world) 
		{
			return;
		}
		// setup
		int left = blocksPerTick;
		// continue running
		BlockPos pos;
		while(left > 0) 
		{
			// completely done or can't do our job anymore?!
			if(blocks.isEmpty()) 
			{
				finish();
				return;
			}
			pos = blocks.remove();
			if(!visited.add(pos)) 
			{
				continue;
			}
			// can we harvest the block and is effective?
			if(!TreeCutHelper.isLog(world, pos))
			{
				continue;
			}
			// save its neighbours
			for(EnumFacing facing : new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST}) 
			{
				BlockPos pos2 = pos.offset(facing);
				if(!visited.contains(pos2)) 
				{
					blocks.add(pos2);
				}
			}
			// also add the layer above
			for(int x = 0; x < 3; ++x) 
			{
				for(int z = 0; z < 3; ++z) 
				{
					BlockPos pos2 = pos.add(-1 + x, 1, -1 + z);
					if(!visited.contains(pos2)) 
					{
						blocks.add(pos2);
					}
				}
			}
			ToolHelper.breakExtraBlock(tool, world, player, pos, pos);
			left--;
		}
	}
	
	/**
	 * When the tree is broken. Unregister work.
	 */
	private void finish() 
	{
		MinecraftForge.EVENT_BUS.unregister(this);
	}
}