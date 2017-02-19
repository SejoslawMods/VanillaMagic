package seia.vanillamagic.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This Event will fire if Player with unlocked required Quest will try to save block into book.
 */
public class EventMoveBlock extends Event 
{
	private World world;
	private EntityPlayer player;
	private BlockPos pos;
	private EnumFacing face;
	
	public EventMoveBlock(World world, EntityPlayer player, BlockPos pos, EnumFacing face)
	{
		this.world = world;
		this.player = player;
		this.pos = pos;
		this.face = face;
	}
	
	/**
	 * @return Returns the World on which this Event occurred.
	 */
	public World getWorld()
	{
		return world;
	}
	
	/**
	 * @return Returns the Player who used book.
	 */
	public EntityPlayer getEntityPlayer()
	{
		return player;
	}
	
	/**
	 * @return Returns the position of the clicked block.
	 */
	public BlockPos getPos()
	{
		return pos;
	}
	
	/**
	 * @return Returns the face of the clicked block.
	 */
	public EnumFacing getFace()
	{
		return face;
	}
	
	/**
	 * This Event is fired BEFORE the block is saved into book. <br>
	 * Canceling this Event will prevent block from being saved into book.
	 */
	public static class Save extends EventMoveBlock
	{
		public Save(World world, EntityPlayer player, BlockPos pos, EnumFacing face) 
		{
			super(world, player, pos, face);
		}
	}
	
	/**
	 * This Event is fired BEFORE the block is loaded from book. <br>
	 * Canceling this Event will prevent block from being loaded from book.
	 */
	public static class Load extends EventMoveBlock
	{
		public Load(World world, EntityPlayer player, BlockPos pos, EnumFacing face) 
		{
			super(world, player, pos, face);
		}
	}
}