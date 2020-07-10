package com.github.sejoslaw.vanillamagic.common.item.evokercrystal;

import com.github.sejoslaw.vanillamagic.api.event.EventSpell;
import com.github.sejoslaw.vanillamagic.api.magic.IEvokerSpell;
import com.github.sejoslaw.vanillamagic.common.quest.Quest;
import com.github.sejoslaw.vanillamagic.common.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.core.VMItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestEvokerCrystal extends Quest {
	/**
	 * On death Evoker should drop Evoker Crystal.
	 */
	@SubscribeEvent
	public void evokerDropCrystal(LivingDropsEvent event) {
		LivingEntity dyingEntity = event.getEntityLiving();

		if (!(dyingEntity instanceof EvokerEntity)) {
			return;
		}

		ItemStack evokerCrystal = VMItems.EVOKER_CRYSTAL.getItem();
		ItemEntity droppedCrystal = new ItemEntity(dyingEntity.world, dyingEntity.getPosX(), dyingEntity.getPosY(), dyingEntity.getPosZ(), evokerCrystal);
		event.getDrops().add(droppedCrystal);
	}

	/**
	 * Player gets the Quest when he picked up the Evoker Crystal.
	 */
	@SubscribeEvent
	public void pickupEvokerCrystal(EntityItemPickupEvent event) {
		ItemStack eventStack = event.getItem().getItem();

		if (!ItemStack.areItemsEqualIgnoreDurability(eventStack, VMItems.EVOKER_CRYSTAL.getItem())) {
			return;
		}

		PlayerEntity player = event.getPlayer();

		if (canAddStat(player)) {
			addStat(player);
		}
	}

	/**
	 * If Evoker Crystal in Right-Hand: <br>
	 * Shift-Right-Click -> Change Spell <br>
	 * Right-Click -> Use Spell <br>
	 */
	@SubscribeEvent
	public void useCrystal(PlayerInteractEvent.RightClickItem event) {
		PlayerEntity caster = event.getPlayer();

		if (!hasQuest(caster)) {
			return;
		}

		Entity target = EntityUtil.getClosestLookingAt(caster, 1000.0D);
		useCrystal(caster, target);
	}

	/**
	 * If Evoker Crystal in Right-Hand: <br>
	 * Shift-Right-Click -> Change Spell <br>
	 * Right-Click -> Use Spell <br>
	 */
	@SubscribeEvent
	public void onShortRangeClickEntity(PlayerInteractEvent.EntityInteractSpecific event) {
		PlayerEntity caster = event.getPlayer();

		if (!hasQuest(caster)) {
			return;
		}

		Entity target = event.getTarget();
		useCrystal(caster, target);
	}

	/**
	 * If Evoker Crystal in Right-Hand: <br>
	 * Shift-Right-Click -> Change Spell <br>
	 * Right-Click -> Use Spell <br>
	 */
	@SubscribeEvent
	public void onShortRangeClickEntity(PlayerInteractEvent.EntityInteract event) {
		PlayerEntity caster = event.getPlayer();

		if (!hasQuest(caster)) {
			return;
		}

		Entity target = event.getTarget();
		useCrystal(caster, target);
	}

	/**
	 * Use Evoker Crystal
	 */
	public void useCrystal(PlayerEntity player, Entity target) {
		useCrystal(player, target, player.isSneaking());
	}

	/**
	 * Use Evoker Crystal
	 */
	public void useCrystal(PlayerEntity player, Entity target, boolean isSneaking) {
		if (!hasQuest(player)) {
			return;
		}

		ItemStack crystal = player.getHeldItemMainhand();
		if (!VMItems.isCustomItem(crystal, VMItems.EVOKER_CRYSTAL)) {
			return;
		}

		if (isSneaking) {
			EvokerSpellRegistry.changeSpell(player, crystal);
		} else {
			IEvokerSpell spell = EvokerSpellRegistry.getCurrentSpell(crystal);

			if (!EventUtil.postEvent(new EventSpell.Cast.EvokerSpell(player, player.world, target, spell))) {
				spell.castSpell(player, target);
			}
		}
	}
}
