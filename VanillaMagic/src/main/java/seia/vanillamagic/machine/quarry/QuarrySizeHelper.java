package seia.vanillamagic.machine.quarry;

public class QuarrySizeHelper 
{
	/**
	 * It's (size)x(size) but (size-2)x(size-2) is for digging <br>
	 * ChunkNumber * BlocksInChunk
	 */
	@Deprecated
	public static final int BASIC_QUARRY_SIZE = 8 * 16;
	
	/**
	 * Chunk size in blocks.
	 */
	public static final int CHUNK_SIZE = 16;
	
	/**
	 * Basic Quarry size is 1 chunk. <br>
	 * Quarry itself needs 1 diamond block to be created. <br>
	 * 16 * 1 = 16
	 */
	public static final int BASIC_SIZE = 16;
	
	private QuarrySizeHelper()
	{
	}
	
	public static int getSize(int diamondBlocks)
	{
		return BASIC_SIZE * diamondBlocks;
	}
}