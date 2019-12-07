package com.github.sejoslaw.vanillamagic.integration;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

/**
 * Class which holds data for all additional blocks equivalents.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class CustomOre {
	public static final List<Block> REDSTONE = new ArrayList<Block>();
	public static final List<Block> DIAMOND = new ArrayList<Block>();
	public static final List<Block> OBSIDIAN = new ArrayList<Block>();

	private CustomOre() {
	}

	static {
		REDSTONE.add(Blocks.REDSTONE_ORE);
		REDSTONE.add(Blocks.LIT_REDSTONE_ORE); // OreDictionary don't add this as oreRedstone

		DIAMOND.add(Blocks.DIAMOND_ORE);

		OBSIDIAN.add(Blocks.OBSIDIAN);
	}
}