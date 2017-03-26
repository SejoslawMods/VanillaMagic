package seia.vanillamagic.integration;

/**
 * This interface should be implemented by the classes who can be understand as an Vanilla Magic Integration.<br>
 * Exceptions are only thrown if the mod can't be load in selected phase. <br>
 * Always return true at the end of each method.
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