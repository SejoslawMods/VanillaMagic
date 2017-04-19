package seia.vanillamagic.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;

/**
 * This Event will fire if Player with unlocked required Quest will try to save / load block into / from book.
 * 
 * @since 0.21.11.4
 * Quest Move Block By Book now uses {@link BreakEvent} and {@link PlaceEvent}
 */
@Deprecated
public class EventMoveBlock extends EventPlayerOnWorld 
{
	private final BlockPos _pos;
	private final EnumFacing _face;
	
	public EventMoveBlock(World world, EntityPlayer player, BlockPos pos, EnumFacing face)
	{
		super(player, world);
		this._pos = pos;
		this._face = face;
	}
	
	/**
	 * @return Returns the position of the clicked block.
	 */
	public BlockPos getPos()
	{
		return _pos;
	}
	
	/**
	 * @return Returns the face of the clicked block.
	 */
	public EnumFacing getFace()
	{
		return _face;
	}
	
	/**
	 * This Event is fired BEFORE the block is saved into book. <br>
	 * Canceling this Event will prevent block from being saved into book.
	 */
	@Deprecated
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
	@Deprecated
	public static class Load extends EventMoveBlock
	{
		public Load(World world, EntityPlayer player, BlockPos pos, EnumFacing face) 
		{
			super(world, player, pos, face);
		}
	}
}