package com.github.sejoslaw.vanillamagic.item.evokercrystal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import com.github.sejoslaw.vanillamagic.api.magic.IEvokerSpell;
import com.github.sejoslaw.vanillamagic.item.VanillaMagicItems;
import com.github.sejoslaw.vanillamagic.item.evokercrystal.spell.EvokerSpellFangAttack;
import com.github.sejoslaw.vanillamagic.item.evokercrystal.spell.EvokerSpellSummonVex;
import com.github.sejoslaw.vanillamagic.item.evokercrystal.spell.EvokerSpellWololo;

/**
 * Class which contains data about all registered Evoker Spells.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class EvokerSpellRegistry {
	/**
	 * List with all registered Evoker Spells.
	 */
	private static List<IEvokerSpell> SPELLS = new ArrayList<>();

	static {
		SPELLS.add(new EvokerSpellFangAttack());
		SPELLS.add(new EvokerSpellSummonVex());
		SPELLS.add(new EvokerSpellWololo());
	}

	private EvokerSpellRegistry() {
	}

	/**
	 * Register new Evoker Spell.
	 */
	public static void add(IEvokerSpell spell) {
		SPELLS.add(spell);
	}

	/**
	 * @return Returns all Evoker Spells list.
	 */
	public static List<IEvokerSpell> getEvokerSpells() {
		return SPELLS;
	}

	/**
	 * @return Returns the Evoker Spell from the given ID.
	 */
	@Nullable
	public static IEvokerSpell getSpell(int spellID) {
		for (IEvokerSpell spell : SPELLS) {
			if (spell.getSpellID() == spellID) {
				return spell;
			}
		}
		return null;
	}

	/**
	 * @return Returns the current Evoker Spell from given ItemStack.
	 */
	@Nullable
	public static IEvokerSpell getCurrentSpell(ItemStack stack) {
		if (VanillaMagicItems.isCustomItem(stack, VanillaMagicItems.EVOKER_CRYSTAL)) {
			CompoundNBT stackTag = stack.getTagCompound();
			int spellID = stackTag.getInteger(ItemEvokerCrystal.NBT_SPELL_ID);
			return getSpell(spellID);
		}
		return null;
	}

	/**
	 * Change Evoker Spell to next one (on shift-right-click).
	 */
	public static void changeSpell(PlayerEntity player, ItemStack crystal) {
		if ((player == null) || (crystal == null)) {
			return;
		}

		IEvokerSpell currentSpell = getCurrentSpell(crystal);
		int spellID = currentSpell.getSpellID();
		spellID++;
		IEvokerSpell nextSpell = getSpell(spellID);

		if (nextSpell == null) {
			nextSpell = getSpell(1);
		}

		ItemStack newCrystal = crystal.copy();
		newCrystal.getTagCompound().setInteger(ItemEvokerCrystal.NBT_SPELL_ID, nextSpell.getSpellID());
		newCrystal
				.setDisplayName(VanillaMagicItems.EVOKER_CRYSTAL.getItemName() + ": " + nextSpell.getSpellName());
		player.setItemStackToSlot(EquipmentSlotType.MAINHAND, newCrystal);
	}
}