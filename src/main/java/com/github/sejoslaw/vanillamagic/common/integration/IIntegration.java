package com.github.sejoslaw.vanillamagic.integration;

/**
 * This interface should be implemented by the classes who can be understand as
 * an Vanilla Magic Integration.<br>
 * Exceptions are only thrown if the mod can't be load in selected phase. <br>
 * Always return true at the end of each method.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IIntegration {
	/**
	 * @return Returns the human readable name of the mod.
	 */
	String getModName();

	/**
	 * Run in PreInitialization stage.
	 */
	default void preInit() throws Exception {
	}

	/**
	 * Run in Initialization stage.
	 */
	default void init() throws Exception {
	}

	/**
	 * Run in PostInitialization stage.
	 */
	default void postInit() throws Exception {
	}
}