package seia.vanillamagic.integration;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

public class VanillaMagicIntegration 
{
	public static final VanillaMagicIntegration INSTANCE = new VanillaMagicIntegration();
	
	//==================================================================================================
	
	public final NBTTagCompound tagCompound = new NBTTagCompound();
	public final List<IIntegration> integrations = new ArrayList<IIntegration>();
	
	private VanillaMagicIntegration()
	{
		integrations.add(new IntegrationVersionChecker());
		integrations.add(new IntegrationBetterAchievements());
	}
	
	public void preInit()
	{
		for(IIntegration i : integrations)
		{
			i.preInit();
		}
	}
	
	public void init()
	{
		for(IIntegration i : integrations)
		{
			i.init();
		}
	}
	
	public void postInit()
	{
		for(IIntegration i : integrations)
		{
			i.postInit();
		}
	}
}