package com.github.sejoslaw.vanillamagic.api.event;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemEvent;

/**
 * This Event is fired when a Block is about to be autoplanted.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventAutoplant extends ItemEvent {
    private final World world;
    private final BlockPos plantPos;

    public EventAutoplant(ItemEntity entity, World world, BlockPos plantPosition) {
        super(entity);
        this.world = world;
        this.plantPos = plantPosition;
    }

    /**
     * @return Returns World on which ItemEntity will be planted.
     */
    public World getWorld() {
        return this.world;
    }

    /**
     * @return Returns the position on which Block will be planted.
     */
    public BlockPos getPosition() {
        return this.plantPos;
    }
}