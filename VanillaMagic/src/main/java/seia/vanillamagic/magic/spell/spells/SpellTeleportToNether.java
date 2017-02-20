package seia.vanillamagic.magic.spell.spells;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.magic.spell.Spell;
import seia.vanillamagic.magic.spell.spells.teleport.TeleportHelper;

public class SpellTeleportToNether extends Spell 
{
	public SpellTeleportToNether(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(EntityPlayer caster, BlockPos pos, EnumFacing face, Vec3d hitVec) 
	{
		try
		{
			if(caster.dimension == 0)
			{
				TeleportHelper.changePlayerDimensionWithoutPortal(caster, -1);
				return true;
			}
			else if(caster.dimension == -1)
			{
				TeleportHelper.changePlayerDimensionWithoutPortal(caster, 0);
				return true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
}