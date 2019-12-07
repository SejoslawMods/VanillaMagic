package com.github.sejoslaw.vanillamagic.common.entity;

import com.github.sejoslaw.vanillamagic.common.util.WeatherUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

/**
 * Class which defines the Spell Summon Lightning Bolt.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EntitySpellSummonLightningBolt extends EntitySpell {
    public EntitySpellSummonLightningBolt(EntityType<? extends EntitySpell> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Run when Entity collide with something.
     */
    protected void onHit(RayTraceResult result) {
        if (result.getType() == Type.BLOCK) {
            WeatherUtil.spawnLightningBolt(world, ((BlockRayTraceResult) result).getPos());
        }
    }
}