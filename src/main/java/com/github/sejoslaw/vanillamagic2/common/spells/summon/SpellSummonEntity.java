package com.github.sejoslaw.vanillamagic2.common.spells.summon;

import com.github.sejoslaw.vanillamagic2.common.registries.SpellSummonEntityRegistry;
import com.github.sejoslaw.vanillamagic2.common.spells.Spell;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellSummonEntity<TEntity extends Entity> extends Spell {
    private final EntityType<TEntity> entityType;

    protected SpellSummonEntity(EntityType<TEntity> entityType) {
        this.entityType = entityType;
    }

    public void cast(PlayerEntity player, IWorld world, BlockPos pos, Direction face) {
        List<Entity> entities = new ArrayList<>();

        this.fillEntitiesToSpawn(world, entities);

        entities.forEach(entity -> {
            if (entity instanceof AgeableEntity) {
                ((AgeableEntity) entity).setGrowingAge(1);
            }

            BlockPos spawnPos = this.getSpawnPos(world, pos.offset(face), entity);
            entity.setLocationAndAngles(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, player.rotationYaw, 0.0F);

            this.modifyEntity(entity);

            world.addEntity(entity);
        });
    }

    public EntityType<? extends Entity> getEntityType() {
        return this.entityType;
    }

    /**
     * Fills list with Entities which should be spawned.
     */
    protected void fillEntitiesToSpawn(IWorld world, List<Entity> entities) {
        entities.add(this.entityType.create(WorldUtils.asWorld(world)));
    }

    /**
     * @return Position where next entity should be spawned
     */
    protected BlockPos getSpawnPos(IWorld world, BlockPos spawnPos, Entity entity) {
        return spawnPos;
    }

    /**
     * Allows nested classes to modify specified Entity.
     */
    protected void modifyEntity(Entity entity) {
    }

    protected int getPercent() {
        return new Random().nextInt(100);
    }

    /**
     * Sets specified ItemStack to main hand of the Entity.
     */
    protected void setItemStackToMainHand(Entity entity, ItemStack stack) {
        entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, stack);
    }

    /**
     * Sets data to spawn grown Slime / Magma Cream.
     */
    protected void setSlimeData(Entity entity) {
        final String key = "Size";

        SlimeEntity slime = (SlimeEntity) entity;

        CompoundNBT nbt = new CompoundNBT();

        slime.writeAdditional(nbt);
        nbt.remove(key);
        nbt.putInt(key, 4);
        slime.readAdditional(nbt);
    }

    /**
     * @return Registers new Summon Spell.
     */
    public static SpellSummonEntity<?> register(EntityType<?> entityType) {
        SpellSummonEntity<?> overwrittenSpell = SpellSummonEntityRegistry.getLogic(entityType);
        return overwrittenSpell != null ? overwrittenSpell : new SpellSummonEntity<>(entityType);
    }
}
