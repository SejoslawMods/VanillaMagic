package seia.vanillamagic.magic.spell.spells;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import seia.vanillamagic.magic.spell.Spell;
import seia.vanillamagic.magic.wand.IWand;

public class SpellLargeFireball extends Spell
{
	public SpellLargeFireball(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(EntityPlayer caster, BlockPos pos, EnumFacing face, Vec3d hitVec) 
	{
		if(pos == null) // casting while NOT looking at block
		{
			World world = caster.world;
			world.playEvent(caster, 1016, new BlockPos((int)caster.posX, (int)caster.posY, (int)caster.posZ), 0);
			Vec3d lookingAt = caster.getLookVec();
			double accelX = lookingAt.xCoord;
			double accelY = lookingAt.yCoord;
			double accelZ = lookingAt.zCoord;
			EntityLargeFireball fireball = new EntityLargeFireball(world, 
					caster.posX + accelX, 
					caster.posY + 1.5D + accelY, 
					caster.posZ + accelZ,
					accelX, accelY, accelZ);
			fireball.shootingEntity = caster;
			fireball.motionX = 0.0D;
			fireball.motionY = 0.0D;
			fireball.motionZ = 0.0D;
			double d0 = (double)MathHelper.sqrt(accelX * accelX + 
					accelY * accelY + 
					accelZ * accelZ);
			fireball.accelerationX = accelX / d0 * 0.1D;
			fireball.accelerationY = accelY / d0 * 0.1D;
			fireball.accelerationZ = accelZ / d0 * 0.1D;
			world.spawnEntity(fireball);
			world.updateEntities();
			return true;
		}
		return false;
	}
}