package seia.vanillamagic.magic.spell.spells;

import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import seia.vanillamagic.entity.EntitySpellFreezeLiquid;
import seia.vanillamagic.magic.spell.Spell;
import seia.vanillamagic.magic.wand.IWand;

public class SpellWaterFreeze extends Spell 
{
	public SpellWaterFreeze(int spellID, String spellName, String spellUniqueName, IWand wand, 
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
			EntitySpellFreezeLiquid spellLightningBolt = new EntitySpellFreezeLiquid(world,
					caster, accelX, accelY, accelZ,
					Blocks.ICE, new BlockLiquid[]{Blocks.WATER, Blocks.FLOWING_WATER}, 5);
			world.spawnEntity(spellLightningBolt);
			world.updateEntities();
			return true;
		}
		return false;
	}
}