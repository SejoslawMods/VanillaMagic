package seia.vanillamagic.item.book;

import static seia.vanillamagic.util.TextHelper.ENTER;

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
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.item.itemupgrade.ItemUpgradeRegistry;
import seia.vanillamagic.item.itemupgrade.upgrade.IItemUpgrade;
import seia.vanillamagic.util.TextHelper;

public class BookItemUpgrade implements IBook
{
	public int getUID() 
	{
		return 5;
	}
	
	public void registerRecipe() 
	{
		GameRegistry.addRecipe(getItem(), new Object[]{
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
						"\n\n\n\n" + BookRegistry.COLOR_TITLE + "==== " + TextHelper.translateToLocal("book.itemUpgrades.title") + " ====" + 
						TextHelper.getEnters(4) + "-" + BookRegistry.AUTHOR + " " + BookRegistry.YEAR
						));
				Map<String, List<IItemUpgrade>> map = ItemUpgradeRegistry.INSTANCE.getUpgradesMap();
				Set<Entry<String, List<IItemUpgrade>>> set = map.entrySet();
				Iterator<Entry<String, List<IItemUpgrade>>> iterator = set.iterator();
				while(iterator.hasNext())
				{
					Entry<String, List<IItemUpgrade>> entry = iterator.next();
					String key = entry.getKey();
					List<IItemUpgrade> values = entry.getValue();
					for(int i = 0; i < values.size(); i++)
					{
						IItemUpgrade upgrade = values.get(i);
						pages.appendTag(new NBTTagString(
								BookRegistry.COLOR_HEADER + 
								"Key: " + key + 
								TextHelper.getEnters(2) + 
								"§0" +
								"Upgrade name: " + upgrade.getUpgradeName() + TextHelper.getEnters(2) +
								"Ingedient item: " + upgrade.getIngredient().getItem().getUnlocalizedName()
								));
					}
				}
			}
			data.setTag("pages", pages);
			data.setString("author", BookRegistry.AUTHOR);
			data.setString("title", BookRegistry.BOOK_NAME_ALTAR_CRAFTING);
			data.setInteger(BookRegistry.BOOK_NBT_UID, getUID());
		}
		book.setTagCompound(data);
		book.setStackDisplayName(BookRegistry.BOOK_ITEM_UPGRADES);
		return book;
	}
}