package seia.vanillamagic.magic.spell.spells.summon.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import seia.vanillamagic.api.magic.IWand;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellSummonZombie extends SpellSummonHostileWithRandomArmor {
	public SpellSummonZombie(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public Entity getEntity(World world) {
		return new EntityZombie(world);
	}

	public Entity getHorse(World world) {
		return new EntityZombieHorse(world);
	}
}