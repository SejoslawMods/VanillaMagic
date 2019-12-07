package com.github.sejoslaw.vanillamagic.magic.spell.spells.summon.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellSummonSkeleton extends SpellSummonHostileWithRandomArmor {
	public SpellSummonSkeleton(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public Entity getEntity(World world) {
		EntitySkeleton entity = new EntitySkeleton(world);
		entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
		return entity;
	}

	public Entity getHorse(World world) {
		return new EntitySkeletonHorse(world);
	}
}