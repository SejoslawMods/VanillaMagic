package com.github.sejoslaw.vanillamagic.quest.upgrade.itemupgrade.upgrades;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.util.TextUtil;

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

		if (world.getDifficulty() == EnumDifficulty.NORMAL) {
			multiplier = 10;
		} else if (world.getDifficulty() == EnumDifficulty.HARD) {
			multiplier = 40;
		}

		if (target instanceof EntityLivingBase) {
			((EntityLivingBase) target).addPotionEffect(new PotionEffect(MobEffects.WITHER, 20 * multiplier, 1));
		}
	}
}