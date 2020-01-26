package com.github.sejoslaw.vanillamagic.common.quest.upgrade.itemupgrade.upgrades;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

/**
 * Class which defines a Wither Effect for Sword.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class UpgradeWitherEffect extends UpgradeSword {
    public ItemStack getIngredient() {
        return new ItemStack(Items.WITHER_SKELETON_SKULL);
    }

    public String getUniqueNBTTag() {
        return "NBT_UPGRADE_WITHER_EFFECT";
    }

    public String getUpgradeName() {
        return "Wither Effect";
    }

    public String getTextColor() {
        return TextUtil.COLOR_GREY;
    }

    public void onAttack(PlayerEntity player, Entity target) {
        World world = player.getEntityWorld();
        int multiplier = 1;

        if (world.getDifficulty() == Difficulty.NORMAL) {
            multiplier = 10;
        } else if (world.getDifficulty() == Difficulty.HARD) {
            multiplier = 40;
        }

        if (target instanceof LivingEntity) {
            ((LivingEntity) target).addPotionEffect(new EffectInstance(Effects.WITHER, 20 * multiplier, 1));
        }
    }
}