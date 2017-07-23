package seia.vanillamagic.magic.spell.spells;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.api.util.VectorWrapper.Vector3D;
import seia.vanillamagic.magic.spell.Spell;
import seia.vanillamagic.magic.spell.spells.teleport.TeleportHelper;
import seia.vanillamagic.util.EntityUtil;

public class SpellTeleportToEnd extends Spell 
{
	public SpellTeleportToEnd(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(EntityPlayer caster, BlockPos pos, EnumFacing face, Vector3D hitVec) 
	{
		try
		{
			if (caster.dimension == 0)
			{
				if (TeleportHelper.entityChangeDimension(caster, 1) != null) return true;
			}
			else if (caster.dimension == 1)
			{
				World world = caster.world;
				List<Entity> entities = world.loadedEntityList;
				for (int i = 0; i < entities.size(); ++i)
				{
					if (entities.get(i) instanceof EntityDragon)
					{
						EntityUtil.addChatComponentMessageNoSpam(caster, "You need to kill Dragon !!!");
						return false;
					}
				}
				if (caster instanceof EntityPlayerMP) 
					TeleportHelper.changePlayerDimensionWithoutPortal((EntityPlayerMP) caster, 0);
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
}