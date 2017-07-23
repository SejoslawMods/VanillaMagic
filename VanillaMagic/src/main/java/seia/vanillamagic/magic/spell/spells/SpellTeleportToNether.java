package seia.vanillamagic.magic.spell.spells;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.api.util.VectorWrapper.Vector3D;
import seia.vanillamagic.magic.spell.Spell;
import seia.vanillamagic.magic.spell.spells.teleport.TeleportUtil;

public class SpellTeleportToNether extends Spell 
{
	public SpellTeleportToNether(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(EntityPlayer caster, BlockPos pos, EnumFacing face, Vector3D hitVec) 
	{
		try
		{
			if (caster instanceof EntityPlayerMP)
			{
				if (caster.dimension == 0)
				{
					TeleportUtil.changePlayerDimensionWithoutPortal((EntityPlayerMP) caster, -1);
					return true;
				}
				else if (caster.dimension == -1)
				{
					TeleportUtil.changePlayerDimensionWithoutPortal((EntityPlayerMP) caster, 0);
					return true;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
}