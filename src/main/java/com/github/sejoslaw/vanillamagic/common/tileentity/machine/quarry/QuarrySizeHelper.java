package com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class QuarrySizeHelper {
	/**
	 * Chunk size in blocks.
	 */
	public static final int CHUNK_SIZE = 16;

	/**
	 * Basic Quarry size is 1 chunk. <br>
	 * Quarry itself needs 1 diamond block to be created. <br>
	 * 16 * 1 = 16
	 */
	public static final int BASIC_SIZE = CHUNK_SIZE;

	private QuarrySizeHelper() {
	}

	public static int calculateSize(int diamondBlocks) {
		return BASIC_SIZE * diamondBlocks;
	}
}