package com.github.sejoslaw.vanillamagic.common.event;

import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

/**
 * Event which is fired when Extra blocks need to be broken. For instance: to
 * cut down tree.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventExtraBlockBreak extends Event {
    public final ItemStack itemStack;
    public final PlayerEntity player;
    public final BlockState state;

    public int width;
    public int height;
    public int depth;
    public int distance;

    public EventExtraBlockBreak(ItemStack itemStack, PlayerEntity player, BlockState state) {
        this.itemStack = itemStack;
        this.player = player;
        this.state = state;
    }

    public static EventExtraBlockBreak fireEvent(ItemStack itemStack, PlayerEntity player, BlockState state, int width, int height, int depth, int distance) {
        EventExtraBlockBreak event = new EventExtraBlockBreak(itemStack, player, state);
        event.width = width;
        event.height = height;
        event.depth = depth;
        event.distance = distance;

        EventUtil.postEvent(event);
        return event;
    }
}