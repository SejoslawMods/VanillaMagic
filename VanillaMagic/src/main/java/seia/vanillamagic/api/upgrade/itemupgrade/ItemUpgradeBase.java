package seia.vanillamagic.api.upgrade.itemupgrade;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Basic implementation of ItemUpgrade.
 * Extending this class will show upgrades in tooltip list.
 */
public abstract class ItemUpgradeBase implements IItemUpgrade
{
	/**
	 * Tooltip header
	 */
	private final String _tooltipInfo = "ItemUpgrades:";
	
	/**
	 * @return Returns the color is which upgrade name should be displayed.
	 */
	public String getTextColor()
	{
		return "§7"; // Grey
	}
	
	@SubscribeEvent
	public void showUpgradeTooltip(ItemTooltipEvent event)
	{
		// ItemStack with upgrade
		ItemStack upgradeStack = event.getItemStack();
		// If upgradeStack does not have required tag, quit
		if (!containsTag(upgradeStack)) return;
		// Tooltips of this upgradeStack
		List<String> tooltips = event.getToolTip();
		// Add header info
		if (!tooltips.contains(_tooltipInfo)) tooltips.add(_tooltipInfo);
		// Add upgrade info
		tooltips.add(this.getTextColor() + "     " + this.getUpgradeName());
	}
}