package seia.vanillamagic.magic.spell.spells;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.api.util.VectorWrapper;
import seia.vanillamagic.api.util.VectorWrapper.Vector3D;
import seia.vanillamagic.magic.spell.Spell;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellLargeFireball extends Spell {
	public SpellLargeFireball(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(EntityPlayer caster, BlockPos pos, EnumFacing face, Vector3D hitVec) {
		// Cast while NOT looking at block
		if (pos == null) {
			return false;
		}

		World world = caster.world;
		world.playEvent(caster, 1016, new BlockPos((int) caster.posX, (int) caster.posY, (int) caster.posZ), 0);

		Vector3D lookingAt = VectorWrapper.wrap(caster.getLookVec());
		double accelX = lookingAt.getX();
		double accelY = lookingAt.getY();
		double accelZ = lookingAt.getZ();

		EntityLargeFireball fireball = new EntityLargeFireball(world, caster.posX + accelX, caster.posY + 1.5D + accelY,
				caster.posZ + accelZ, accelX, accelY, accelZ);
		fireball.shootingEntity = caster;
		fireball.motionX = 0.0D;
		fireball.motionY = 0.0D;
		fireball.motionZ = 0.0D;

		double d0 = (double) MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
		fireball.accelerationX = accelX / d0 * 0.1D;
		fireball.accelerationY = accelY / d0 * 0.1D;
		fireball.accelerationZ = accelZ / d0 * 0.1D;

		world.spawnEntity(fireball);
		world.updateEntities();

		return true;
	}
}