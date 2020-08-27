package com.github.sejoslaw.vanillamagic2.common.handlers.clients;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.handlers.EventHandler;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@OnlyIn(Dist.CLIENT)
public class ShowSaturationTooltipHandler extends EventHandler {
    @SubscribeEvent
    public void showTooltip(ItemTooltipEvent event) {
        this.onShowTooltip(event, VMForgeConfig.SHOW_SATURATION_TOOLTIP.get(), (player, stack, tooltips) ->
                this.withFood(stack, food ->
                        tooltips.add(TextUtils.toComponent(this.getSaturationString(food.getSaturation())))));
    }

    private String getSaturationString(float saturation) {
        String message = TextFormatting.GRAY + TextUtils.translate("vm.handler.saturation").getFormattedText() + " ";

        if (saturation <= 1) {
            message += TextFormatting.DARK_RED;
        }
        if (saturation <= 2) {
            message += TextFormatting.RED;
        }
        if (saturation <= 3) {
            message += TextFormatting.GOLD;
        }
        if (saturation <= 4) {
            message += TextFormatting.YELLOW;
        }
        if (saturation <= 5) {
            message += TextFormatting.GREEN;
        }
        if (saturation <= 6) {
            message += TextFormatting.DARK_GREEN;
        }
        if (saturation <= 7) {
            message += TextFormatting.DARK_AQUA;
        }
        if (saturation <= 8) {
            message += TextFormatting.BLUE;
        }
        if (saturation <= 9) {
            message += TextFormatting.DARK_PURPLE;
        }

        return message + this.squareSymbol;
    }
}
