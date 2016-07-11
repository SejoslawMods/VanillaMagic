package seia.vanillamagic.utils.spell;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public enum EnumSpell
{
	LIGHTER(0, "Flint and Steel Clone", "spellFlintAndSteel", EnumWand.STICK, new ItemStack(Items.COAL));
	
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