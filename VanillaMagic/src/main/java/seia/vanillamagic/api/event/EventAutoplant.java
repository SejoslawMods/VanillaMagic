package seia.vanillamagic.api.event;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemEvent;

/**
 * This Event is fired when a Block is about to be autoplant.
 */
public class EventAutoplant extends ItemEvent 
{
	private final World _world;
	private final BlockPos _plantPos;
	
	public EventAutoplant(EntityItem entityItem, World world, BlockPos plantPosition) 
	{
		super(entityItem);
		this._world = world;
		this._plantPos = plantPosition;
	}
	
	/**
	 * @return Returns World on which EntityItem will be planted.
	 */
	public World getWorld()
	{
		return this._world;
	}
	
	/**
	 * @return Returns the position on which Block will be planted.
	 */
	public BlockPos getPosition()
	{
		return this._plantPos;
	}
}