package seia.vanillamagic.integration;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class CustomOre 
{
	public static final List<Block> REDSTONE = new ArrayList<Block>();
	public static final List<Block> DIAMOND = new ArrayList<Block>();
	public static final List<Block> OBSIDIAN = new ArrayList<Block>();
	
	private CustomOre()
	{
	}
	
	static
	{
		REDSTONE.add(Blocks.REDSTONE_ORE);
		REDSTONE.add(Blocks.LIT_REDSTONE_ORE); // OreDictionary don't add this as oreRedstone
		
		DIAMOND.add(Blocks.DIAMOND_ORE);
		
		OBSIDIAN.add(Blocks.OBSIDIAN);
	}
}