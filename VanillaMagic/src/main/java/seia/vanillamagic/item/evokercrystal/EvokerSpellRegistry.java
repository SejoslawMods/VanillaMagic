package seia.vanillamagic.item.evokercrystal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import seia.vanillamagic.api.magic.IEvokerSpell;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.item.evokercrystal.spell.EvokerSpellFangAttack;
import seia.vanillamagic.item.evokercrystal.spell.EvokerSpellSummonVex;
import seia.vanillamagic.item.evokercrystal.spell.EvokerSpellWololo;

public class EvokerSpellRegistry 
{
	private static List<IEvokerSpell> _SPELLS = new ArrayList<>();
	
	private static IEvokerSpell SPELL_FANGS = new EvokerSpellFangAttack();
	private static IEvokerSpell SPELL_SUMMON_VEX = new EvokerSpellSummonVex();
	private static IEvokerSpell SPELL_WOLOLO = new EvokerSpellWololo();
	
	private EvokerSpellRegistry()
	{
	}
	
	public static void add(IEvokerSpell spell)
	{
		_SPELLS.add(spell);
	}
	
	@Nullable
	public static IEvokerSpell getSpell(int spellID)
	{
		for(IEvokerSpell spell : _SPELLS)
		{
			if(spell.getSpellID() == spellID)
			{
				return spell;
			}
		}
		return null;
	}
	
	@Nullable
	public static IEvokerSpell getCurrentSpell(ItemStack stack)
	{
		if(VanillaMagicItems.isCustomItem(stack, VanillaMagicItems.EVOKER_CRYSTAL))
		{
			NBTTagCompound stackTag = stack.getTagCompound();
			int spellID = stackTag.getInteger(ItemEvokerCrystal.NBT_SPELL_ID);
			return getSpell(spellID);
		}
		return null;
	}

	public static void changeSpell(EntityPlayer player, ItemStack crystal)
	{
		if(player == null || crystal == null)
		{
			return;
		}
		IEvokerSpell currentSpell = getCurrentSpell(crystal);
		int spellID = currentSpell.getSpellID();
		spellID++;
		IEvokerSpell nextSpell = getSpell(spellID);
		if(nextSpell == null)
		{
			nextSpell = getSpell(1);
		}
		ItemStack newCrystal = crystal.copy();
		newCrystal.getTagCompound().setInteger(ItemEvokerCrystal.NBT_SPELL_ID, nextSpell.getSpellID());
		newCrystal.setStackDisplayName(VanillaMagicItems.EVOKER_CRYSTAL.getItemName() + ": " + nextSpell.getSpellName());
		player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, newCrystal);
	}
}