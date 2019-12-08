package com.github.sejoslaw.vanillamagic.common.event;

import com.github.sejoslaw.vanillamagic.common.config.VMConfig;
import com.github.sejoslaw.vanillamagic.common.util.TextUtil;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
    @OnlyIn(Dist.CLIENT)
    public void registerTweak(ItemTooltipEvent event) {
        if ((!VMConfig.SHOW_ADDITIONAL_TOOLTIPS.get()) || (event.getItemStack() == null)) {
            return;
        }

        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        Food food = item.getFood();

        if (food == null) {
            return;
        }

        int hunger = food.getHealing();
        float saturation = food.getSaturation() * 10;

        event.getToolTip().add(TextUtil.wrap(getHungerString(hunger)));
        event.getToolTip().add(TextUtil.wrap(getSaturationString(saturation)));

        if (item.isDamageable()) {
            event.getToolTip().add(TextUtil.wrap(getDurabilityString(stack)));
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
        int damage = itemstack.getDamage();
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