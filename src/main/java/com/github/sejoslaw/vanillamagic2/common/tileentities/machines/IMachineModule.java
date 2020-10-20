package com.github.sejoslaw.vanillamagic2.common.tileentities.machines;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IMachineModule {
    /**
     * Called once for all related modules before canExecute. Should be used to preconfigure VM TileMachine.
     */
    default boolean setup(IVMTileMachine machine) {
        return true;
    }

    /**
     * @return True if this module can be executed for the specified VM TileMachine.
     */
    boolean canExecute(IVMTileMachine machine);

    /**
     * Executes module logic for the specified VM TileMachine.
     */
    void execute(IVMTileMachine machine);
}
