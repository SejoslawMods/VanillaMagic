package com.github.sejoslaw.vanillamagic.quest.upgrade.itemupgrade.upgrades;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.util.TextUtil;
import com.github.sejoslaw.vanillamagic.util.WeatherUtil;
import net.minecraft.item.Items;

/**
 * Class which defines Thor upgrade for Sword. (thunder enemy on hit).
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class UpgradeThor extends UpgradeSword {
	public ItemStack getIngredient() {
		return new ItemStack(Items.CREEPER_HEAD);
	}

	public String getUniqueNBTTag() {
		return "NBT_UPGRADE_THOR";
	}

	public String getUpgradeName() {
		return "Thor " + "\u26A1";
	}

	public String getTextColor() {
		return TextUtil.COLOR_BLUE;
	}

	public void onAttack(PlayerEntity player, Entity target) {
		if (target instanceof EntityLivingBase) {
			WeatherUtil.spawnLightningBolt(player.world, target.posX, target.posY, target.posZ);
		}
	}
}