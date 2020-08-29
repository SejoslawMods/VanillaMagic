package com.github.sejoslaw.vanillamagic2.common.handlers.clients;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.handlers.EventHandler;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@OnlyIn(Dist.CLIENT)
public class ShowDurabilityTooltipHandler extends EventHandler {
    @SubscribeEvent
    public void showTooltip(ItemTooltipEvent event) {
        this.onShowTooltip(event, VMForgeConfig.SHOW_DURABILITY_TOOLTIP.get(), (player, stack, tooltips) ->
                this.forDamageable(stack, () ->
                        tooltips.add(TextUtils.toComponent(this.getDurabilityString(stack)))));
    }

    private String getDurabilityString(ItemStack stack) {
        String message = TextUtils.getFormattedText("vm.handler.durability") + " ";

        int max = stack.getMaxDamage();
        int damage = stack.getDamage();
        float percentage = 1 - ((float) damage / (float) max);

        if (percentage >= .9) {
            message += TextFormatting.LIGHT_PURPLE;
        }
        if (percentage >= .8) {
            message += TextFormatting.DARK_PURPLE;
        }
        if (percentage >= .7) {
            message += TextFormatting.BLUE;
        }
        if (percentage >= .6) {
            message += TextFormatting.DARK_AQUA;
        }
        if (percentage >= .5) {
            message += TextFormatting.DARK_GREEN;
        }
        if (percentage >= .4) {
            message += TextFormatting.GREEN;
        }
        if (percentage >= .3) {
            message += TextFormatting.YELLOW;
        }
        if (percentage >= .2) {
            message += TextFormatting.GOLD;
        }
        if (percentage >= .1) {
            message += TextFormatting.RED;
        }
        if (percentage < .1) {
            message += TextFormatting.DARK_RED;
        }

        return message + (max - damage) + " / " + max + TextFormatting.RESET;
    }
}
