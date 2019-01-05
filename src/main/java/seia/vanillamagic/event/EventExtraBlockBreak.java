package seia.vanillamagic.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;
import seia.vanillamagic.util.EventUtil;

/**
 * Event which is fired when Extra blocks need to be broken. For instance: to
 * cut down tree.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventExtraBlockBreak extends Event {
	public final ItemStack itemStack;
	public final EntityPlayer player;
	public final IBlockState state;

	public int width;
	public int height;
	public int depth;
	public int distance;

	public EventExtraBlockBreak(ItemStack itemStack, EntityPlayer player, IBlockState state) {
		this.itemStack = itemStack;
		this.player = player;
		this.state = state;
	}

	/**
	 * Method to run the Event statically.
	 */
	public static EventExtraBlockBreak fireEvent(ItemStack itemStack, EntityPlayer player, IBlockState state, int width,
			int height, int depth, int distance) {
		EventExtraBlockBreak event = new EventExtraBlockBreak(itemStack, player, state);
		event.width = width;
		event.height = height;
		event.depth = depth;
		event.distance = distance;

		EventUtil.postEvent(event);
		return event;
	}
}