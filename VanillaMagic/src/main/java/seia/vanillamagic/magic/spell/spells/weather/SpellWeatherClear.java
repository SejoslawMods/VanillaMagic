package seia.vanillamagic.magic.spell.spells.weather;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.api.util.VectorWrapper.Vector3D;
import seia.vanillamagic.magic.spell.Spell;

public class SpellWeatherClear extends Spell 
{
	public SpellWeatherClear(int spellID, String spellName, String spellUniqueName, IWand wand, 
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(EntityPlayer caster, BlockPos pos, EnumFacing face, Vector3D hitVec) 
	{
		World world = caster.world;
		WorldInfo worldInfo = world.getWorldInfo();
		worldInfo.setCleanWeatherTime(1000);
		worldInfo.setRainTime(0);
		worldInfo.setThunderTime(0);
		worldInfo.setRaining(false);
		worldInfo.setThundering(false);
		return true;
	}
}