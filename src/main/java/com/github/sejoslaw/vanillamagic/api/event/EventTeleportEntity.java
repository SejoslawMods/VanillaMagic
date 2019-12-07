package com.github.sejoslaw.vanillamagic.api.event;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

/**
 * This Event is fired BEFORE Entity is teleport in VanillaMagic-way.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventTeleportEntity extends VMEvent {
    private final Entity entity;
    private final BlockPos newPos;

    public EventTeleportEntity(Entity entity, BlockPos newPos) {
        this.entity = entity;
        this.newPos = newPos;
    }

    /**
     * @return Returns Entity to teleport.
     */
    public Entity getEntity() {
        return this.entity;
    }

    /**
     * It may return current Entity position if used in cross-dimension
     * teleport.<br>
     *
     * @return Returns new position on which Entity should be teleport.
     */
    public BlockPos getNewPos() {
        return this.newPos;
    }

    /**
     * This Event is fired BEFORE Entity is teleport to new Dimension.
     */
    public static class ChangeDimension extends EventTeleportEntity {
        private final DimensionType newDimId;

        public ChangeDimension(Entity entity, BlockPos newPos, DimensionType newDimId) {
            super(entity, newPos);
            this.newDimId = newDimId;
        }

        /**
         * @return Returns the Id of a new Dimension into which Entity will be teleport.
         */
        public DimensionType getNewDimension() {
            return this.newDimId;
        }
    }
}