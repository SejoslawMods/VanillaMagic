package seia.vanillamagic.quest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.utils.ItemStackHelper;
import seia.vanillamagic.utils.spell.EnumSpell;
import seia.vanillamagic.utils.spell.EnumWand;

public class QuestList 
{
	public static List<Quest> QUESTS = new ArrayList<Quest>();
	
	public static final QuestCraft QUEST_CRAFT_STICK = new QuestCraft(null, 
			0, 
			0, 
			Items.STICK, 
			"Craft Stick", 
			"craftStick");
	public static final QuestCastSpell QUEST_CAST_SPELL_LIGHTER = new QuestCastSpell(QUEST_CRAFT_STICK.getAchievement(), 
			QUEST_CRAFT_STICK.getPosX(), 
			QUEST_CRAFT_STICK.getPosY() - 2, 
			EnumSpell.LIGHTER.spellName, 
			EnumSpell.LIGHTER.spellUniqueName, 
			EnumSpell.LIGHTER);
	
	public static final QuestCraft QUEST_CRAFT_CAULDRON = new QuestCraft(QUEST_CRAFT_STICK.getAchievement(), 
			QUEST_CRAFT_STICK.getPosX(), 
			QUEST_CRAFT_STICK.getPosY() + 2, 
			Items.CAULDRON, 
			"Craft Cauldron", 
			"craftCauldron");
	public static final QuestMineBlock QUEST_MINE_REDSTONE = new QuestMineBlock(QUEST_CRAFT_CAULDRON.getAchievement(), 
			QUEST_CRAFT_CAULDRON.getPosX(), 
			QUEST_CRAFT_CAULDRON.getPosY() + 2,  
			"Mine Redstone", 
			"mineRedstone", 
			new Block[]{Blocks.REDSTONE_ORE, Blocks.LIT_REDSTONE_ORE});

	public static final QuestBuildAltair QUEST_BUILD_ALTAIR_TIER_1 = new QuestBuildAltair(QUEST_MINE_REDSTONE.getAchievement(), 
			QUEST_MINE_REDSTONE.getPosX(), 
			QUEST_MINE_REDSTONE.getPosY() + 2, 
			Items.CAULDRON, 
			"Build Altair Tier 1", 
			"buildAltairTier1", 
			1);
	public static final QuestCraftOnAltair QUEST_CRAFT_GUNPOWDER = new QuestCraftOnAltair(QUEST_BUILD_ALTAIR_TIER_1.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_1.getPosX() - 2, 
			QUEST_BUILD_ALTAIR_TIER_1.getPosY() + 1,  
			"Craft Gunpowder", 
			"craftGunpowder", 
			new ItemStack[]{new ItemStack(Items.SUGAR), new ItemStack(Items.COAL)}, 
			new ItemStack(Items.GUNPOWDER), 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltair QUEST_CRAFT_SUGAR = new QuestCraftOnAltair(QUEST_CRAFT_GUNPOWDER.getAchievement(), 
			QUEST_CRAFT_GUNPOWDER.getPosX() - 2, 
			QUEST_CRAFT_GUNPOWDER.getPosY(),  
			"Craft Sugar", 
			"craftSugar", 
			new ItemStack[]{ItemStackHelper.getSugarCane()}, 
			new ItemStack(Items.SUGAR), 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltair QUEST_CRAFT_DIRT = new QuestCraftOnAltair(QUEST_CRAFT_SUGAR.getAchievement(), 
			QUEST_CRAFT_SUGAR.getPosX() - 2, 
			QUEST_CRAFT_SUGAR.getPosY(),  
			"Craft Dirt", 
			"craftDirt", 
			new ItemStack[]{new ItemStack(Blocks.SAND)}, 
			new ItemStack(Blocks.DIRT), 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltair QUEST_CRAFT_SAND = new QuestCraftOnAltair(QUEST_CRAFT_DIRT.getAchievement(), 
			QUEST_CRAFT_DIRT.getPosX(), 
			QUEST_CRAFT_DIRT.getPosY() - 2,  
			"Craft Sand", 
			"craftSand", 
			new ItemStack[]{new ItemStack(Blocks.COBBLESTONE)}, 
			new ItemStack(Blocks.SAND), 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltair QUEST_CRAFT_COBBLESTONE = new QuestCraftOnAltair(QUEST_CRAFT_DIRT.getAchievement(), 
			QUEST_CRAFT_DIRT.getPosX(), 
			QUEST_CRAFT_DIRT.getPosY() + 2,  
			"Craft Cobblestone", 
			"craftCobblestone", 
			new ItemStack[]{new ItemStack(Blocks.DIRT)}, 
			new ItemStack(Blocks.COBBLESTONE), 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltair QUEST_CRAFT_LEATHER = new QuestCraftOnAltair(QUEST_BUILD_ALTAIR_TIER_1.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_1.getPosX() + 2, 
			QUEST_BUILD_ALTAIR_TIER_1.getPosY() + 1,  
			"Craft Leather", 
			"craftLeather", 
			new ItemStack[]{new ItemStack(Items.ROTTEN_FLESH)}, 
			new ItemStack(Items.LEATHER), 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltair QUEST_CRAFT_GLASS = new QuestCraftOnAltair(QUEST_CRAFT_SAND.getAchievement(), 
			QUEST_CRAFT_SAND.getPosX() - 2, 
			QUEST_CRAFT_SAND.getPosY(),  
			"Craft Glass", 
			"craftGlass",
			new ItemStack[]{new ItemStack(Blocks.SAND), new ItemStack(Items.COAL)},
			new ItemStack(Blocks.GLASS),
			1,
			EnumWand.STICK);
	
	public static final QuestBuildAltair QUEST_BUILD_ALTAIR_TIER_2 = new QuestBuildAltair(QUEST_BUILD_ALTAIR_TIER_1.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_1.getPosX(), 
			QUEST_BUILD_ALTAIR_TIER_1.getPosY() + 2, 
			Items.CAULDRON, 
			"Build Altair Tier 2", 
			"buildAltairTier2", 
			2);
	public static final QuestCraftOnAltair QUEST_CRAFT_IRON_ORE = new QuestCraftOnAltair(QUEST_BUILD_ALTAIR_TIER_2.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_2.getPosX() + 2, 
			QUEST_BUILD_ALTAIR_TIER_2.getPosY() + 1,  
			"Craft Iron Ore", 
			"craftIronOre", 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 10), new ItemStack(Blocks.COBBLESTONE, 10)}, 
			new ItemStack(Blocks.IRON_ORE), 
			2,
			EnumWand.STICK);
	public static final QuestCraftOnAltair QUEST_CRAFT_CACTUS = new QuestCraftOnAltair(QUEST_BUILD_ALTAIR_TIER_2.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_2.getPosX() - 2, 
			QUEST_BUILD_ALTAIR_TIER_2.getPosY() + 1,  
			"Craft Cactus", 
			"craftCactus", 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 4)}, 
			new ItemStack(Blocks.CACTUS), 
			2,
			EnumWand.STICK);
	
	public static final QuestBuildAltair QUEST_BUILD_ALTAIR_TIER_3 = new QuestBuildAltair(QUEST_BUILD_ALTAIR_TIER_2.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_2.getPosX(), 
			QUEST_BUILD_ALTAIR_TIER_2.getPosY() + 2,
			Items.CAULDRON, 
			"Build Altair Tier 3", 
			"buildAltairTier3", 
			3);
	public static final QuestCraftOnAltair QUEST_CRAFT_GOLD_INGOT = new QuestCraftOnAltair(QUEST_BUILD_ALTAIR_TIER_3.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_3.getPosX() + 2, 
			QUEST_BUILD_ALTAIR_TIER_3.getPosY() + 1, 
			Items.GOLD_INGOT, 
			"Craft Gold Ingot", 
			"craftGoldIngot", 
			new ItemStack[]{new ItemStack(Items.IRON_INGOT, 8)}, 
			new ItemStack(Items.GOLD_INGOT), 
			3,
			EnumWand.STICK);
	
	public static final QuestBuildAltair QUEST_BUILD_ALTAIR_TIER_4 = new QuestBuildAltair(QUEST_BUILD_ALTAIR_TIER_3.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_3.getPosX(), 
			QUEST_BUILD_ALTAIR_TIER_3.getPosY() + 2,
			Items.CAULDRON, 
			"Build Altair Tier 4", 
			"buildAltairTier4", 
			4);
	public static final QuestCraftOnAltair QUEST_CRAFT_ROTTEN_FLESH = new QuestCraftOnAltair(QUEST_BUILD_ALTAIR_TIER_4.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_4.getPosX() + 2, 
			QUEST_BUILD_ALTAIR_TIER_4.getPosY() + 1, 
			Items.ROTTEN_FLESH, 
			"Craft Rotten Flesh", 
			"craftRottenFlesh", 
			new ItemStack[]{new ItemStack(Items.LEATHER)}, 
			new ItemStack(Items.ROTTEN_FLESH), 
			4,
			EnumWand.STICK);
	public static final QuestCraftOnAltair QUEST_CRAFT_STRING = new QuestCraftOnAltair(QUEST_CRAFT_ROTTEN_FLESH.getAchievement(), 
			QUEST_CRAFT_ROTTEN_FLESH.getPosX() + 2, 
			QUEST_CRAFT_ROTTEN_FLESH.getPosY(),  
			"Craft String", 
			"craftString", 
			new ItemStack[]{new ItemStack(Items.ROTTEN_FLESH, 2)}, 
			new ItemStack(Items.STRING), 
			4,
			EnumWand.STICK);
	public static final QuestCraftOnAltair QUEST_CRAFT_SPIDER_EYE = new QuestCraftOnAltair(QUEST_CRAFT_STRING.getAchievement(), 
			QUEST_CRAFT_STRING.getPosX() + 2, 
			QUEST_CRAFT_STRING.getPosY(),  
			"Craft Spider Eye", 
			"craftSpiderEye", 
			new ItemStack[]{new ItemStack(Items.STRING, 2)}, 
			new ItemStack(Items.SPIDER_EYE), 
			4,
			EnumWand.STICK);
	public static final QuestCraftOnAltair QUEST_CRAFT_BONE = new QuestCraftOnAltair(QUEST_CRAFT_SPIDER_EYE.getAchievement(), 
			QUEST_CRAFT_SPIDER_EYE.getPosX() + 2, 
			QUEST_CRAFT_SPIDER_EYE.getPosY(),  
			"Craft Bone", 
			"craftBone", 
			new ItemStack[]{new ItemStack(Items.SPIDER_EYE, 2)}, 
			new ItemStack(Items.BONE), 
			4,
			EnumWand.STICK);
	public static final QuestCraftOnAltair QUEST_CRAFT_ARROW = new QuestCraftOnAltair(QUEST_CRAFT_BONE.getAchievement(), 
			QUEST_CRAFT_BONE.getPosX() + 2, 
			QUEST_CRAFT_BONE.getPosY(), 
			"Craft Arrow", 
			"craftArrow", 
			new ItemStack[]{new ItemStack(Items.BONE, 2)}, 
			new ItemStack(Items.ARROW), 
			4,
			EnumWand.STICK);
	public static final QuestCraftOnAltair QUEST_CRAFT_ENDER_PEARL = new QuestCraftOnAltair(QUEST_CRAFT_ARROW.getAchievement(), 
			QUEST_CRAFT_ARROW.getPosX() + 2, 
			QUEST_CRAFT_ARROW.getPosY(),  
			"Craft Ender Pearl", 
			"craftEnderPearl", 
			new ItemStack[]{new ItemStack(Items.ARROW, 2)}, 
			new ItemStack(Items.ENDER_PEARL), 
			4,
			EnumWand.STICK);
	
	public static final QuestBuildAltair QUEST_BUILD_ALTAIR_TIER_5 = new QuestBuildAltair(QUEST_BUILD_ALTAIR_TIER_4.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_4.getPosX(), 
			QUEST_BUILD_ALTAIR_TIER_4.getPosY() + 2,
			Items.CAULDRON, 
			"Build Altair Tier 5", 
			"buildAltairTier5", 
			5);
	public static final QuestCraftOnAltair QUEST_CRAFT_GLOWSTONE_DUST = new QuestCraftOnAltair(QUEST_BUILD_ALTAIR_TIER_5.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_5.getPosX() - 2, 
			QUEST_BUILD_ALTAIR_TIER_5.getPosY() + 1,  
			"Craft Glowstone Dust", 
			"craftGlowstoneDust", 
			new ItemStack[]{new ItemStack(Items.REDSTONE, 2), new ItemStack(Items.COAL, 2), new ItemStack(Blocks.LAPIS_BLOCK)}, 
			new ItemStack(Items.GLOWSTONE_DUST), 
			5,
			EnumWand.STICK);
	
	public static final QuestPick QUEST_PICK_BLAZE_ROD = new QuestPick(QUEST_BUILD_ALTAIR_TIER_5.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_5.getPosX(), 
			QUEST_BUILD_ALTAIR_TIER_5.getPosY() + 2,  
			"Pick Blaze Rod", 
			"pickBlazeRod", 
			new ItemStack(Items.BLAZE_ROD));
	public static final QuestBuildAltair QUEST_BUILD_ALTAIR_TIER_6 = new QuestBuildAltair(QUEST_PICK_BLAZE_ROD.getAchievement(), 
			QUEST_PICK_BLAZE_ROD.getPosX(), 
			QUEST_PICK_BLAZE_ROD.getPosY() + 2,
			Items.CAULDRON, 
			"Build Altair Tier 6", 
			"buildAltairTier6", 
			6);
	public static final QuestCraftOnAltair QUEST_CRAFT_DIAMOND = new QuestCraftOnAltair(QUEST_BUILD_ALTAIR_TIER_6.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_6.getPosX() + 2, 
			QUEST_BUILD_ALTAIR_TIER_6.getPosY() + 1,  
			"Craft Diamond", 
			"craftDiamond", 
			new ItemStack[]{new ItemStack(Items.GOLD_INGOT, 8)}, 
			new ItemStack(Items.DIAMOND), 
			6,
			EnumWand.BLAZE_ROD);
	
	public static final QuestBuildAltair QUEST_BUILD_ALTAIR_TIER_7 = new QuestBuildAltair(QUEST_BUILD_ALTAIR_TIER_6.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_6.getPosX(), 
			QUEST_BUILD_ALTAIR_TIER_6.getPosY() + 2,
			Items.CAULDRON, 
			"Build Altair Tier 7", 
			"buildAltairTier7", 
			7);
	public static final QuestCraftOnAltair QUEST_CRAFT_EMERALD = new QuestCraftOnAltair(QUEST_BUILD_ALTAIR_TIER_7.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_7.getPosX() + 2, 
			QUEST_BUILD_ALTAIR_TIER_7.getPosY() + 1,  
			"Craft Emerald", 
			"craftEmerald", 
			new ItemStack[]{new ItemStack(Items.DIAMOND, 8)}, 
			new ItemStack(Items.EMERALD), 
			7,
			EnumWand.BLAZE_ROD);
	public static final QuestCraftOnAltair QUEST_CRAFT_NETHER_STAR = new QuestCraftOnAltair(QUEST_CRAFT_EMERALD.getAchievement(), 
			QUEST_CRAFT_EMERALD.getPosX() + 2, 
			QUEST_CRAFT_EMERALD.getPosY(),  
			"Craft Nether Star", 
			"craftNetherStar", 
			new ItemStack[]{new ItemStack(Blocks.EMERALD_BLOCK, 4)},
			new ItemStack(Items.NETHER_STAR), 
			7,
			EnumWand.BLAZE_ROD);
	public static final QuestCraftOnAltair QUEST_CRAFT_DRAGON_EGG = new QuestCraftOnAltair(QUEST_CRAFT_NETHER_STAR.getAchievement(), 
			QUEST_CRAFT_NETHER_STAR.getPosX() + 2, 
			QUEST_CRAFT_NETHER_STAR.getPosY(),  
			"Craft Dragon Egg", 
			"craftDragonEgg", 
			new ItemStack[]{new ItemStack(Items.NETHER_STAR, 4)}, 
			new ItemStack(Blocks.DRAGON_EGG), 
			7,
			EnumWand.BLAZE_ROD);
	
	//===================================================================================================
	
	public static final QuestCraft QUEST_CRAFT_BOOK = new QuestCraft(QUEST_CRAFT_STICK.getAchievement(), 
			QUEST_CRAFT_STICK.getPosX() + 2, 
			QUEST_CRAFT_STICK.getPosY(), 
			Items.BOOK, 
			"Craft Book", 
			"craftBook");
	public static final QuestCraft QUEST_CRAFT_BOOKSHELF = new QuestCraft(QUEST_CRAFT_BOOK.getAchievement(), 
			QUEST_CRAFT_BOOK.getPosX() + 2, 
			QUEST_CRAFT_BOOK.getPosY(), 
			Item.getItemFromBlock(Blocks.BOOKSHELF), 
			"Craft Bookshelf", 
			"craftBookshelf");
	public static final QuestMineBlock QUEST_MINE_DIAMOND = new QuestMineBlock(QUEST_CRAFT_BOOKSHELF.getAchievement(), 
			QUEST_CRAFT_BOOKSHELF.getPosX() + 2, 
			QUEST_CRAFT_BOOKSHELF.getPosY(),  
			"Mine Diamond", 
			"mineDiamond", 
			new Block[]{Blocks.DIAMOND_ORE});
	public static final QuestMineBlock QUEST_MINE_OBSIDIAN = new QuestMineBlock(QUEST_MINE_DIAMOND.getAchievement(), 
			QUEST_MINE_DIAMOND.getPosX() + 2, 
			QUEST_MINE_DIAMOND.getPosY(),  
			"Mine Obsidian", 
			"mineObsidian", 
			new Block[]{Blocks.OBSIDIAN});
	public static final QuestCraft QUEST_CRAFT_ENCHANTING_TABLE = new QuestCraft(QUEST_MINE_OBSIDIAN.getAchievement(), 
			QUEST_MINE_OBSIDIAN.getPosX() + 2, 
			QUEST_MINE_OBSIDIAN.getPosY(), 
			Item.getItemFromBlock(Blocks.ENCHANTING_TABLE), 
			"Craft Enchanting Table", 
			"craftEnchantingTable");
	//TODO: add quest for enchanting sanctuary
	
	//===================================================================================================
	
	static
	{
		// TODO: for each crafting create an QuestCraftOnAltair
		//List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		
		//TODO: for each smelting recipe create an QuestCraftOnAltair which is an input slot item + 1 Coal = output smelting
	}
}