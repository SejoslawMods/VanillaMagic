package seia.vanillamagic.quest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import seia.vanillamagic.chunkloader.QuestChunkLoader;
import seia.vanillamagic.machine.quarry.QuestQuarry;
import seia.vanillamagic.quest.fulltreecut.QuestFullTreeCut;
import seia.vanillamagic.quest.portablecraftingtable.QuestPortableCraftingTable;
import seia.vanillamagic.quest.spell.QuestCastSpellInAir;
import seia.vanillamagic.quest.spell.QuestCastSpellOnBlock;
import seia.vanillamagic.utils.ItemStackHelper;
import seia.vanillamagic.utils.spell.EnumSpell;
import seia.vanillamagic.utils.spell.EnumWand;

public class QuestList 
{
	public static List<Quest> QUESTS = new ArrayList<Quest>();

	private static Achievement START = AchievementList.OPEN_INVENTORY;
	public static final QuestCraft QUEST_CRAFT_STICK = new QuestCraft(START, 0, 0, new ItemStack(Items.STICK), "Craft Stick", "craftStick");
	
	public static final QuestPortableCraftingTable QUEST_PORTABLE_CRAFTING_TABLE = new QuestPortableCraftingTable(QUEST_CRAFT_STICK, -2, 0, new ItemStack(Blocks.CRAFTING_TABLE), "Portable Crafting Table", "portableCraftingTable");
	
	public static final QuestCastSpellOnBlock QUEST_CAST_SPELL_LIGHTER = new QuestCastSpellOnBlock(QUEST_CRAFT_STICK, 0, -2, EnumSpell.LIGHTER);
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_SMALL_FIREBALL = new QuestCastSpellInAir(QUEST_CAST_SPELL_LIGHTER, 0, -2, EnumSpell.SMALL_FIREBALL);
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_FUS_RO_DAH = new QuestCastSpellInAir(QUEST_CAST_SPELL_SMALL_FIREBALL, 0, -2, EnumSpell.FUS_RO_DAH);
	
	// SUMMONS Passive Entities
	public static final QuestCastSpellOnBlock QUEST_SUMMON_CHICKEN = new QuestCastSpellOnBlock(QUEST_CAST_SPELL_SMALL_FIREBALL, -2, 0, EnumSpell.SUMMON_CHICKEN);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_PIG = new QuestCastSpellOnBlock(QUEST_SUMMON_CHICKEN, -2, 0, EnumSpell.SUMMON_PIG);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_COW = new QuestCastSpellOnBlock(QUEST_SUMMON_PIG, -2, 0, EnumSpell.SUMMON_COW);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_MOOSHROOM = new QuestCastSpellOnBlock(QUEST_SUMMON_COW, 0, +2, EnumSpell.SUMMON_MOOSHROOM);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_SHEEP = new QuestCastSpellOnBlock(QUEST_SUMMON_COW, -2, 0, EnumSpell.SUMMON_SHEEP);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_WOLF = new QuestCastSpellOnBlock(QUEST_SUMMON_SHEEP, 0, +2, EnumSpell.SUMMON_WOLF);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_RABBIT = new QuestCastSpellOnBlock(QUEST_SUMMON_SHEEP, -2, 0, EnumSpell.SUMMON_RABBIT);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_HORSE = new QuestCastSpellOnBlock(QUEST_SUMMON_RABBIT, -2, 0, EnumSpell.SUMMON_HORSE);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_VILLAGER = new QuestCastSpellOnBlock(QUEST_SUMMON_HORSE, -2, 0, EnumSpell.SUMMON_VILLAGER);
	// END SUMMONS Passive Entities
	
	//SUMMONS Monster
	public static final QuestCastSpellOnBlock QUEST_SUMMON_ZOMBIE = new QuestCastSpellOnBlock(QUEST_SUMMON_CHICKEN, 0, -2, EnumSpell.SUMMON_ZOMBIE);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_CREEPER = new QuestCastSpellOnBlock(QUEST_SUMMON_ZOMBIE, -2, 0, EnumSpell.SUMMON_CREEPER);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_SKELETON = new QuestCastSpellOnBlock(QUEST_SUMMON_CREEPER, -2, 0, EnumSpell.SUMMON_SKELETON);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_BLAZE = new QuestCastSpellOnBlock(QUEST_SUMMON_SKELETON, -2, 0, EnumSpell.SUMMON_BLAZE);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_MAGMA_CUBE = new QuestCastSpellOnBlock(QUEST_SUMMON_BLAZE, -2, 0, EnumSpell.SUMMON_MAGMA_CUBE);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_GHAST = new QuestCastSpellOnBlock(QUEST_SUMMON_MAGMA_CUBE, -2, 0, EnumSpell.SUMMON_GHAST);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_ENDERMAN = new QuestCastSpellOnBlock(QUEST_SUMMON_GHAST, -2, 0, EnumSpell.SUMMON_ENDERMAN);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_SPIDER = new QuestCastSpellOnBlock(QUEST_SUMMON_ENDERMAN, -2, 0, EnumSpell.SUMMON_SPIDER);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_SLIME = new QuestCastSpellOnBlock(QUEST_SUMMON_SPIDER, -2, 0, EnumSpell.SUMMON_SLIME);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_WITCH = new QuestCastSpellOnBlock(QUEST_SUMMON_SLIME, -2, 0, EnumSpell.SUMMON_WITCH);
	// END SUMMONS Monster
	
	// SPELLS
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_LARGE_FIREBALL = new QuestCastSpellInAir(QUEST_CAST_SPELL_SMALL_FIREBALL, +2, 0, EnumSpell.LARGE_FIREBALL);
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_LIGHTNING_BOLT = new QuestCastSpellInAir(QUEST_CAST_SPELL_LARGE_FIREBALL, 0, +2, EnumSpell.LIGHTNING_BOLT);
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_PULL_ENTITY_TO_PLAYER = new QuestCastSpellInAir(QUEST_CAST_SPELL_LIGHTNING_BOLT, +2, 0, EnumSpell.PULL_ENTITY_TO_PLAYER);
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_WATER_FREEZE = new QuestCastSpellInAir(QUEST_CAST_SPELL_PULL_ENTITY_TO_PLAYER, +2, 0, EnumSpell.WATER_FREEZE);
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_MOVE_IN_AIR = new QuestCastSpellInAir(QUEST_CAST_SPELL_WATER_FREEZE, +2, 0, EnumSpell.MOVE_IN_AIR);
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_TELEPORT = new QuestCastSpellInAir(QUEST_CAST_SPELL_LARGE_FIREBALL, 0, -2, EnumSpell.TELEPORT);
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_TELEPORT_TO_NETHER = new QuestCastSpellInAir(QUEST_CAST_SPELL_TELEPORT, +2, 0, EnumSpell.TELEPORT_TO_NETHER);
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_TELEPORT_TO_END = new QuestCastSpellInAir(QUEST_CAST_SPELL_TELEPORT_TO_NETHER, +2, 0, ItemStackHelper.getHead(1, 5), EnumSpell.TELEPORT_TO_END);
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_METEOR = new QuestCastSpellInAir(QUEST_CAST_SPELL_LARGE_FIREBALL, +2, 0, EnumSpell.METEOR);
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_WEATHER_RAIN = new QuestCastSpellInAir(QUEST_CAST_SPELL_METEOR, +2, 0, EnumSpell.WEATHER_RAIN);
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_WEATHER_CLEAR = new QuestCastSpellInAir(QUEST_CAST_SPELL_WEATHER_RAIN, +2, 0, EnumSpell.WEATHER_CLEAR);
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_WEATHER_THUNDERSTORM = new QuestCastSpellInAir(QUEST_CAST_SPELL_WEATHER_CLEAR, +2, 0, EnumSpell.WEATHER_THUNDERSTORM);
	// END SPELLS
	
	public static final QuestCraft QUEST_CRAFT_CAULDRON = new QuestCraft(QUEST_CRAFT_STICK, 0, +2, new ItemStack(Items.CAULDRON), "Craft Cauldron", "craftCauldron");
	public static final QuestMineBlock QUEST_MINE_REDSTONE = new QuestMineBlock(QUEST_CRAFT_CAULDRON, 0, +2,new ItemStack(Blocks.REDSTONE_ORE),"Mine Redstone", "mineRedstone", new Block[]{Blocks.REDSTONE_ORE, Blocks.LIT_REDSTONE_ORE});
	public static final QuestBuildAltar QUEST_BUILD_ALTAR_TIER_1 = new QuestBuildAltar(QUEST_MINE_REDSTONE, 0, +2, "Build Altar Tier 1", "buildAltarTier1", 1);
	
	public static final QuestCraftOnAltar QUEST_CRAFT_GUNPOWDER = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_1, -2, +1, "Craft Gunpowder", "craftGunpowder", 
			new ItemStack[]{new ItemStack(Items.SUGAR), new ItemStack(Items.COAL)}, 
			new ItemStack[]{new ItemStack(Items.GUNPOWDER)}, 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_SUGAR = new QuestCraftOnAltar(QUEST_CRAFT_GUNPOWDER, -2, 0, "Craft Sugar", "craftSugar", 
			new ItemStack[]{ItemStackHelper.getSugarCane(1)}, 
			new ItemStack[]{new ItemStack(Items.SUGAR)}, 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_DIRT = new QuestCraftOnAltar(QUEST_CRAFT_SUGAR, -2, 0, "Craft Dirt", "craftDirt", 
			new ItemStack[]{new ItemStack(Blocks.SAND)}, 
			new ItemStack[]{new ItemStack(Blocks.DIRT)}, 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_SAND = new QuestCraftOnAltar(QUEST_CRAFT_DIRT, 0, -2, "Craft Sand", "craftSand", 
			new ItemStack[]{new ItemStack(Blocks.COBBLESTONE)}, 
			new ItemStack[]{new ItemStack(Blocks.SAND)}, 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_COBBLESTONE = new QuestCraftOnAltar(QUEST_CRAFT_DIRT, -2, 0, "Craft Cobblestone", "craftCobblestone", 
			new ItemStack[]{new ItemStack(Blocks.DIRT)}, 
			new ItemStack[]{new ItemStack(Blocks.COBBLESTONE)}, 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_GRAVEL = new QuestCraftOnAltar(QUEST_CRAFT_COBBLESTONE, -2, 0, "Craft Gravel", "craftGravel", 
			new ItemStack[]{new ItemStack(Blocks.COBBLESTONE, 2)}, 
			new ItemStack[]{new ItemStack(Blocks.GRAVEL)}, 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_LEATHER = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_1, +2, +1, "Craft Leather", "craftLeather", 
			new ItemStack[]{new ItemStack(Items.ROTTEN_FLESH)}, 
			new ItemStack[]{new ItemStack(Items.LEATHER)}, 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_CLAY = new QuestCraftOnAltar(QUEST_CRAFT_LEATHER, +2, 0, "Craft Clay", "craftClay", 
			new ItemStack[]{new ItemStack(Items.SUGAR), new ItemStack(Items.GUNPOWDER)}, 
			new ItemStack[]{new ItemStack(Items.CLAY_BALL)}, 
			1, 
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_GLASS = new QuestCraftOnAltar(QUEST_CRAFT_SAND, -2, 0, "Craft Glass", "craftGlass",
			new ItemStack[]{new ItemStack(Blocks.SAND), new ItemStack(Items.COAL)},
			new ItemStack[]{new ItemStack(Blocks.GLASS)},
			1,
			EnumWand.STICK);
	
	public static final QuestBuildAltar QUEST_BUILD_ALTAR_TIER_2 = new QuestBuildAltar(QUEST_BUILD_ALTAR_TIER_1, 0, +2,"Build Altar Tier 2", "buildAltarTier2", 2);
	
	public static final QuestCraftOnAltar QUEST_CRAFT_IRON_ORE = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_2, +2, +1, "Craft Iron Ore", "craftIronOre", 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 10), new ItemStack(Blocks.COBBLESTONE, 10)}, 
			new ItemStack[]{new ItemStack(Blocks.IRON_ORE)}, 
			2,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_CACTUS = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_2, -2, +1, "Craft Cactus", "craftCactus", 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 2, 0)}, 
			new ItemStack[]{new ItemStack(Blocks.CACTUS)}, 
			2,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_SAPLING_0 = new QuestCraftOnAltar(QUEST_CRAFT_CACTUS, -2, 0, "Craft Oak Sapling from Cactus", "craftSapling0", 
			new ItemStack[]{new ItemStack(Blocks.CACTUS, 2)}, 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 1, 0)},
			2, 
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_SAPLING_1 = new QuestCraftOnAltar(QUEST_CRAFT_SAPLING_0, 0, +2, "Craft Spruce Sapling", "craftSapling1", 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 1, 0)}, 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 1, 1)},
			2, 
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_SAPLING_2 = new QuestCraftOnAltar(QUEST_CRAFT_SAPLING_1, -2, 0, "Craft Birch Sapling", "craftSapling2", 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 1, 1)}, 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 1, 2)},
			2, 
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_SAPLING_3 = new QuestCraftOnAltar(QUEST_CRAFT_SAPLING_2, -2, 0, "Craft Jungle Sapling", "craftSapling3", 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 1, 2)}, 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 1, 3)},
			2, 
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_SAPLING_4 = new QuestCraftOnAltar(QUEST_CRAFT_SAPLING_3, -2, 0, "Craft Acacia Sapling", "craftSapling4", 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 1, 3)}, 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 1, 4)},
			2, 
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_SAPLING_5 = new QuestCraftOnAltar(QUEST_CRAFT_SAPLING_4, -2, 0, "Craft Dark Oak Sapling", "craftSapling5", 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 1, 4)}, 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 1, 5)},
			2, 
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_SAPLING_6 = new QuestCraftOnAltar(QUEST_CRAFT_SAPLING_5, -2, 0, "Craft Oak Sapling from Dark Oak", "craftSapling6", 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 1, 5)}, 
			new ItemStack[]{new ItemStack(Blocks.SAPLING)},
			2, 
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_PUMPKIN = new QuestCraftOnAltar(QUEST_CRAFT_SAPLING_0, -2, 0, "Craft Pumpkin", "craftPumpkin", 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 3)}, 
			new ItemStack[]{new ItemStack(Blocks.PUMPKIN)}, 
			2, 
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_MELON = new QuestCraftOnAltar(QUEST_CRAFT_PUMPKIN, -2, 0, "Craft Melon Block", "craftMelonBlock", 
			new ItemStack[]{new ItemStack(Blocks.PUMPKIN, 2)}, 
			new ItemStack[]{new ItemStack(Blocks.MELON_BLOCK)}, 
			2, 
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_VINES = new QuestCraftOnAltar(QUEST_CRAFT_MELON, -2, 0, "Craft Vine", "craftVine", 
			new ItemStack[]{new ItemStack(Blocks.MELON_BLOCK, 2)}, 
			new ItemStack[]{new ItemStack(Blocks.VINE)}, 
			2, 
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_LILY_PAD = new QuestCraftOnAltar(QUEST_CRAFT_VINES, -2, 0, "Craft Lily Pad", "craftLiyPad", 
			new ItemStack[]{new ItemStack(Blocks.VINE, 2)}, 
			new ItemStack[]{new ItemStack(Blocks.WATERLILY)}, 
			2, 
			EnumWand.STICK);
	
	public static final QuestBuildAltar QUEST_BUILD_ALTAR_TIER_3 = new QuestBuildAltar(QUEST_BUILD_ALTAR_TIER_2, 0, +2, "Build Altar Tier 3", "buildAltarTier3", 3);
	public static final QuestSmeltOnAltar QUEST_SMELT_ON_ALTAR = new QuestSmeltOnAltar(QUEST_BUILD_ALTAR_TIER_3, -2, +1, new ItemStack(Blocks.FURNACE), "Smelt in Altar", "smeltInAltar", 1, EnumWand.STICK);
	public static final QuestQuarry QUEST_QUARRY = new QuestQuarry(QUEST_SMELT_ON_ALTAR, 0, +2, "Quarry !!!", "quarry");
	public static final QuestFullTreeCut QUEST_FULL_TREE_CUT = new QuestFullTreeCut(QUEST_QUARRY, -2, 0, new ItemStack(Items.DIAMOND_AXE), "Full Tree Cut", "fullTreeCut");
	//TODO: Fix QuestMineMulti and MineBlockTask
	//public static final QuestMineMulti QUEST_MINE_3X3 = new QuestMineMulti(QUEST_FULL_TREE_CUT, -2, 0, new ItemStack(Items.DIAMOND_PICKAXE), "Mining 3x3", "mining3x3", 1, 1, EnumWand.BLAZE_ROD);
	
	public static final QuestCraftOnAltar QUEST_CRAFT_GOLD_INGOT = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_3, +2, +1, "Craft Gold Ingot", "craftGoldIngot", 
			new ItemStack[]{new ItemStack(Items.IRON_INGOT, 8)}, 
			new ItemStack[]{new ItemStack(Items.GOLD_INGOT)}, 
			3,
			EnumWand.STICK);
	
	public static final QuestBuildAltar QUEST_BUILD_ALTAR_TIER_4 = new QuestBuildAltar(QUEST_BUILD_ALTAR_TIER_3, 0, +2, "Build Altar Tier 4", "buildAltarTier4", 4);
	
	public static final QuestCraftOnAltar QUEST_CRAFT_ROTTEN_FLESH = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_4, +2, +1, "Craft Rotten Flesh", "craftRottenFlesh", 
			new ItemStack[]{new ItemStack(Items.LEATHER)}, 
			new ItemStack[]{new ItemStack(Items.ROTTEN_FLESH)}, 
			4,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_STRING = new QuestCraftOnAltar(QUEST_CRAFT_ROTTEN_FLESH, +2, 0, "Craft String", "craftString", 
			new ItemStack[]{new ItemStack(Items.ROTTEN_FLESH, 2)}, 
			new ItemStack[]{new ItemStack(Items.STRING)}, 
			4,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_SPIDER_EYE = new QuestCraftOnAltar(QUEST_CRAFT_STRING, +2, 0, "Craft Spider Eye", "craftSpiderEye", 
			new ItemStack[]{new ItemStack(Items.STRING, 2)}, 
			new ItemStack[]{new ItemStack(Items.SPIDER_EYE)}, 
			4,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_BONE = new QuestCraftOnAltar(QUEST_CRAFT_SPIDER_EYE, +2, 0, "Craft Bone", "craftBone", 
			new ItemStack[]{new ItemStack(Items.SPIDER_EYE, 2)}, 
			new ItemStack[]{new ItemStack(Items.BONE)}, 
			4,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_ARROW = new QuestCraftOnAltar(QUEST_CRAFT_BONE, +2, 0, "Craft Arrow", "craftArrow", 
			new ItemStack[]{new ItemStack(Items.BONE, 2)}, 
			new ItemStack[]{new ItemStack(Items.ARROW)}, 
			4,
			EnumWand.STICK);
	
	public static final QuestArrowMachineGun QUEST_ARROW_MACHINE_GUN = new QuestArrowMachineGun(QUEST_CRAFT_ARROW, 0, -2, new ItemStack(Items.ARROW), "Arrow Machine Gun", "arrowMachineGun");
	
	public static final QuestCraftOnAltar QUEST_CRAFT_ENDER_PEARL = new QuestCraftOnAltar(QUEST_CRAFT_ARROW, +2, 0, "Craft Ender Pearl", "craftEnderPearl", 
			new ItemStack[]{new ItemStack(Items.ARROW, 2)}, 
			new ItemStack[]{new ItemStack(Items.ENDER_PEARL)}, 
			4,
			EnumWand.STICK);
	
	public static final QuestBuildAltar QUEST_BUILD_ALTAR_TIER_5 = new QuestBuildAltar(QUEST_BUILD_ALTAR_TIER_4, 0, +2, "Build Altar Tier 5", "buildAltarTier5", 5);
	
	public static final QuestCraftOnAltar QUEST_CRAFT_GLOWSTONE_DUST = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_5, -2, +1, "Craft Glowstone Dust", "craftGlowstoneDust", 
			new ItemStack[]{new ItemStack(Items.REDSTONE, 2), new ItemStack(Items.COAL, 2), new ItemStack(Blocks.LAPIS_BLOCK)}, 
			new ItemStack[]{new ItemStack(Items.GLOWSTONE_DUST)}, 
			5,
			EnumWand.BLAZE_ROD);
	public static final QuestCraftOnAltar QUEST_CRAFT_NETHERRACK = new QuestCraftOnAltar(QUEST_CRAFT_GLOWSTONE_DUST, -2, 0, "Craft Netherrack", "craftNetherrack", 
			new ItemStack[]{new ItemStack(Blocks.STONE), new ItemStack(Items.COAL, 2)}, 
			new ItemStack[]{new ItemStack(Blocks.NETHERRACK)}, 
			5, 
			EnumWand.BLAZE_ROD);
	public static final QuestCraftOnAltar QUEST_CRAFT_SOUL_SAND = new QuestCraftOnAltar(QUEST_CRAFT_NETHERRACK, -2, 0, "Craft Soul Sand", "craftSoulSand", 
			new ItemStack[]{new ItemStack(Blocks.SAND), new ItemStack(Items.COAL, 2)}, 
			new ItemStack[]{new ItemStack(Blocks.SOUL_SAND)}, 
			5, 
			EnumWand.BLAZE_ROD);
	public static final QuestCraftOnAltar QUEST_CRAFT_MAGMA_BLOCK = new QuestCraftOnAltar(QUEST_CRAFT_SOUL_SAND, -2, 0, "Craft Magma Block", "craftMagmaBlock", 
			new ItemStack[]{new ItemStack(Blocks.NETHERRACK), new ItemStack(Items.MAGMA_CREAM)}, 
			new ItemStack[]{new ItemStack(Blocks.field_189877_df)}, 
			5, 
			EnumWand.BLAZE_ROD);
	public static final QuestCraftOnAltar QUEST_CRAFT_NETHER_WART = new QuestCraftOnAltar(QUEST_CRAFT_MAGMA_BLOCK, -2, 0, "Craft Nether Wart", "craftNetherWart", 
			new ItemStack[]{new ItemStack(Items.GUNPOWDER), new ItemStack(Items.MAGMA_CREAM), new ItemStack(Items.SUGAR), new ItemStack(Items.REDSTONE)}, 
			new ItemStack[]{new ItemStack(Items.NETHER_WART)}, 
			5, 
			EnumWand.BLAZE_ROD);
	
	public static final QuestPick QUEST_PICK_BLAZE_ROD = new QuestPick(QUEST_BUILD_ALTAR_TIER_5, 0, +2,  "Pick Blaze Rod", "pickBlazeRod", new ItemStack(Items.BLAZE_ROD));
	
	public static final QuestMoveBlock QUEST_MOVE_BLOCK = new QuestMoveBlock(QUEST_PICK_BLAZE_ROD, -2, +1, new ItemStack(Items.BOOK), "Move block by book", "questMoveBlock", new ItemStack(Items.BOOK), EnumWand.BLAZE_ROD);
	
	public static final QuestCraftOnAltar QUEST_CRAFT_WITHER_SKELETON_HEAD = new QuestCraftOnAltar(QUEST_PICK_BLAZE_ROD, +2, +1, "Craft Wither Skeleton Skull", "craftWitherSkeletonSkull", 
			new ItemStack[]{new ItemStack(Blocks.COAL_BLOCK), ItemStackHelper.getHead(1, 0)}, 
			new ItemStack[]{ItemStackHelper.getHead(1, 1)}, 
			5, 
			EnumWand.BLAZE_ROD);
	
	public static final QuestBuildAltar QUEST_BUILD_ALTAR_TIER_6 = new QuestBuildAltar(QUEST_PICK_BLAZE_ROD, 0, +2, "Build Altar Tier 6", "buildAltarTier6", 6);
	
	public static final QuestCraftOnAltar QUEST_CRAFT_DIAMOND = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_6, +2, +1, "Craft Diamond", "craftDiamond", 
			new ItemStack[]{new ItemStack(Items.GOLD_INGOT, 8)}, 
			new ItemStack[]{new ItemStack(Items.DIAMOND)}, 
			6,
			EnumWand.BLAZE_ROD);
	
	public static final QuestBuildAltar QUEST_BUILD_ALTAR_TIER_7 = new QuestBuildAltar(QUEST_BUILD_ALTAR_TIER_6, 0, +2, "Build Altar Tier 7", "buildAltarTier7", 7);
	
	public static final QuestCraftOnAltar QUEST_CRAFT_GOLD_ORE = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_7, -2, +1, "Craft Gold Ore", "craftGoldOre", 
			new ItemStack[]{new ItemStack(Blocks.IRON_ORE, 16)}, 
			new ItemStack[]{new ItemStack(Blocks.GOLD_ORE)}, 
			7, 
			EnumWand.BLAZE_ROD);
	public static final QuestCraftOnAltar QUEST_CRAFT_COAL_ORE = new QuestCraftOnAltar(QUEST_CRAFT_GOLD_ORE, 0, +2, new ItemStack(Blocks.COAL_ORE), "Craft Coal Ore", "craftCoalOre", 
			new ItemStack[]{new ItemStack(Blocks.GOLD_ORE)}, 
			new ItemStack[]{new ItemStack(Blocks.COAL_ORE, 4)}, 
			7,
			EnumWand.BLAZE_ROD);
	public static final QuestCraftOnAltar QUEST_CRAFT_REDSTONE_ORE = new QuestCraftOnAltar(QUEST_CRAFT_GOLD_ORE, -2, 0, "Craft Redstone Ore", "craftRedstoneOre", 
			new ItemStack[]{new ItemStack(Blocks.GOLD_ORE, 16)}, 
			new ItemStack[]{new ItemStack(Blocks.REDSTONE_ORE)}, 
			7,
			EnumWand.BLAZE_ROD);
	public static final QuestCraftOnAltar QUEST_CRAFT_LAPIS_ORE = new QuestCraftOnAltar(QUEST_CRAFT_REDSTONE_ORE, -2, 0, "Craft Lapis Ore", "craftLapisOre", 
			new ItemStack[]{new ItemStack(Blocks.REDSTONE_ORE, 16)}, 
			new ItemStack[]{new ItemStack(Blocks.LAPIS_ORE)}, 
			7,
			EnumWand.BLAZE_ROD);
	public static final QuestCraftOnAltar QUEST_CRAFT_DIAMOND_ORE = new QuestCraftOnAltar(QUEST_CRAFT_LAPIS_ORE, -2, 0, "Craft Diamond Ore", "craftDiamondOre", 
			new ItemStack[]{new ItemStack(Blocks.LAPIS_ORE, 16)}, 
			new ItemStack[]{new ItemStack(Blocks.DIAMOND_ORE)}, 
			7,
			EnumWand.BLAZE_ROD);
	
	public static final QuestOreMultiplier QUEST_ORE_MULTIPLY_2 = new QuestOreMultiplier(QUEST_CRAFT_DIAMOND_ORE, 0, +2, new ItemStack(Items.CAULDRON), "Ore Doubling", "oreDoubling", 
			2, 
			EnumWand.NETHER_STAR);
	
	public static final QuestCraftOnAltar QUEST_CRAFT_EMERALD = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_7, +2, +1, "Craft Emerald", "craftEmerald", 
			new ItemStack[]{new ItemStack(Items.DIAMOND, 8)}, 
			new ItemStack[]{new ItemStack(Items.EMERALD)}, 
			7,
			EnumWand.BLAZE_ROD);
	public static final QuestCraftOnAltar QUEST_CRAFT_EMERALD_ORE = new QuestCraftOnAltar(QUEST_CRAFT_EMERALD, 0, +2, "Craft Emerald Ore", "craftEmeraldOre", 
			new ItemStack[]{new ItemStack(Blocks.DIAMOND_ORE, 8)}, 
			new ItemStack[]{new ItemStack(Blocks.EMERALD_ORE)}, 
			7,
			EnumWand.BLAZE_ROD);
	public static final QuestCraftOnAltar QUEST_CRAFT_NETHER_STAR = new QuestCraftOnAltar(QUEST_CRAFT_EMERALD, +2, 0, "Craft Nether Star", "craftNetherStar", 
			new ItemStack[]{new ItemStack(Blocks.EMERALD_BLOCK, 4)},
			new ItemStack[]{new ItemStack(Items.NETHER_STAR)}, 
			7,
			EnumWand.BLAZE_ROD);
	
	public static final QuestItemMagnet QUEST_MAGNET = new QuestItemMagnet(QUEST_CRAFT_NETHER_STAR, 0, +2, new ItemStack(Items.NETHER_STAR), "Item Magnet", "itemMagnet");
	
	public static final QuestCraftOnAltar QUEST_CRAFT_DRAGON_EGG = new QuestCraftOnAltar(QUEST_CRAFT_NETHER_STAR, +2, 0, "Craft Dragon Egg", "craftDragonEgg", 
			new ItemStack[]{new ItemStack(Items.NETHER_STAR, 4)}, 
			new ItemStack[]{new ItemStack(Blocks.DRAGON_EGG)}, 
			7,
			EnumWand.BLAZE_ROD);
	
	//===================================================================================================
	
	public static final QuestCraft QUEST_CRAFT_BOOK = new QuestCraft(QUEST_CRAFT_STICK, +2, 0, new ItemStack(Items.BOOK), "Craft Book", "craftBook");
	public static final QuestCraft QUEST_CRAFT_BOOKSHELF = new QuestCraft(QUEST_CRAFT_BOOK, +2, 0, new ItemStack(Blocks.BOOKSHELF), "Craft Bookshelf", "craftBookshelf");
	public static final QuestMineBlock QUEST_MINE_DIAMOND = new QuestMineBlock(QUEST_CRAFT_BOOKSHELF, +2, 0, new ItemStack(Items.DIAMOND), "Mine Diamond", "mineDiamond", new Block[]{Blocks.DIAMOND_ORE});
	public static final QuestMineBlock QUEST_MINE_OBSIDIAN = new QuestMineBlock(QUEST_MINE_DIAMOND, +2, 0, new ItemStack(Blocks.OBSIDIAN), "Mine Obsidian", "mineObsidian", new Block[]{Blocks.OBSIDIAN});
	public static final QuestCraft QUEST_CRAFT_ENCHANTING_TABLE = new QuestCraft(QUEST_MINE_OBSIDIAN, +2, 0, new ItemStack(Blocks.ENCHANTING_TABLE), "Craft Enchanting Table", "craftEnchantingTable");
	public static final QuestChunkLoader QUEST_CHUNK_LOADER = new QuestChunkLoader(QUEST_CRAFT_ENCHANTING_TABLE, 0, +2, new ItemStack(Blocks.ENCHANTING_TABLE), "Build ChunkLoader", "chunkLoader");
	//TODO: add quest for enchanting sanctuary
	
	//===================================================================================================
	
	static
	{
		// TODO: for each crafting create an QuestCraftOnAltar
		//List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
	}
}