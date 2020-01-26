package com.github.sejoslaw.vanillamagic.common.quest.mobspawnerdrop;

import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;

public class MobSpawnerRegistryEntry {
    public final EntityType<?> entityType;
    public final String entityId;
    public final ItemStack stack;

    public MobSpawnerRegistryEntry(EntityType<?> entityType, String entityId, ItemStack bookStack) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.stack = bookStack;
    }
}
