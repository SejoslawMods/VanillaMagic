package seia.vanillamagic.magic.spell.spells;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import seia.vanillamagic.magic.spell.Spell;
import seia.vanillamagic.magic.wand.IWand;
import seia.vanillamagic.util.EntityHelper;

public class SpellFusRoDah extends Spell 
{
	public SpellFusRoDah(int spellID, String spellName, String spellUniqueName, IWand wand, 
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(EntityPlayer caster, BlockPos pos, EnumFacing face, Vec3d hitVec) 
	{
		if(pos == null)
		{
			// it will be (2 *SIZE)x(2 *SIZE)x(2 * SIZE) area of effect
			int SIZE = 8;
			float strenght = 2.0f;
			double casterX = caster.posX;
			double casterY = caster.posY;
			double casterZ = caster.posZ;
			BlockPos casterPos = new BlockPos(casterX, casterY, casterZ);
			AxisAlignedBB aabb = new AxisAlignedBB(casterPos);
			aabb = aabb.expand(SIZE, SIZE, SIZE);
			World world = caster.world;
			List<Entity> entitiesInAABB = world.getEntitiesWithinAABBExcludingEntity(caster, aabb);
			for(Entity entity : entitiesInAABB)
			{
				if(entity instanceof EntityLivingBase)
				{
					EntityHelper.knockBack(caster, entity, strenght);
				}
			}
			return true;
		}
		return false;
	}
}