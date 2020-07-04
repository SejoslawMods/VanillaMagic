package com.github.sejoslaw.vanillamagic.common.quest;

import com.github.sejoslaw.vanillamagic.common.config.VMConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestItemMagnet extends Quest {
    public final int range = VMConfig.ITEM_MAGNET_RANGE.get();

    @SubscribeEvent
    public void useMagnet(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();

        if (!(entity instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity player = (PlayerEntity) entity;

        if (player.isSpectator()) {
            return;
        }

        ItemStack offHandStack = player.getHeldItemOffhand();

        if (offHandStack.getItem() != Items.NETHER_STAR) {
            return;
        }

        float itemMotion = 0.45F;

        double x = player.getPosX();
        double y = player.getPosY() + 0.75;
        double z = player.getPosZ();

        List<ItemEntity> items = player.world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
        Vec3d playerVec = new Vec3d(x, y, z);

        for (ItemEntity itemEntity : items) {
            Vec3d itemEntityVec = new Vec3d(itemEntity.getPosX(), itemEntity.getPosY(), itemEntity.getPosZ());
            Vec3d finalVec = playerVec.subtract(itemEntityVec);

            if (finalVec.length() > 1) {
                finalVec = finalVec.normalize();
            }

            itemEntity.setMotion(new Vec3d(finalVec.getX() * itemMotion, finalVec.getY() * itemMotion, finalVec.getZ() * itemMotion));
        }
    }
}