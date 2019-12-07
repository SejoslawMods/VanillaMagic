package com.github.sejoslaw.vanillamagic.quest.upgrade.toolupgrade;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import com.github.sejoslaw.vanillamagic.api.upgrade.toolupgrade.ToolRegistry;
import com.github.sejoslaw.vanillamagic.quest.QuestSpawnOnCauldron;

/**
 * Class which is a definition of ToolUpgrade Quest. (upgrade tool to better
 * version - better resource).
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestToolUpgrade extends QuestSpawnOnCauldron {
	public boolean isBaseItem(ItemEntity entityItem) {
		for (int i = 0; i < ToolRegistry.size(); ++i) {
			if (ItemStack.areItemsEqualIgnoreDurability(ToolRegistry.getBaseTool(i), entityItem.getItem())) {
				return true;
			}
		}

		return false;
	}

	public boolean canGetUpgrade(ItemStack base) {
		return true;
	}

	public ItemStack getResultSingle(ItemEntity base, ItemEntity ingredient) {
		return ToolRegistry.getResult(base.getItem(), ingredient.getItem());
	}
}