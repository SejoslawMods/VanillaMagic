package seia.vanillamagic.item.book;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import seia.vanillamagic.api.upgrade.itemupgrade.IItemUpgrade;
import seia.vanillamagic.quest.upgrade.itemupgrade.ItemUpgradeRegistry;
import seia.vanillamagic.util.CraftingUtil;
import seia.vanillamagic.util.TextUtil;

public class BookItemUpgrade implements IBook
{
	public int getUID() 
	{
		return BookRegistry.BOOK_ITEM_UPGRADES_UID;
	}
	
	public void registerRecipe() 
	{
		CraftingUtil.addShapedRecipe(getItem(), new Object[]{
				"B  ",
				" B ",
				"  B",
				'B', Items.BOOK
		});
	}
	
	public ItemStack getItem() 
	{
		ItemStack book = new ItemStack(BookRegistry.BOOK_ITEM);
		NBTTagCompound data = new NBTTagCompound();
		{
			// Constructing TagCompound
			NBTTagList pages = new NBTTagList();
			{		
				// Pages
				pages.appendTag(new NBTTagString(
						"\n\n\n\n" + BookRegistry.COLOR_TITLE + "==== " + TextUtil.translateToLocal("book.itemUpgrades.title") + " ====" + 
						TextUtil.getEnters(4) + "-" + BookRegistry.AUTHOR + " " + BookRegistry.YEAR
						));
				Map<String, List<IItemUpgrade>> map = ItemUpgradeRegistry.getUpgradesMap();
				Set<Entry<String, List<IItemUpgrade>>> set = map.entrySet();
				Iterator<Entry<String, List<IItemUpgrade>>> iterator = set.iterator();
				while (iterator.hasNext())
				{
					Entry<String, List<IItemUpgrade>> entry = iterator.next();
					String entryKey = entry.getKey();
					String key = ItemUpgradeRegistry.getLocalizedNameForMapping(entryKey);
					List<IItemUpgrade> values = entry.getValue();
					for (int i = 0; i < values.size(); ++i)
					{
						IItemUpgrade upgrade = values.get(i);
						pages.appendTag(new NBTTagString(
								BookRegistry.COLOR_HEADER + 
								"Key: " + key + 
								TextUtil.getEnters(2) + 
								"§0" +
								"Upgrade name: " + upgrade.getUpgradeName() + TextUtil.getEnters(2) +
								"Ingredient item: " + upgrade.getIngredient().getDisplayName()
								));
					}
				}
			}
			data.setTag("pages", pages);
			data.setString("author", BookRegistry.AUTHOR);
			data.setString("title", BookRegistry.BOOK_NAME_ITEM_UPGRADES);
			data.setInteger(BookRegistry.BOOK_NBT_UID, getUID());
		}
		book.setTagCompound(data);
		book.setStackDisplayName(BookRegistry.BOOK_NAME_ITEM_UPGRADES);
		return book;
	}
}