package seia.vanillamagic.magic.spell.spells;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.entity.EntitySpellPull;
import seia.vanillamagic.magic.spell.Spell;

public class SpellPullEntityToPlayer extends Spell 
{
	public SpellPullEntityToPlayer(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(EntityPlayer caster, BlockPos pos, EnumFacing face, Vec3d hitVec) 
	{
		if(pos == null)
		{
			World world = caster.world;
			Vec3d lookingAt = caster.getLookVec();
			double accelX = lookingAt.xCoord;
			double accelY = lookingAt.yCoord;
			double accelZ = lookingAt.zCoord;
			EntitySpellPull spellLightningBolt = new EntitySpellPull(world,
					caster, accelX, accelY, accelZ,
					caster.getPosition());
			world.spawnEntity(spellLightningBolt);
			world.updateEntities();
			return true;
		}
		return false;
	}
}