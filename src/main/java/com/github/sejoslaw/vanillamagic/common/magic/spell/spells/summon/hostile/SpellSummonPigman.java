package com.github.sejoslaw.vanillamagic.magic.spell.spells.summon.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellSummonPigman extends SpellSummonHostile {
	public SpellSummonPigman(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public Entity getEntity(World world) {
		EntityPigZombie entity = new EntityPigZombie(world);
		entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
		return entity;
	}
}