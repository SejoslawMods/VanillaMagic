package seia.vanillamagic.tileentity.machine.quarry;

public class QuarrySizeHelper 
{
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