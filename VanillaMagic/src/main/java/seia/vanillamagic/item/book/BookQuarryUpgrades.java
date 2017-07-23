package seia.vanillamagic.item.book;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.api.tileentity.machine.IQuarryUpgrade;
import seia.vanillamagic.tileentity.machine.quarry.QuarryUpgradeRegistry;
import seia.vanillamagic.util.TextHelper;

public class BookQuarryUpgrades implements IBook
{
	public int getUID() 
	{
		return BookRegistry.BOOK_QUARRY_UPGRADES_UID;
	}
	
	public void registerRecipe() 
	{
		GameRegistry.addRecipe(getItem(), new Object[]{
				"  B",
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
						"\n\n\n\n" + BookRegistry.COLOR_TITLE + "==== " + TextHelper.translateToLocal("book.quarryUpgrades.title") + " ====" + 
						TextHelper.getEnters(4) + "-" + BookRegistry.AUTHOR + " " + BookRegistry.YEAR
						));
				for(IQuarryUpgrade iqu : QuarryUpgradeRegistry.getUpgrades())
				{
					pages.appendTag(new NBTTagString(
							BookRegistry.COLOR_HEADER + 
							iqu.getUpgradeName() + TextHelper.getEnters(2) + 
							"§0" +
							"Block: " + ForgeRegistries.BLOCKS.getKey(iqu.getBlock()).getResourcePath()
							));
				}
			}
			data.setTag("pages", pages);
			data.setString("author", BookRegistry.AUTHOR);
			data.setString("title", BookRegistry.BOOK_NAME_QUARRY_UPGRADES);
			data.setInteger(BookRegistry.BOOK_NBT_UID, getUID());
		}
		book.setTagCompound(data);
		book.setStackDisplayName(BookRegistry.BOOK_NAME_QUARRY_UPGRADES);
		return book;
	}
}