package com.github.sejoslaw.vanillamagic.common.itemupgrade;

import java.util.List;

import com.github.sejoslaw.vanillamagic.api.upgrade.itemupgrade.IItemUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Basic implementation of ItemUpgrade. Extending this class will show upgrades
 * in tooltip list.
 */
public abstract class ItemUpgradeBase implements IItemUpgrade {
    /**
     * Tooltip header
     */
    private final String _tooltipInfo = "ItemUpgrades:";

    /**
     * @return Returns the color is which upgrade name should be displayed.
     */
    public String getTextColor() {
        return "�7"; // Grey
    }

    @SubscribeEvent
    public void showUpgradeTooltip(ItemTooltipEvent event) {
        ItemStack upgradeStack = event.getItemStack();
        if (!containsTag(upgradeStack)) {
            return;
        }

        List<ITextComponent> tooltips = event.getToolTip();

        // Add header info
        if (!tooltips.contains(_tooltipInfo)) {
            tooltips.add(new StringTextComponent(_tooltipInfo));
        }

        // Add upgrade info
        tooltips.add(new StringTextComponent(this.getTextColor() + "     " + this.getUpgradeName()));
    }
}