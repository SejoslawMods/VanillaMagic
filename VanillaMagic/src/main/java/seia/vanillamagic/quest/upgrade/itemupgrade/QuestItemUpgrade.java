package seia.vanillamagic.quest.upgrade.itemupgrade;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import seia.vanillamagic.api.upgrade.itemupgrade.IItemUpgrade;
import seia.vanillamagic.quest.QuestSpawnOnCauldron;
import seia.vanillamagic.quest.upgrade.itemupgrade.ItemUpgradeRegistry.ItemEntry;
import seia.vanillamagic.util.ItemStackHelper;

public class QuestItemUpgrade extends QuestSpawnOnCauldron
{
	public boolean canGetUpgrade(ItemStack base) 
	{
		base.setStackDisplayName(base.getDisplayName() + " ");
		NBTTagCompound tag = base.getTagCompound();
		if(tag == null)
		{
			return false;
		}
		return !tag.getBoolean(IItemUpgrade.NBT_ITEM_CONTAINS_UPGRADE);
	}
	
	public boolean isBaseItem(EntityItem entityItem) 
	{
		for(ItemEntry ie : ItemUpgradeRegistry.getBaseItems())
		{
			if(entityItem.getEntityItem().getItem() == ie.item)
			{
				if(ItemStackHelper.getStackSize(entityItem.getEntityItem()) == ItemStackHelper.getStackSize(ie.stack))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public ItemStack getResultSingle(EntityItem base, EntityItem ingredient)
	{
		return ItemUpgradeRegistry.getResult(base.getEntityItem(), ingredient.getEntityItem());
	}
}