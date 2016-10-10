package seia.vanillamagic.integration;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.plugins.vanilla.VanillaPlugin;
import net.minecraftforge.fml.common.Loader;

/**
 * Items crafted vanilla-style are registered by {@link VanillaPlugin}
 */
@JEIPlugin
public class IntegrationJEI extends BlankModPlugin implements IIntegration
{
	public static IJeiHelpers JEI_HELPERS;
	
	public String getModName() 
	{
		return "[JEI] JustEnoughItems";
	}
	
	public boolean postInit() throws Exception
	{
		// Just to know from where this MOD_ID is.
		return Loader.isModLoaded(mezz.jei.config.Constants.MOD_ID);
	}
	
	public void register(IModRegistry registry)
	{
		JEI_HELPERS = registry.getJeiHelpers();
	}
}