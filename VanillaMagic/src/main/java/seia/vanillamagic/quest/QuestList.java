package seia.vanillamagic.quest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import seia.vanillamagic.quest.build.QuestBuildAltair;
import seia.vanillamagic.quest.craft.QuestCraft;
import seia.vanillamagic.quest.craft.QuestCraftOnAltair;
import seia.vanillamagic.quest.mine.QuestMineBlock;
import seia.vanillamagic.utils.ItemStackIngredientsHelper;

public class QuestList 
{
	public static List<Quest> QUESTS = new ArrayList<Quest>();
	
	public static final QuestCraft QUEST_CRAFT_STICK = new QuestCraft(null, 
			0, 
			0, 
			Items.STICK, 
			"Craft Stick", 
			"craftStick");
	public static final QuestCraft QUEST_CRAFT_CAULDRON = new QuestCraft(QUEST_CRAFT_STICK.getAchievement(), 
			QUEST_CRAFT_STICK.getPosX(), 
			QUEST_CRAFT_STICK.getPosY() + 2, 
			Items.CAULDRON, 
			"Craft Cauldron", 
			"craftCauldron");
	public static final QuestMineBlock QUEST_MINE_REDSTONE = new QuestMineBlock(QUEST_CRAFT_CAULDRON.getAchievement(), 
			QUEST_CRAFT_CAULDRON.getPosX(), 
			QUEST_CRAFT_CAULDRON.getPosY() + 2, 
			Item.getItemFromBlock(Blocks.REDSTONE_ORE), 
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
			Items.GUNPOWDER, 
			"Craft Gunpowder", 
			"craftGunpowder", 
			new ItemStack[]{new ItemStack(Items.SUGAR), new ItemStack(Items.COAL)}, 
			new ItemStack(Items.GUNPOWDER), 
			1);
	public static final QuestCraftOnAltair QUEST_CRAFT_SUGAR = new QuestCraftOnAltair(QUEST_CRAFT_GUNPOWDER.getAchievement(), 
			QUEST_CRAFT_GUNPOWDER.getPosX() - 2, 
			QUEST_CRAFT_GUNPOWDER.getPosY(), 
			Items.SUGAR, 
			"Craft Sugar", 
			"craftSugar", 
			new ItemStack[]{ItemStackIngredientsHelper.getSugarCane()}, 
			new ItemStack(Items.SUGAR), 
			1);
	public static final QuestCraftOnAltair QUEST_CRAFT_DIRT = new QuestCraftOnAltair(QUEST_CRAFT_SUGAR.getAchievement(), 
			QUEST_CRAFT_SUGAR.getPosX() - 2, 
			QUEST_CRAFT_SUGAR.getPosY(), 
			Item.getItemFromBlock(Blocks.DIRT), 
			"Craft Dirt", 
			"craftDirt", 
			new ItemStack[]{new ItemStack(Blocks.SAND)}, 
			new ItemStack(Blocks.DIRT), 
			1);
	public static final QuestCraftOnAltair QUEST_CRAFT_SAND = new QuestCraftOnAltair(QUEST_CRAFT_DIRT.getAchievement(), 
			QUEST_CRAFT_DIRT.getPosX(), 
			QUEST_CRAFT_DIRT.getPosY() - 2, 
			Item.getItemFromBlock(Blocks.SAND), 
			"Craft Sand", 
			"craftSand", 
			new ItemStack[]{new ItemStack(Blocks.COBBLESTONE)}, 
			new ItemStack(Blocks.SAND), 
			1);
	public static final QuestCraftOnAltair QUEST_CRAFT_COBBLESTONE = new QuestCraftOnAltair(QUEST_CRAFT_DIRT.getAchievement(), 
			QUEST_CRAFT_DIRT.getPosX(), 
			QUEST_CRAFT_DIRT.getPosY() + 2, 
			Item.getItemFromBlock(Blocks.COBBLESTONE), 
			"Craft Cobblestone", 
			"craftCobblestone", 
			new ItemStack[]{new ItemStack(Blocks.DIRT)}, 
			new ItemStack(Blocks.COBBLESTONE), 
			1);
	public static final QuestCraftOnAltair QUEST_CRAFT_LEATHER = new QuestCraftOnAltair(QUEST_BUILD_ALTAIR_TIER_1.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_1.getPosX() + 2, 
			QUEST_BUILD_ALTAIR_TIER_1.getPosY() + 1, 
			Items.LEATHER, 
			"Craft Leather", 
			"craftLeather", 
			new ItemStack[]{new ItemStack(Items.ROTTEN_FLESH)}, 
			new ItemStack(Items.LEATHER), 
			1);
	public static final QuestCraftOnAltair QUEST_CRAFT_GLASS = new QuestCraftOnAltair(QUEST_CRAFT_SAND.getAchievement(), 
			QUEST_CRAFT_SAND.getPosX() - 2, 
			QUEST_CRAFT_SAND.getPosY(), 
			Item.getItemFromBlock(Blocks.GLASS), 
			"Craft Glass", 
			"craftGlass",
			new ItemStack[]{new ItemStack(Blocks.SAND), new ItemStack(Items.COAL)},
			new ItemStack(Blocks.GLASS),
			1);
	
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
			Item.getItemFromBlock(Blocks.IRON_ORE), 
			"Craft Iron Ore", 
			"craftIronOre", 
			ItemStackIngredientsHelper.getCraftIronOreIngredients(), 
			new ItemStack(Blocks.IRON_ORE), 
			2);
	public static final QuestCraftOnAltair QUEST_CRAFT_CACTUS = new QuestCraftOnAltair(QUEST_BUILD_ALTAIR_TIER_2.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_2.getPosX() - 2, 
			QUEST_BUILD_ALTAIR_TIER_2.getPosY() + 1, 
			Item.getItemFromBlock(Blocks.CACTUS), 
			"Craft Cactus", 
			"craftCactus", 
			new ItemStack[]{new ItemStack(Blocks.SAPLING), new ItemStack(Blocks.SAPLING), new ItemStack(Blocks.SAPLING), new ItemStack(Blocks.SAPLING)}, 
			new ItemStack(Blocks.CACTUS), 
			2);
	
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
			ItemStackIngredientsHelper.getCraftGoldIngotIngredients(), 
			new ItemStack(Items.GOLD_INGOT), 
			3);
	
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
			4);
	public static final QuestCraftOnAltair QUEST_CRAFT_STRING = new QuestCraftOnAltair(QUEST_CRAFT_ROTTEN_FLESH.getAchievement(), 
			QUEST_CRAFT_ROTTEN_FLESH.getPosX() + 2, 
			QUEST_CRAFT_ROTTEN_FLESH.getPosY(),  
			"Craft String", 
			"craftString", 
			new ItemStack[]{new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.ROTTEN_FLESH)}, 
			new ItemStack(Items.STRING), 
			4);
	public static final QuestCraftOnAltair QUEST_CRAFT_SPIDER_EYE = new QuestCraftOnAltair(QUEST_CRAFT_STRING.getAchievement(), 
			QUEST_CRAFT_STRING.getPosX() + 2, 
			QUEST_CRAFT_STRING.getPosY(),  
			"Craft Spider Eye", 
			"craftSpiderEye", 
			new ItemStack[]{new ItemStack(Items.STRING), new ItemStack(Items.STRING)}, 
			new ItemStack(Items.SPIDER_EYE), 
			4);
	public static final QuestCraftOnAltair QUEST_CRAFT_BONE = new QuestCraftOnAltair(QUEST_CRAFT_SPIDER_EYE.getAchievement(), 
			QUEST_CRAFT_SPIDER_EYE.getPosX() + 2, 
			QUEST_CRAFT_SPIDER_EYE.getPosY(),  
			"Craft Bone", 
			"craftBone", 
			new ItemStack[]{new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.SPIDER_EYE)}, 
			new ItemStack(Items.BONE), 
			4);
	public static final QuestCraftOnAltair QUEST_CRAFT_ARROW = new QuestCraftOnAltair(QUEST_CRAFT_BONE.getAchievement(), 
			QUEST_CRAFT_BONE.getPosX() + 2, 
			QUEST_CRAFT_BONE.getPosY(), 
			"Craft Arrow", 
			"craftArrow", 
			new ItemStack[]{new ItemStack(Items.BONE), new ItemStack(Items.BONE)}, 
			new ItemStack(Items.ARROW), 
			4);
	public static final QuestCraftOnAltair QUEST_CRAFT_ENDER_PEARL = new QuestCraftOnAltair(QUEST_CRAFT_ARROW.getAchievement(), 
			QUEST_CRAFT_ARROW.getPosX() + 2, 
			QUEST_CRAFT_ARROW.getPosY(),  
			"Craft Ender Pearl", 
			"craftEnderPearl", 
			new ItemStack[]{new ItemStack(Items.ARROW), new ItemStack(Items.ARROW)}, 
			new ItemStack(Items.ENDER_PEARL), 
			4);
	
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
			Items.GLOWSTONE_DUST, 
			"Craft Glowstone Dust", 
			"craftGlowstoneDust", 
			new ItemStack[]{new ItemStack(Items.REDSTONE), new ItemStack(Items.REDSTONE), new ItemStack(Items.COAL), new ItemStack(Items.COAL), new ItemStack(Blocks.LAPIS_BLOCK)}, 
			new ItemStack(Items.GLOWSTONE_DUST), 
			5);
	
	public static final QuestBuildAltair QUEST_BUILD_ALTAIR_TIER_6 = new QuestBuildAltair(QUEST_BUILD_ALTAIR_TIER_5.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_5.getPosX(), 
			QUEST_BUILD_ALTAIR_TIER_5.getPosY() + 2,
			Items.CAULDRON, 
			"Build Altair Tier 6", 
			"buildAltairTier6", 
			6);
	public static final QuestCraftOnAltair QUEST_CRAFT_DIAMOND = new QuestCraftOnAltair(QUEST_BUILD_ALTAIR_TIER_6.getAchievement(), 
			QUEST_BUILD_ALTAIR_TIER_6.getPosX() + 2, 
			QUEST_BUILD_ALTAIR_TIER_6.getPosY() + 1, 
			Items.DIAMOND, 
			"Craft Diamond", 
			"craftDiamond", 
			ItemStackIngredientsHelper.getCraftDiamondIngredients(), 
			new ItemStack(Items.DIAMOND), 
			6);
	
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
			Items.EMERALD, 
			"Craft Emerald", 
			"craftEmerald", 
			ItemStackIngredientsHelper.getCraftEmeraldIngredients(), 
			new ItemStack(Items.EMERALD), 
			7);
	public static final QuestCraftOnAltair QUEST_CRAFT_NETHER_STAR = new QuestCraftOnAltair(QUEST_CRAFT_EMERALD.getAchievement(), 
			QUEST_CRAFT_EMERALD.getPosX() + 2, 
			QUEST_CRAFT_EMERALD.getPosY(),  
			"Craft Nether Star", 
			"craftNetherStar", 
			new ItemStack[]{new ItemStack(Blocks.EMERALD_BLOCK), new ItemStack(Blocks.EMERALD_BLOCK), new ItemStack(Blocks.EMERALD_BLOCK), new ItemStack(Blocks.EMERALD_BLOCK)},
			new ItemStack(Items.NETHER_STAR), 
			7);
	public static final QuestCraftOnAltair QUEST_CRAFT_DRAGON_EGG = new QuestCraftOnAltair(QUEST_CRAFT_NETHER_STAR.getAchievement(), 
			QUEST_CRAFT_NETHER_STAR.getPosX() + 2, 
			QUEST_CRAFT_NETHER_STAR.getPosY(),  
			"Craft Dragon Egg", 
			"craftDragonEgg", 
			new ItemStack[]{new ItemStack(Items.NETHER_STAR), new ItemStack(Items.NETHER_STAR), new ItemStack(Items.NETHER_STAR), new ItemStack(Items.NETHER_STAR)}, 
			new ItemStack(Blocks.DRAGON_EGG), 
			7);
	
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