package seia.vanillamagic.magic.spell.spells.summon;

import java.util.Random;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import seia.vanillamagic.config.VMConfig;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.magic.spell.Spell;
import seia.vanillamagic.magic.wand.IWand;
import seia.vanillamagic.util.EntityHelper;

public abstract class SpellSummon extends Spell 
{
	public SpellSummon(int spellID, String spellName, String spellUniqueName, IWand wand, 
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}
	
	public boolean castSpell(EntityPlayer caster, BlockPos pos, EnumFacing face, Vec3d hitVec) 
	{
		if(pos != null)
		{
			World world = caster.world;
			BlockPos spawnPos = pos.offset(face);
			Entity entity = getEntity(world); // entity to spawn after spell being casted
			if(entity != null)
			{
				if(entity instanceof EntityAgeable)
				{
					((EntityAgeable) entity).setGrowingAge(1);
				}
				entity.setLocationAndAngles(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, caster.rotationYaw, 0.0F);
				if(getSpawnWithArmor())
				{
					EntityHelper.addRandomArmorToEntity(entity);
				}
				world.spawnEntity(entity);
				Entity horse = getHorse(world);
				if(horse != null)
				{
					int rand = new Random().nextInt(100);
					if(rand < VMConfig.percentForSpawnOnHorse)
					{
						entity.startRiding(horse);
					}
				}
				world.updateEntities();
				return true;
			}
			else
			{
				VanillaMagic.LOGGER.log(Level.INFO, "Wrong spellID. (spellID = " + getSpellID() + ")");
			}
		}
		return false;
	}
	
	public boolean getSpawnWithArmor()
	{
		return false;
	}
	
	@Nullable
	public Entity getHorse(World world)
	{
		return null;
	}
	
	public abstract Entity getEntity(World world);
}