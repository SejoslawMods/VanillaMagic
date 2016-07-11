package seia.vanillamagic.utils.spell;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.utils.ItemStackHelper;

public enum EnumSpell
{
	LIGHTER(0, "Flint and Steel Clone", "spellFlintAndSteel", EnumWand.STICK, new ItemStack(Items.COAL)),
	SMALL_FIREBALL(1, "Feel like Blaze", "spellSmallFireball", EnumWand.BLAZE_ROD, new ItemStack(Items.REDSTONE, 2)),
	LARGE_FIREBALL(2, "Feel like Ghast", "spellLargeFireball", EnumWand.BLAZE_ROD, new ItemStack(Items.GHAST_TEAR));
	
	public final int spellID;
	public final String spellName;
	public final String spellUniqueName;
	public final EnumWand minimalWandTier;
	public final ItemStack itemOffHand;
	
	EnumSpell(int spellID, String spellName, String spellUniqueName, EnumWand minimalWandTier, ItemStack itemOffHand)
	{
		this.spellID = spellID;
		this.spellName = spellName;
		this.spellUniqueName = spellUniqueName;
		this.minimalWandTier = minimalWandTier;
		this.itemOffHand = itemOffHand;
	}
	
	public boolean isItemOffHandRightForSpell(ItemStack stackOffHand)
	{
		return itemOffHand.getItem().equals(stackOffHand.getItem());
	}
	
	//================================================================================================
	
	public static EnumSpell getSpellById(int id)
	{
		EnumSpell[] spells = values();
		for(int i = 0; i < spells.length; i++)
		{
			if(spells[i].spellID == id)
			{
				return spells[i];
			}
		}
		return null;
	}
}