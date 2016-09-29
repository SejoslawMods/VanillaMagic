package seia.vanillamagic.quest.multimine;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import seia.vanillamagic.util.ToolHelper;

public class MineBlockTask 
{
	public ItemStack mainHand;
	public World world;
	public EntityPlayer player;
	public BlockPos pos;
	public BlockPos refPos;
	
	public MineBlockTask(ItemStack mainHand, World world, EntityPlayer player, BlockPos pos, BlockPos refPos) 
	{
		this.mainHand = mainHand;
		this.world = world;
		this.player = player;
		this.pos = pos;
		this.refPos = refPos;
	}
	
	@SubscribeEvent
	public void mine(WorldTickEvent event)
	{
		if(!world.isAirBlock(pos) && !Block.isEqualTo(world.getBlockState(pos).getBlock(), Blocks.BEDROCK))
		{
			ToolHelper.breakExtraBlock(mainHand, world, player, pos, pos);
		}
		MinecraftForge.EVENT_BUS.unregister(this);
	}
}