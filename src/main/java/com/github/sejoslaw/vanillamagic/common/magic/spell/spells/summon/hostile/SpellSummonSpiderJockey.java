package com.github.sejoslaw.vanillamagic.common.magic.spell.spells.summon.hostile;

import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellSummonSpiderJockey extends SpellSummonHostile {
    public SpellSummonSpiderJockey(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
        super(spellID, spellName, spellUniqueName, wand, itemOffHand);
    }

    public boolean castSpell(PlayerEntity caster, BlockPos pos, Direction face, Vec3d hitVec) {
        if (pos == null) {
            return false;
        }

        World world = caster.world;
        BlockPos spawnPos = pos.offset(face);
        Entity spiderEntity = null;
        ItemStack boneStack = new ItemStack(Items.BONE);
        BlockPos bonePos = pos.offset(face);

        AxisAlignedBB aabb = new AxisAlignedBB(bonePos);
        List<Entity> entities = world.getEntitiesInAABBexcluding(caster, aabb, e -> e instanceof ItemEntity);

        for (Entity entity : entities) {
            if (ItemStack.areItemsEqual(boneStack, ((ItemEntity) entity).getItem())) {
                spiderEntity = new SpiderEntity(EntityType.SPIDER, world);

                Entity skeleton = new SkeletonEntity(EntityType.SKELETON, world);
                skeleton.setLocationAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), caster.rotationYaw, 0.0F);
                skeleton.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));

                world.addEntity(skeleton);

                skeleton.startRiding(spiderEntity);

                entity.remove();

                break;
            }
        }

        if (spiderEntity == null) {
            return false;
        }

        spiderEntity.setLocationAndAngles(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, caster.rotationYaw, 0.0F);

        world.addEntity(spiderEntity);

        return true;
    }

    // Build in castSpell method.
    public Entity getEntity(World world) {
        return null;
    }
}