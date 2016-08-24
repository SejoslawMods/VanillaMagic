package seia.vanillamagic.integration;

public interface IIntegration 
{
	default void preInit()
	{
	}
	
	default void init()
	{
	}
	
	default void postInit()
	{
	}
}