package com.github.sejoslaw.vanillamagic.event;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.github.sejoslaw.vanillamagic.config.VMConfig;

/**
 * Additional tooltips colors and information.<br>
 * <br>
 * NOTE !!! <br>
 * Colors and coloring algorithms remade from V-Tweaks.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ActionEventAdditionalToolTips {
	/**
	 * This symbol will be displayed in tooltip.
	 */
	private final String symbol = "\u25A0";

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerTweak(ItemTooltipEvent event) {
		if ((!VMConfig.SHOW_ADDITIONAL_TOOLTIPS) || (event.getItemStack() == null)) {
			return;
		}

		ItemStack stack = event.getItemStack();

		// Food tooltip
		if (stack.getItem() instanceof ItemFood) {
			ItemFood food = (ItemFood) stack.getItem();
			int hunger = food.getHealAmount(stack);
			float saturation = food.getSaturationModifier(stack) * 10;

			event.getToolTip().add(getHungerString(hunger));
			event.getToolTip().add(getSaturationString(saturation));
		}

		if (stack.getItem().isDamageable()) {
			event.getToolTip().add(getDurabilityString(stack));
		}
	}

	private String getHungerString(int hunger) {
		String ret = TextFormatting.GRAY + "Hunger: ";

		for (int i = 0; i < (hunger / 2); i++) {
			ret += TextFormatting.DARK_RED + symbol;
		}

		if (hunger % 2 != 0) {
			ret += TextFormatting.RED + symbol;
		}

		return ret;
	}

	private String getSaturationString(float saturation) {
		String ret = TextFormatting.GRAY + "Saturation: ";

		if (saturation <= 1) {
			return ret += TextFormatting.DARK_RED + symbol;
		}
		if (saturation <= 2) {
			return ret += TextFormatting.RED + symbol;
		}
		if (saturation <= 3) {
			return ret += TextFormatting.GOLD + symbol;
		}
		if (saturation <= 4) {
			return ret += TextFormatting.YELLOW + symbol;
		}
		if (saturation <= 5) {
			return ret += TextFormatting.GREEN + symbol;
		}
		if (saturation <= 6) {
			return ret += TextFormatting.DARK_GREEN + symbol;
		}
		if (saturation <= 7) {
			return ret += TextFormatting.DARK_AQUA + symbol;
		}
		if (saturation <= 8) {
			return ret += TextFormatting.BLUE + symbol;
		}

		if (saturation <= 9) {
			return ret += TextFormatting.DARK_PURPLE + symbol;
		} else {
			return ret += TextFormatting.LIGHT_PURPLE + symbol;
		}
	}

	private String getDurabilityString(ItemStack itemstack) {
		String ret = "Durability: ";
		int max = itemstack.getMaxDamage();
		int damage = itemstack.getItemDamage();
		float percentage = 1 - ((float) damage / (float) max);

		if (percentage >= .9) {
			return ret = ret + TextFormatting.LIGHT_PURPLE + (max - damage) + " / " + max + TextFormatting.RESET;
		}
		if (percentage >= .8) {
			return ret = ret + TextFormatting.DARK_PURPLE + (max - damage) + " / " + max + TextFormatting.RESET;
		}
		if (percentage >= .7) {
			return ret = ret + TextFormatting.BLUE + (max - damage) + " / " + max + TextFormatting.RESET;
		}
		if (percentage >= .6) {
			return ret = ret + TextFormatting.DARK_AQUA + (max - damage) + " / " + max + TextFormatting.RESET;
		}
		if (percentage >= .5) {
			return ret = ret + TextFormatting.DARK_GREEN + (max - damage) + " / " + max + TextFormatting.RESET;
		}
		if (percentage >= .4) {
			return ret = ret + TextFormatting.GREEN + (max - damage) + " / " + max + TextFormatting.RESET;
		}
		if (percentage >= .3) {
			return ret = ret + TextFormatting.YELLOW + (max - damage) + " / " + max + TextFormatting.RESET;
		}
		if (percentage >= .2) {
			return ret = ret + TextFormatting.GOLD + (max - damage) + " / " + max + TextFormatting.RESET;
		}
		if (percentage >= .1) {
			return ret = ret + TextFormatting.RED + (max - damage) + " / " + max + TextFormatting.RESET;
		}
		if (percentage < .1) {
			return ret = ret + TextFormatting.DARK_RED + (max - damage) + " / " + max + TextFormatting.RESET;
		}

		return ret = ret + (max - damage) + " / " + max;
	}
}