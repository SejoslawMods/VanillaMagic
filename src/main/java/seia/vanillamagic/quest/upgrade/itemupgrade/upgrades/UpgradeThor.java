package seia.vanillamagic.quest.upgrade.itemupgrade.upgrades;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.util.ItemStackUtil;
import seia.vanillamagic.util.TextUtil;
import seia.vanillamagic.util.WeatherUtil;

/**
 * Class which defines Thor upgrade for Sword. (thunder enemy on hit).
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class UpgradeThor extends UpgradeSword {
	public ItemStack getIngredient() {
		return ItemStackUtil.getHead(1, 4);
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

	public void onAttack(EntityPlayer player, Entity target) {
		if (target instanceof EntityLivingBase) {
			WeatherUtil.spawnLightningBolt(player.world, target.posX, target.posY, target.posZ);
		}
	}
}