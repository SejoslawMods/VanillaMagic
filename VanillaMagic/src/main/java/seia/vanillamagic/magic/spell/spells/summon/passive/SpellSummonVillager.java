package seia.vanillamagic.magic.spell.spells.summon.passive;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import seia.vanillamagic.magic.wand.IWand;

public class SpellSummonVillager extends SpellSummonPassive 
{
	public SpellSummonVillager(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public Entity getEntity(World world) 
	{
		EntityVillager entity = new EntityVillager(world);
		VillagerRegistry.setRandomProfession(entity, new Random());
		return entity;
	}
}