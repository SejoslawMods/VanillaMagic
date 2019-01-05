package seia.vanillamagic.magic.spell.spells;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.api.util.VectorWrapper;
import seia.vanillamagic.api.util.VectorWrapper.Vector3D;
import seia.vanillamagic.entity.EntitySpellTeleport;
import seia.vanillamagic.magic.spell.Spell;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellTeleport extends Spell {
	public SpellTeleport(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(EntityPlayer caster, BlockPos pos, EnumFacing face, Vector3D hitVec) {
		if (pos != null) {
			return false;
		}

		World world = caster.world;

		Vector3D lookingAt = VectorWrapper.wrap(caster.getLookVec());
		double accelX = lookingAt.getX();
		double accelY = lookingAt.getY();
		double accelZ = lookingAt.getZ();

		EntitySpellTeleport spellTeleport = new EntitySpellTeleport(world, caster, accelX, accelY, accelZ);

		world.spawnEntity(spellTeleport);
		world.updateEntities();

		return true;
	}
}