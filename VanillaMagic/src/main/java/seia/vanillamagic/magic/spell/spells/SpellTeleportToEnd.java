package seia.vanillamagic.magic.spell.spells;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import seia.vanillamagic.api.event.EventTeleportEntity;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.magic.spell.Spell;
import seia.vanillamagic.magic.spell.spells.teleport.TeleportHelper;
import seia.vanillamagic.util.EntityHelper;

public class SpellTeleportToEnd extends Spell 
{
	public SpellTeleportToEnd(int spellID, String spellName, String spellUniqueName, IWand wand,
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
//				caster.changeDimension(1);
//				return true;
//				if(!MinecraftForge.EVENT_BUS.post(new EventTeleportEntity.ChangeDimension(caster, caster.getPosition(), 1)))
//				{
//					caster.changeDimension(1);
//					return true;
//				}
				if(TeleportHelper.entityChangeDimension(caster, 1) != null)
				{
					return true;
				}
			}
			else if(caster.dimension == 1)
			{
				if(caster.hasAchievement(AchievementList.THE_END2))
				{
					World world = caster.world;
					List<Entity> entities = world.loadedEntityList;
					for(int i = 0; i < entities.size(); ++i)
					{
						if(entities.get(i) instanceof EntityDragon)
						{
							EntityHelper.addChatComponentMessageNoSpam(caster, "You need to kill Dragon !!!");
							return false;
						}
					}
					if(caster instanceof EntityPlayerMP)
					{
						TeleportHelper.changePlayerDimensionWithoutPortal((EntityPlayerMP) caster, 0);
					}
					return true;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
}