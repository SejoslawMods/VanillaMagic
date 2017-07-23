package seia.vanillamagic.magic.spell.spells;

import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.api.util.VectorWrapper;
import seia.vanillamagic.api.util.VectorWrapper.Vector3D;
import seia.vanillamagic.entity.EntitySpellFreezeLiquid;
import seia.vanillamagic.magic.spell.Spell;

public class SpellWaterFreeze extends Spell 
{
	public SpellWaterFreeze(int spellID, String spellName, String spellUniqueName, IWand wand, 
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(EntityPlayer caster, BlockPos pos, EnumFacing face, Vector3D hitVec) 
	{
		if(pos == null)
		{
			World world = caster.world;
			Vector3D lookingAt = VectorWrapper.wrap(caster.getLookVec());
			double accelX = lookingAt.getX();
			double accelY = lookingAt.getY();
			double accelZ = lookingAt.getZ();
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