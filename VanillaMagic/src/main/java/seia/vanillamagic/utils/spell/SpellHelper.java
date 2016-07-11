package seia.vanillamagic.utils.spell;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
		//else if()
		{
		}
		return false;
	}
	
	/*
	 * Flint and Steal Clone
	 */
	public static boolean spellLighter(EntityPlayer caster, 
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		World world = caster.worldObj;
		if(pos != null) // RightClickBlock
		{
			pos = pos.offset(face);
			if (world.isAirBlock(pos))
	        {
				world.playSound(caster, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, new Random().nextFloat() * 0.4F + 0.8F);
				world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
				return true;
	        }
		}
		else // RightClickItem
		{
		}
		return false;
	}
}