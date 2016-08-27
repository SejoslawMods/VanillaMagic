package seia.vanillamagic.integration;

/**
 * Exceptions are only thrown if the mod can't be load in selected phase.
 */
public interface IIntegration
{
	String getModName();
	
	default void preInit() throws Exception
	{
	}
	
	default void init() throws Exception
	{
	}
	
	default void postInit() throws Exception
	{
	}
}