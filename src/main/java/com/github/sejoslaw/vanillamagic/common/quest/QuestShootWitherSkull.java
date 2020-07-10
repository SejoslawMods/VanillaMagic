package com.github.sejoslaw.vanillamagic.common.quest;

import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestShootWitherSkull extends Quest {
    private final ItemStack shouldHaveLeftHand = new ItemStack(Items.WITHER_SKELETON_SKULL);
    private final ItemStack shouldHaveRightHand = WandRegistry.WAND_NETHER_STAR.getWandStack();

    @SubscribeEvent
    public void shootWitherSkull(PlayerInteractEvent.RightClickItem event) {
        PlayerEntity player = event.getPlayer();
        ItemStack leftHand = player.getHeldItemOffhand();
        ItemStack rightHand = player.getHeldItemMainhand();

        if (!ItemStack.areItemsEqual(leftHand, shouldHaveLeftHand) || !ItemStack.areItemsEqual(rightHand, shouldHaveRightHand)) {
            return;
        }

        checkQuestProgress(player);

        if (!hasQuest(player)) {
            return;
        }

        World world = event.getWorld();
        world.playEvent(null, 1024, new BlockPos(player), 0);

        Vec3d lookingAt = player.getLookVec();
        double accelX = lookingAt.getX();
        double accelY = lookingAt.getY();
        double accelZ = lookingAt.getZ();

        WitherSkullEntity entityWitherSkull = new WitherSkullEntity(world, player.getPosX() + accelX, player.getPosY() + 1.5D + accelY, player.getPosZ() + accelZ, accelX, accelY, accelZ);
        entityWitherSkull.shootingEntity = player;
        entityWitherSkull.setMotion(0, 0, 0);

        double accelDelta = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);

        entityWitherSkull.accelerationX = accelX / accelDelta * 0.1D;
        entityWitherSkull.accelerationY = accelY / accelDelta * 0.1D;
        entityWitherSkull.accelerationZ = accelZ / accelDelta * 0.1D;

        world.addEntity(entityWitherSkull);

        ItemStack offHand = player.getHeldItemOffhand();
        ItemStackUtil.decreaseStackSize(offHand, 1);

        if (ItemStackUtil.getStackSize(offHand) <= 0) {
            player.inventory.deleteStack(offHand);
        }
    }
}