package com.github.sejoslaw.vanillamagic2.common.handlers.clients;

import com.github.sejoslaw.vanillamagic2.common.handlers.EventHandler;
import com.github.sejoslaw.vanillamagic2.common.itemupgrades.ItemUpgrade;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemUpgradeRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@OnlyIn(Dist.CLIENT)
public class ShowItemUpgradesTooltipHandler extends EventHandler {
    @SubscribeEvent
    public void showTooltip(ItemTooltipEvent event) {
        this.onShowTooltip(event, true, (player, stack, tooltips) -> {
            List<ItemUpgrade> upgrades = ItemUpgradeRegistry.getInstalledUpgrades(stack);

            if (upgrades.size() > 0) {
                TextUtils.addLine(tooltips, "quest.tooltip.upgrades", "");
                upgrades.forEach(upgrade -> TextUtils.addLine(tooltips, "", upgrade.getClass().getSimpleName()));
            }
        });
    }
}
