package com.github.sejoslaw.vanillamagic2.core;

import com.github.sejoslaw.vanillamagic2.common.entities.EntityMeteor;
import com.github.sejoslaw.vanillamagic2.common.entities.EntitySpell;
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
public final class VMEntities {
    public static final EntityType<EntitySpell> SPELL = null;
    public static final EntityType<EntityMeteor> METEOR = null;

    @SubscribeEvent
    public static void onEntitiesRegistry(final RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().registerAll(
                buildEntityType(EntitySpell::new, "spell"),
                buildEntityType(EntityMeteor::new, "meteor")
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
