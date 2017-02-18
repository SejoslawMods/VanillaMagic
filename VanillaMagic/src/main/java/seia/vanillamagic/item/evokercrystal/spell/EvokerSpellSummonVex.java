package seia.vanillamagic.item.evokercrystal.spell;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import seia.vanillamagic.config.VMConfig;

public class EvokerSpellSummonVex extends EvokerSpell
{
	public EvokerSpellSummonVex()
	{
		super();
	}
	
	public int getSpellID() 
	{
		return 1;
	}
	
	public String getSpellName() 
	{
		return "Summon Vex";
	}
	
	public void castSpell(Entity fakeEvoker, Entity target) 
	{
		if(fakeEvoker == null)
		{
			return;
		}
		Random rand = new Random();
		for(int i = 0; i < VMConfig.vexNumber; ++i)
		{
			BlockPos pos = (new BlockPos(fakeEvoker)).add(-2 + rand.nextInt(5), 1, -2 + rand.nextInt(5));
			EntityVex entityVex = new EntityVex(fakeEvoker.world);
			entityVex.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
			entityVex.onInitialSpawn(fakeEvoker.world.getDifficultyForLocation(pos), (IEntityLivingData) null);
			// entityVex.setOwner(fakeEvoker); // TODO: Set Player as Vex Owner
			entityVex.setBoundOrigin(pos);
			if(VMConfig.vexHasLimitedLife)
			{
				entityVex.setLimitedLife(20 * (30 + rand.nextInt(90)));
			}
			fakeEvoker.world.spawnEntity(entityVex);
		}
		fakeEvoker.playSound(SoundEvents.EVOCATION_ILLAGER_PREPARE_SUMMON, 1.0F, 1.0F);
	}
}