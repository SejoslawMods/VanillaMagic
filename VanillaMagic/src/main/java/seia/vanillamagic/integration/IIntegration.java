package seia.vanillamagic.integration;

/**
 * This interface should be implemented by the classes who can be understand as an Vanilla Magic Integration.<br>
 * Exceptions are only thrown if the mod can't be load in selected phase. <br>
 * Always return true at the end of each method.
 */
public interface IIntegration
{
	String getModName();
	
	default boolean preInit() throws Exception
	{
		return false;
	}
	
	default boolean init() throws Exception
	{
		return false;
	}
	
	default boolean postInit() throws Exception
	{
		return false;
	}
}