package seia.vanillamagic.item.evokercrystal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.item.evokercrystal.spell.IEvokerSpell;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.util.EntityHelper;

public class QuestEvokerCrystal extends Quest
{
	/**
	 * On death Evoker should drop Evoker Crystal.
	 */
	@SubscribeEvent
	public void evokerDropCrystal(LivingDropsEvent event)
	{
		EntityLivingBase dyingEntity = event.getEntityLiving();
		if(dyingEntity instanceof EntityEvoker)
		{
			ItemStack evokerCrystal = VanillaMagicItems.EVOKER_CRYSTAL.getItem();
			EntityItem droppedCrystal = new EntityItem(dyingEntity.world, dyingEntity.posX, dyingEntity.posY, dyingEntity.posZ, evokerCrystal);
			event.getDrops().add(droppedCrystal);
		}
	}
	
	/**
	 * Player gets the Achievement when he picked up the Evoker Crystal.
	 */
	@SubscribeEvent
	public void pickupEvokerCrystal(EntityItemPickupEvent event)
	{
		if(ItemStack.areItemsEqualIgnoreDurability(event.getItem().getEntityItem(), VanillaMagicItems.EVOKER_CRYSTAL.getItem()))
		{
			EntityPlayer player = event.getEntityPlayer();
			if(canPlayerGetAchievement(player))
			{
				player.addStat(achievement, 1);
			}
		}
	}
	
	/**
	 * If Evoker Crystal in Right-Hand: <br>
	 * Shift-Right-Click -> Change Spell <br>
	 * Right-Click -> Use Spell <br>
	 */
	@SubscribeEvent
	public void useCrystal(RightClickItem event)
	{
		EntityPlayer caster = event.getEntityPlayer();
		if(!caster.hasAchievement(achievement))
		{
			return;
		}
		Entity target = EntityHelper.getClosestLookingAt(caster, 1000.0D);
		useCrystal(caster, target);
	}
	
	/**
	 * If Evoker Crystal in Right-Hand: <br>
	 * Shift-Right-Click -> Change Spell <br>
	 * Right-Click -> Use Spell <br>
	 */
	@SubscribeEvent
	public void onShortRangeClickEntity(EntityInteractSpecific event)
	{
		EntityPlayer caster = event.getEntityPlayer();
		if(!caster.hasAchievement(achievement))
		{
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
	public void onShortRangeClickEntity(EntityInteract event)
	{
		EntityPlayer caster = event.getEntityPlayer();
		if(!caster.hasAchievement(achievement))
		{
			return;
		}
		Entity target = event.getTarget();
		useCrystal(caster, target);
	}
	
	public void useCrystal(EntityPlayer player, Entity target)
	{
		useCrystal(player, target, player.isSneaking());
	}
	
	public void useCrystal(EntityPlayer player, Entity target, boolean isSneaking)
	{
		if(player.hasAchievement(achievement))
		{
			ItemStack crystal = player.getHeldItemMainhand();
			if(VanillaMagicItems.isCustomItem(crystal, VanillaMagicItems.EVOKER_CRYSTAL))
			{
				if(isSneaking) // Change Spell
				{
					EvokerSpellRegistry.changeSpell(player, crystal);
				}
				else // Use Spell
				{
					IEvokerSpell spell = EvokerSpellRegistry.getCurrentSpell(crystal);
					spell.castSpell(player, target);
				}
			}
		}
	}
}