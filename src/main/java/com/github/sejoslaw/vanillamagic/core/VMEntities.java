package com.github.sejoslaw.vanillamagic.core;

import com.github.sejoslaw.vanillamagic.common.entity.EntitySpellFreezeLiquid;
import com.github.sejoslaw.vanillamagic.common.entity.EntitySpellPull;
import com.github.sejoslaw.vanillamagic.common.entity.EntitySpellSummonLightningBolt;
import com.github.sejoslaw.vanillamagic.common.entity.EntitySpellTeleport;
import com.github.sejoslaw.vanillamagic.common.entity.meteor.EntityMeteor;
import com.github.sejoslaw.vanillamagic.common.entity.meteor.EntitySpellSummonMeteor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ObjectHolder;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@ObjectHolder(VanillaMagic.MODID)
public class VMEntities {
    public static final EntityType<EntitySpellFreezeLiquid> FREEZE_LIQUID_ENTITY_TYPE = null;
    public static final EntityType<EntitySpellPull> PULL_ENTITY_TYPE = null;
    public static final EntityType<EntitySpellSummonLightningBolt> LIGHTNING_BOLT_ENTITY_TYPE = null;
    public static final EntityType<EntitySpellTeleport> TELEPORT_ENTITY_TYPE = null;
    public static final EntityType<EntitySpellSummonMeteor> SUMMON_METEOR_ENTITY_TYPE = null;
    public static final EntityType<EntityMeteor> METEOR_ENTITY_TYPE = null;

    @SubscribeEvent
    public static void onEntitiesRegistry(final RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll(
                buildEntityType(EntitySpellFreezeLiquid::new, "spellFreezeLiquid"),
                buildEntityType(EntitySpellPull::new, "spellPull"),
                buildEntityType(EntitySpellSummonLightningBolt::new, "spellLightningBolt"),
                buildEntityType(EntitySpellTeleport::new, "spellTeleport"),
                buildEntityType(EntitySpellSummonMeteor::new, "spellMeteor"),
                buildEntityType(EntityMeteor::new, "entityMeteor")
        );
    }

    private static <T extends Entity> EntityType<?> buildEntityType(EntityType.IFactory<T> factory, String registryName) {
        return EntityType.Builder
                .create(factory, EntityClassification.MISC)
                .setShouldReceiveVelocityUpdates(true)
                .setTrackingRange(50)
                .setUpdateInterval(60)
                .size(1.0F, 1.0F)
                .build(registryName)
                .setRegistryName(VanillaMagic.MODID, registryName);
    }
}
