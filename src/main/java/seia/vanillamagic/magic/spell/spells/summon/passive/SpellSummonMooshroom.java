package seia.vanillamagic.magic.spell.spells.summon.passive;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import seia.vanillamagic.api.magic.IWand;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellSummonMooshroom extends SpellSummonPassive {
	public SpellSummonMooshroom(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public Entity getEntity(World world) {
		return new EntityMooshroom(world);
	}
}