package com.github.sejoslaw.vanillamagic.api.event;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.IMachine;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This is the base Event for all Machine-based Events.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventMachine extends EventCustomTileEntity {
	private final IMachine tileMachine;

	public EventMachine(IMachine tileMachine, World world, BlockPos customTilePos) {
		super(tileMachine, world, customTilePos);
		this.tileMachine = tileMachine;
	}

	/**
	 * @return Returns Machine connected with this Event.
	 */
	public IMachine getTileMachine() {
		return tileMachine;
	}

	/**
	 * This event is fired BEFORE the given {@link IMachine} performs it's
	 * work.
	 */
	public static class Work extends EventMachine {
		public Work(IMachine tileMachine, World world, BlockPos customTilePos) {
			super(tileMachine, world, customTilePos);
		}
	}
}