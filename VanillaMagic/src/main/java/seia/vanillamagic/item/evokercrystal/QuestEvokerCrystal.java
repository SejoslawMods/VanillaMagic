package seia.vanillamagic.item.evokercrystal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.event.EventSpell;
import seia.vanillamagic.api.magic.IEvokerSpell;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.util.EntityUtil;

public class QuestEvokerCrystal extends Quest
{
	/**
	 * On death Evoker should drop Evoker Crystal.
	 */
	@SubscribeEvent
	public void evokerDropCrystal(LivingDropsEvent event)
	{
		EntityLivingBase dyingEntity = event.getEntityLiving();
		if (dyingEntity instanceof EntityEvoker)
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
		if (ItemStack.areItemsEqualIgnoreDurability(event.getItem().getItem(), VanillaMagicItems.EVOKER_CRYSTAL.getItem()))
		{
			EntityPlayer player = event.getEntityPlayer();
			if (canAddStat(player)) addStat(player);
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
		if (!hasQuest(caster)) return;
		Entity target = EntityUtil.getClosestLookingAt(caster, 1000.0D);
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
		if (!hasQuest(caster)) return;
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
		if(!hasQuest(caster)) return;
		Entity target = event.getTarget();
		useCrystal(caster, target);
	}
	
	/**
	 * Use Evoker Crystal
	 */
	public void useCrystal(EntityPlayer player, Entity target)
	{
		useCrystal(player, target, player.isSneaking());
	}
	
	/**
	 * Use Evoker Crystal
	 */
	public void useCrystal(EntityPlayer player, Entity target, boolean isSneaking)
	{
		if (hasQuest(player))
		{
			ItemStack crystal = player.getHeldItemMainhand();
			if (VanillaMagicItems.isCustomItem(crystal, VanillaMagicItems.EVOKER_CRYSTAL))
			{
				if (isSneaking) EvokerSpellRegistry.changeSpell(player, crystal); // Change Spell
				else // Use Spell
				{
					IEvokerSpell spell = EvokerSpellRegistry.getCurrentSpell(crystal);
					if (!MinecraftForge.EVENT_BUS.post(new EventSpell.Cast.EvokerSpell(
							player, player.world, target, spell)))
					{
						spell.castSpell(player, target);
					}
				}
			}
		}
	}
}