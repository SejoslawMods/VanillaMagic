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
public class ShowHungerTooltipHandler extends EventHandler {
    @SubscribeEvent
    public void showTooltip(ItemTooltipEvent event) {
        this.onShowTooltip(event, VMForgeConfig.SHOW_HUNGER_TOOLTIP.get(), (player, stack, tooltips) ->
                this.withFood(stack, food ->
                        tooltips.add(TextUtils.toComponent(this.getHungerString(food.getHealing())))));
    }

    private String getHungerString(int hunger) {
        String message = TextFormatting.GRAY + TextUtils.translate("vm.handler.hunger").getFormattedText() + " ";

        for (int i = 0; i < (hunger / 2); i++) {
            message += TextFormatting.DARK_RED + this.squareSymbol;
        }

        if (hunger % 2 != 0) {
            message += TextFormatting.RED + this.squareSymbol;
        }

        return message;
    }
}
