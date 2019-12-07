package com.github.sejoslaw.vanillamagic.api.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * Base class for all Events that connects Player and World.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventPlayerOnWorld extends VMEvent {
    private final PlayerEntity player;
    private final World world;

    public EventPlayerOnWorld(PlayerEntity player, World world) {
        this.player = player;
        this.world = world;
    }

    /**
     * @return Returns Player who started this Event.
     */
    public PlayerEntity getPlayerEntity() {
        return player;
    }

    /**
     * @return Returns World on which Player started this Event.
     */
    public World getWorld() {
        return world;
    }
}