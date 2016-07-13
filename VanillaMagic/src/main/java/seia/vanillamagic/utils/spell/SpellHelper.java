package seia.vanillamagic.utils.spell;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import seia.vanillamagic.entity.EntitySpellSummonLightningBolt;
import seia.vanillamagic.entity.EntitySpellTeleport;
import seia.vanillamagic.entity.meteor.EntitySpellSummonMeteor;

/*
 * The work of each spell.
 * When using any of those methods we are sure that the Caster has got the right wand in hand.
 */
public class SpellHelper
{
	/**
	 * @return True - if the spell was casted correctly
	 */
	public static boolean castSpellById(int spellID, EntityPlayer caster, 
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(spellID == EnumSpell.LIGHTER.spellID)
		{
			return spellLighter(caster, pos, face, hitVec);
		}
		else if(spellID == EnumSpell.SMALL_FIREBALL.spellID)
		{
			return spellSmallFireball(caster, pos, face, hitVec);
		}
		else if(spellID == EnumSpell.LARGE_FIREBALL.spellID)
		{
			return spellLargeFireball(caster, pos, face, hitVec);
		}
		else if(spellID == EnumSpell.TELEPORT.spellID)
		{
			return spellTeleport(caster, pos, face, hitVec);
		}
		else if(spellID == EnumSpell.METEOR.spellID)
		{
			return spellSummonMeteor(caster, pos, face, hitVec);
		}
		else if(spellID == EnumSpell.LIGHTNING_BOLT.spellID)
		{
			return spellSummonLightningBolt(caster, pos, face, hitVec);
		}
		return false;
	}
	
	/*
	 * Flint and Steal Clone
	 */
	public static boolean spellLighter(EntityPlayer caster, 
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(pos != null) // RightClickBlock
		{
			World world = caster.worldObj;
			pos = pos.offset(face);
			if (world.isAirBlock(pos))
			{
				world.playSound(caster, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, new Random().nextFloat() * 0.4F + 0.8F);
				world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Feel like Blaze
	 */
	public static boolean spellSmallFireball(EntityPlayer caster, 
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(pos == null) // casting while NOT looking at block
		{
			World world = caster.worldObj;
			world.playEvent(caster, 1018, new BlockPos((int)caster.posX, (int)caster.posY, (int)caster.posZ), 0);
			Vec3d lookingAt = caster.getLookVec();
			double accelX = lookingAt.xCoord;
			double accelY = lookingAt.yCoord;
			double accelZ = lookingAt.zCoord;
			EntitySmallFireball fireball = new EntitySmallFireball(world, 
					caster.posX + accelX, 
					caster.posY + 1.5D + accelY, 
					caster.posZ + accelZ, 
					accelX, accelY, accelZ);
			fireball.shootingEntity = caster;
			fireball.motionX = 0.0D;
			fireball.motionY = 0.0D;
			fireball.motionZ = 0.0D;
			double d0 = (double)MathHelper.sqrt_double(accelX * accelX + 
					accelY * accelY + 
					accelZ * accelZ);
			fireball.accelerationX = accelX / d0 * 0.1D;
			fireball.accelerationY = accelY / d0 * 0.1D;
			fireball.accelerationZ = accelZ / d0 * 0.1D;
			world.spawnEntityInWorld(fireball);
			world.updateEntities();
			return true;
		}
		return false;
	}
	
	/*
	 * Feel like Ghast
	 */
	public static boolean spellLargeFireball(EntityPlayer caster,
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(pos == null) // casting while NOT looking at block
		{
			World world = caster.worldObj;
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
			double d0 = (double)MathHelper.sqrt_double(accelX * accelX + 
					accelY * accelY + 
					accelZ * accelZ);
			fireball.accelerationX = accelX / d0 * 0.1D;
			fireball.accelerationY = accelY / d0 * 0.1D;
			fireball.accelerationZ = accelZ / d0 * 0.1D;
			world.spawnEntityInWorld(fireball);
			world.updateEntities();
			return true;
		}
		return false;
	}
	
	/*
	 * Teleportation !!!
	 */
	public static boolean spellTeleport(EntityPlayer caster,
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(pos == null)
		{
			World world = caster.worldObj;
			Vec3d lookingAt = caster.getLookVec();
			double accelX = lookingAt.xCoord;
			double accelY = lookingAt.yCoord;
			double accelZ = lookingAt.zCoord;
			EntitySpellTeleport spellTeleport = new EntitySpellTeleport(world, caster, 
					accelX, accelY, accelZ);
			world.spawnEntityInWorld(spellTeleport);
			world.updateEntities();
			return true;
		}
		return false;
	}
	
	/*
	 * Meteor !!!
	 */
	public static boolean spellSummonMeteor(EntityPlayer caster,
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(pos == null)
		{
			World world = caster.worldObj;
			Vec3d lookingAt = caster.getLookVec();
			double accelX = lookingAt.xCoord;
			double accelY = lookingAt.yCoord;
			double accelZ = lookingAt.zCoord;
			EntitySpellSummonMeteor spellMeteor = new EntitySpellSummonMeteor(world, caster, 
					accelX, accelY, accelZ);
			world.spawnEntityInWorld(spellMeteor);
			world.updateEntities();
			return true;
		}
		return false;
	}
	
	/*
	 * Thunder !!!
	 */
	public static boolean spellSummonLightningBolt(EntityPlayer caster,
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(pos == null)
		{
			World world = caster.worldObj;
			Vec3d lookingAt = caster.getLookVec();
			double accelX = lookingAt.xCoord;
			double accelY = lookingAt.yCoord;
			double accelZ = lookingAt.zCoord;
			EntitySpellSummonLightningBolt spellLightningBolt = new EntitySpellSummonLightningBolt(world,
					caster, accelX, accelY, accelZ);
			world.spawnEntityInWorld(spellLightningBolt);
			world.updateEntities();
			return true;
		}
		return false;
	}
}