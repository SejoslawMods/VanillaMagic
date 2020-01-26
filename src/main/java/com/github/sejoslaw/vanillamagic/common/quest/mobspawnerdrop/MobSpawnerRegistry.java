package com.github.sejoslaw.vanillamagic.common.quest.mobspawnerdrop;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.core.VMLogger;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

/**
 * Registry which store all data related with Mob Spawner.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class MobSpawnerRegistry {
    public static final String NBT_SPAWNER_ENTITY = "NBT_SPAWNER_ENTITY";
    private static final Set<MobSpawnerRegistryEntry> ENTRIES = new HashSet<>();

    private MobSpawnerRegistry() {
    }

    /**
     * PostInitialization stage. Register all Entities for Mob Spawner.
     */
    public static void postInit() {
        VMLogger.logInfo("Started Mob Spawner Registry...");

        for (EntityType<?> type : ForgeRegistries.ENTITIES) {
            String entityName = Registry.ENTITY_TYPE.getKey(type).toString();

            ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
            stack.setDisplayName(TextUtil.wrap("Mob Spawner Entity Data: " + entityName));

            CompoundNBT nbt = stack.getOrCreateTag();
            nbt.putString(NBT_SPAWNER_ENTITY, entityName);

            ENTRIES.add(new MobSpawnerRegistryEntry(type, entityName, stack));
        }

        VMLogger.logInfo("Mob Spawner Registry: registered " + ENTRIES.size() + " entities for Mob Spawner.");
    }

    /**
     * @return Returns ItemStack with Entity info.
     */
    public static ItemStack getStackFromTile(MobSpawnerTileEntity tileEntity) {
        AbstractSpawner spawner = tileEntity.getSpawnerBaseLogic();
        CompoundNBT nbt = spawner.write(new CompoundNBT());
        ListNBT listNbt = (ListNBT) nbt.get("SpawnPotentials");
        CompoundNBT entityNbt = listNbt.getCompound(0);
        String entityId = entityNbt.getString("id");

        return getEntry(entityId).stack;
    }

    /**
     * Change MobSpawner mob ID.
     */
    public static void setID(MobSpawnerTileEntity tileEntity, String entityId, World world, BlockPos spawnerPos) {
        EntityType<?> entityType = getEntry(entityId).entityType;
        tileEntity.getSpawnerBaseLogic().setEntityType(entityType);
    }

    private static MobSpawnerRegistryEntry getEntry(String entityId) {
        return ENTRIES.stream().filter(entry -> entry.entityId.equals(entityId)).findFirst().orElse(null);
    }
}