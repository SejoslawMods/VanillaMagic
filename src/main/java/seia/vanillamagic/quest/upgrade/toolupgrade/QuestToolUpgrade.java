package seia.vanillamagic.quest.upgrade.toolupgrade;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.api.upgrade.toolupgrade.ToolRegistry;
import seia.vanillamagic.quest.QuestSpawnOnCauldron;

/**
 * Class which is a definition of ToolUpgrade Quest. (upgrade tool to better version - better resource)
 */
public class QuestToolUpgrade extends QuestSpawnOnCauldron
{
	public boolean isBaseItem(EntityItem entityItem) 
	{
		for (int i = 0; i < ToolRegistry.size(); ++i)
			if (ItemStack.areItemsEqualIgnoreDurability(ToolRegistry.getBaseTool(i), entityItem.getItem()))
				return true;
		return false;
	}
	
	public boolean canGetUpgrade(ItemStack base) 
	{
		return true;
	}
	
	public ItemStack getResultSingle(EntityItem base, EntityItem ingredient)
	{
		return ToolRegistry.getResult(base.getItem(), ingredient.getItem());
	}
}