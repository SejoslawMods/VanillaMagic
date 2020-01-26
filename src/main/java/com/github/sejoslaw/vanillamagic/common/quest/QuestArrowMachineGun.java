package com.github.sejoslaw.vanillamagic.common.quest;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestArrowMachineGun extends Quest {
    public boolean checkHands(ItemStack leftHand, ItemStack rightHand) {
        return (leftHand.getItem().equals(Items.ARROW) || leftHand.getItem().equals(Items.TIPPED_ARROW))
                && rightHand.getItem().equals(WandRegistry.WAND_NETHER_STAR.getWandStack().getItem());
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickItem event) {
        PlayerEntity player = event.getPlayer();
        ItemStack leftHand = player.getHeldItemOffhand();
        ItemStack rightHand = player.getHeldItemMainhand();
        World world = player.world;

        if (ItemStackUtil.isNullStack(leftHand) || !checkHands(leftHand, rightHand)) {
            return;
        }

        checkQuestProgress(player);

        if (!hasQuest(player)) {
            return;
        }

        ArrowEntity arrowEntity = new ArrowEntity(world, player);
        arrowEntity.setPotionEffect(leftHand);
        arrowEntity.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.0F, 1.0F);
        arrowEntity.setIsCritical(true);

        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, leftHand);

        if (j > 0) {
            arrowEntity.setDamage(arrowEntity.getDamage() + (double) j * 0.5D + 0.5D);
        }

        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, leftHand);

        if (k > 0) {
            arrowEntity.setKnockbackStrength(k);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, leftHand) > 0) {
            arrowEntity.setFire(100);
        }

        world.addEntity(arrowEntity);
        world.playSound(null, player.posX, player.posY, player.posZ,
                SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (new Random().nextFloat() * 0.4F + 1.2F) + 0.5F);

        if (ItemStackUtil.getStackSize(leftHand) > 0) {
            ItemStackUtil.decreaseStackSize(leftHand, 1);

            if (ItemStackUtil.getStackSize(leftHand) <= 0) {
                player.inventory.deleteStack(leftHand);
            }
        }
    }

    @SubscribeEvent
    public void showTooltip(ItemTooltipEvent event) {
        PlayerEntity player = event.getPlayer();

        if (player == null) {
            return;
        }

        ItemStack leftHand = player.getHeldItemOffhand();
        ItemStack rightHand = player.getHeldItemMainhand();

        if (ItemStackUtil.isNullStack(leftHand)) {
            return;
        }

        if (checkHands(leftHand, rightHand)) {
            ItemStack eventStack = event.getItemStack();

            if (ItemStack.areItemsEqual(eventStack, WandRegistry.WAND_NETHER_STAR.getWandStack()) || eventStack.getItem().equals(Items.ARROW) || eventStack.getItem().equals(Items.TIPPED_ARROW)) {
                event.getToolTip().add(TextUtil.wrap("Hold down Right-Mouse-Button to fire arrows very fast."));
            }
        }
    }
}