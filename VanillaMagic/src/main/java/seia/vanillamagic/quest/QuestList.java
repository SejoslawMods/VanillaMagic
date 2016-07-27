package seia.vanillamagic.quest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.quest.quarry.QuestQuarry;
import seia.vanillamagic.quest.spell.QuestCastSpellInAir;
import seia.vanillamagic.quest.spell.QuestCastSpellOnBlock;
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
	public static final QuestCastSpellOnBlock QUEST_CAST_SPELL_LIGHTER = new QuestCastSpellOnBlock(QUEST_CRAFT_STICK.getAchievement(), 
			QUEST_CRAFT_STICK.getPosX(), 
			QUEST_CRAFT_STICK.getPosY() - 2, 
			EnumSpell.LIGHTER);
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_SMALL_FIREBALL = new QuestCastSpellInAir(QUEST_CAST_SPELL_LIGHTER.getAchievement(), 
			QUEST_CAST_SPELL_LIGHTER.getPosX(), 
			QUEST_CAST_SPELL_LIGHTER.getPosY() - 2, 
			EnumSpell.SMALL_FIREBALL);
	
	// SUMMONS Passive Entities
	public static final QuestCastSpellOnBlock QUEST_SUMMON_CHICKEN = new QuestCastSpellOnBlock(QUEST_CAST_SPELL_SMALL_FIREBALL.getAchievement(), 
			QUEST_CAST_SPELL_SMALL_FIREBALL.getPosX() - 2, 
			QUEST_CAST_SPELL_SMALL_FIREBALL.getPosY(), 
			EnumSpell.SUMMON_CHICKEN);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_PIG = new QuestCastSpellOnBlock(QUEST_SUMMON_CHICKEN.getAchievement(), 
			QUEST_SUMMON_CHICKEN.getPosX() - 2, 
			QUEST_SUMMON_CHICKEN.getPosY(), 
			EnumSpell.SUMMON_PIG);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_COW = new QuestCastSpellOnBlock(QUEST_SUMMON_PIG.getAchievement(), 
			QUEST_SUMMON_PIG.getPosX() - 2, 
			QUEST_SUMMON_PIG.getPosY(), 
			EnumSpell.SUMMON_COW);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_MOOSHROOM = new QuestCastSpellOnBlock(QUEST_SUMMON_COW.getAchievement(), 
			QUEST_SUMMON_COW.getPosX(), 
			QUEST_SUMMON_COW.getPosY() + 2, 
			EnumSpell.SUMMON_MOOSHROOM);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_SHEEP = new QuestCastSpellOnBlock(QUEST_SUMMON_COW.getAchievement(), 
			QUEST_SUMMON_COW.getPosX() - 2, 
			QUEST_SUMMON_COW.getPosY(), 
			EnumSpell.SUMMON_SHEEP);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_WOLF = new QuestCastSpellOnBlock(QUEST_SUMMON_SHEEP.getAchievement(), 
			QUEST_SUMMON_SHEEP.getPosX(), 
			QUEST_SUMMON_SHEEP.getPosY() + 2, 
			EnumSpell.SUMMON_WOLF);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_RABBIT = new QuestCastSpellOnBlock(QUEST_SUMMON_SHEEP.getAchievement(), 
			QUEST_SUMMON_SHEEP.getPosX() - 2, 
			QUEST_SUMMON_SHEEP.getPosY(), 
			EnumSpell.SUMMON_RABBIT);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_HORSE = new QuestCastSpellOnBlock(QUEST_SUMMON_RABBIT.getAchievement(), 
			QUEST_SUMMON_RABBIT.getPosX() - 2, 
			QUEST_SUMMON_RABBIT.getPosY(), 
			EnumSpell.SUMMON_HORSE);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_VILLAGER = new QuestCastSpellOnBlock(QUEST_SUMMON_HORSE.getAchievement(), 
			QUEST_SUMMON_HORSE.getPosX() - 2, 
			QUEST_SUMMON_HORSE.getPosY(), 
			EnumSpell.SUMMON_VILLAGER);
	// END SUMMONS Passive Entities
	
	//SUMMONS Monster
	public static final QuestCastSpellOnBlock QUEST_SUMMON_ZOMBIE = new QuestCastSpellOnBlock(QUEST_SUMMON_CHICKEN.getAchievement(), 
			QUEST_SUMMON_CHICKEN.getPosX(), 
			QUEST_SUMMON_CHICKEN.getPosY() - 2, 
			EnumSpell.SUMMON_ZOMBIE);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_CREEPER = new QuestCastSpellOnBlock(QUEST_SUMMON_ZOMBIE.getAchievement(), 
			QUEST_SUMMON_ZOMBIE.getPosX() - 2, 
			QUEST_SUMMON_ZOMBIE.getPosY(), 
			EnumSpell.SUMMON_CREEPER);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_SKELETON = new QuestCastSpellOnBlock(QUEST_SUMMON_CREEPER.getAchievement(), 
			QUEST_SUMMON_CREEPER.getPosX() - 2, 
			QUEST_SUMMON_CREEPER.getPosY(), 
			EnumSpell.SUMMON_SKELETON);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_BLAZE = new QuestCastSpellOnBlock(QUEST_SUMMON_SKELETON.getAchievement(), 
			QUEST_SUMMON_SKELETON.getPosX() - 2, 
			QUEST_SUMMON_SKELETON.getPosY(), 
			EnumSpell.SUMMON_BLAZE);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_MAGMA_CUBE = new QuestCastSpellOnBlock(QUEST_SUMMON_BLAZE.getAchievement(), 
			QUEST_SUMMON_BLAZE.getPosX() - 2, 
			QUEST_SUMMON_BLAZE.getPosY(), 
			EnumSpell.SUMMON_MAGMA_CUBE);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_GHAST = new QuestCastSpellOnBlock(QUEST_SUMMON_MAGMA_CUBE.getAchievement(), 
			QUEST_SUMMON_MAGMA_CUBE.getPosX() - 2, 
			QUEST_SUMMON_MAGMA_CUBE.getPosY(), 
			EnumSpell.SUMMON_GHAST);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_ENDERMAN = new QuestCastSpellOnBlock(QUEST_SUMMON_GHAST.getAchievement(), 
			QUEST_SUMMON_GHAST.getPosX() - 2, 
			QUEST_SUMMON_GHAST.getPosY(), 
			EnumSpell.SUMMON_ENDERMAN);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_SPIDER = new QuestCastSpellOnBlock(QUEST_SUMMON_ENDERMAN.getAchievement(), 
			QUEST_SUMMON_ENDERMAN.getPosX() - 2, 
			QUEST_SUMMON_ENDERMAN.getPosY(), 
			EnumSpell.SUMMON_SPIDER);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_SLIME = new QuestCastSpellOnBlock(QUEST_SUMMON_SPIDER.getAchievement(), 
			QUEST_SUMMON_SPIDER.getPosX() - 2, 
			QUEST_SUMMON_SPIDER.getPosY(), 
			EnumSpell.SUMMON_SLIME);
	public static final QuestCastSpellOnBlock QUEST_SUMMON_WITCH = new QuestCastSpellOnBlock(QUEST_SUMMON_SLIME.getAchievement(), 
			QUEST_SUMMON_SLIME.getPosX() - 2, 
			QUEST_SUMMON_SLIME.getPosY(), 
			EnumSpell.SUMMON_WITCH);
	// END SUMMONS Monster
	
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_LARGE_FIREBALL = new QuestCastSpellInAir(QUEST_CAST_SPELL_SMALL_FIREBALL.getAchievement(), 
			QUEST_CAST_SPELL_SMALL_FIREBALL.getPosX() + 2, 
			QUEST_CAST_SPELL_SMALL_FIREBALL.getPosY(),
			EnumSpell.LARGE_FIREBALL);
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_LIGHTNING_BOLT = new QuestCastSpellInAir(QUEST_CAST_SPELL_LARGE_FIREBALL.getAchievement(), 
			QUEST_CAST_SPELL_LARGE_FIREBALL.getPosX(), 
			QUEST_CAST_SPELL_LARGE_FIREBALL.getPosY() + 2, 
			EnumSpell.LIGHTNING_BOLT);
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_TELEPORT = new QuestCastSpellInAir(QUEST_CAST_SPELL_LARGE_FIREBALL.getAchievement(), 
			QUEST_CAST_SPELL_LARGE_FIREBALL.getPosX(), 
			QUEST_CAST_SPELL_LARGE_FIREBALL.getPosY() - 2, 
			EnumSpell.TELEPORT);
	public static final QuestCastSpellInAir QUEST_CAST_SPELL_METEOR = new QuestCastSpellInAir(QUEST_CAST_SPELL_LARGE_FIREBALL.getAchievement(), 
			QUEST_CAST_SPELL_LARGE_FIREBALL.getPosX() + 2, 
			QUEST_CAST_SPELL_LARGE_FIREBALL.getPosY(), 
			EnumSpell.METEOR);
	
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

	public static final QuestBuildAltar QUEST_BUILD_ALTAR_TIER_1 = new QuestBuildAltar(QUEST_MINE_REDSTONE.getAchievement(), 
			QUEST_MINE_REDSTONE.getPosX(), 
			QUEST_MINE_REDSTONE.getPosY() + 2, 
			Items.CAULDRON, 
			"Build Altar Tier 1", 
			"buildAltarTier1", 
			1);
	public static final QuestCraftOnAltar QUEST_CRAFT_GUNPOWDER = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_1.getAchievement(), 
			QUEST_BUILD_ALTAR_TIER_1.getPosX() - 2, 
			QUEST_BUILD_ALTAR_TIER_1.getPosY() + 1,  
			"Craft Gunpowder", 
			"craftGunpowder", 
			new ItemStack[]{new ItemStack(Items.SUGAR), new ItemStack(Items.COAL)}, 
			new ItemStack(Items.GUNPOWDER), 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_SUGAR = new QuestCraftOnAltar(QUEST_CRAFT_GUNPOWDER.getAchievement(), 
			QUEST_CRAFT_GUNPOWDER.getPosX() - 2, 
			QUEST_CRAFT_GUNPOWDER.getPosY(),  
			"Craft Sugar", 
			"craftSugar", 
			new ItemStack[]{ItemStackHelper.getSugarCane()}, 
			new ItemStack(Items.SUGAR), 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_DIRT = new QuestCraftOnAltar(QUEST_CRAFT_SUGAR.getAchievement(), 
			QUEST_CRAFT_SUGAR.getPosX() - 2, 
			QUEST_CRAFT_SUGAR.getPosY(),  
			"Craft Dirt", 
			"craftDirt", 
			new ItemStack[]{new ItemStack(Blocks.SAND)}, 
			new ItemStack(Blocks.DIRT), 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_SAND = new QuestCraftOnAltar(QUEST_CRAFT_DIRT.getAchievement(), 
			QUEST_CRAFT_DIRT.getPosX(), 
			QUEST_CRAFT_DIRT.getPosY() - 2,  
			"Craft Sand", 
			"craftSand", 
			new ItemStack[]{new ItemStack(Blocks.COBBLESTONE)}, 
			new ItemStack(Blocks.SAND), 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_COBBLESTONE = new QuestCraftOnAltar(QUEST_CRAFT_DIRT.getAchievement(), 
			QUEST_CRAFT_DIRT.getPosX(), 
			QUEST_CRAFT_DIRT.getPosY() + 2,  
			"Craft Cobblestone", 
			"craftCobblestone", 
			new ItemStack[]{new ItemStack(Blocks.DIRT)}, 
			new ItemStack(Blocks.COBBLESTONE), 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_LEATHER = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_1.getAchievement(), 
			QUEST_BUILD_ALTAR_TIER_1.getPosX() + 2, 
			QUEST_BUILD_ALTAR_TIER_1.getPosY() + 1,  
			"Craft Leather", 
			"craftLeather", 
			new ItemStack[]{new ItemStack(Items.ROTTEN_FLESH)}, 
			new ItemStack(Items.LEATHER), 
			1,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_GLASS = new QuestCraftOnAltar(QUEST_CRAFT_SAND.getAchievement(), 
			QUEST_CRAFT_SAND.getPosX() - 2, 
			QUEST_CRAFT_SAND.getPosY(),  
			"Craft Glass", 
			"craftGlass",
			new ItemStack[]{new ItemStack(Blocks.SAND), new ItemStack(Items.COAL)},
			new ItemStack(Blocks.GLASS),
			1,
			EnumWand.STICK);
	
	public static final QuestBuildAltar QUEST_BUILD_ALTAR_TIER_2 = new QuestBuildAltar(QUEST_BUILD_ALTAR_TIER_1.getAchievement(), 
			QUEST_BUILD_ALTAR_TIER_1.getPosX(), 
			QUEST_BUILD_ALTAR_TIER_1.getPosY() + 2, 
			Items.CAULDRON, 
			"Build Altar Tier 2", 
			"buildAltarTier2", 
			2);
	public static final QuestCraftOnAltar QUEST_CRAFT_IRON_ORE = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_2.getAchievement(), 
			QUEST_BUILD_ALTAR_TIER_2.getPosX() + 2, 
			QUEST_BUILD_ALTAR_TIER_2.getPosY() + 1,  
			"Craft Iron Ore", 
			"craftIronOre", 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 10), new ItemStack(Blocks.COBBLESTONE, 10)}, 
			new ItemStack(Blocks.IRON_ORE), 
			2,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_CACTUS = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_2.getAchievement(), 
			QUEST_BUILD_ALTAR_TIER_2.getPosX() - 2, 
			QUEST_BUILD_ALTAR_TIER_2.getPosY() + 1,  
			"Craft Cactus", 
			"craftCactus", 
			new ItemStack[]{new ItemStack(Blocks.SAPLING, 4)}, 
			new ItemStack(Blocks.CACTUS), 
			2,
			EnumWand.STICK);
	
	public static final QuestBuildAltar QUEST_BUILD_ALTAR_TIER_3 = new QuestBuildAltar(QUEST_BUILD_ALTAR_TIER_2.getAchievement(), 
			QUEST_BUILD_ALTAR_TIER_2.getPosX(), 
			QUEST_BUILD_ALTAR_TIER_2.getPosY() + 2,
			Items.CAULDRON, 
			"Build Altar Tier 3", 
			"buildAltarTier3", 
			3);
	public static final QuestSmeltOnAltar QUEST_SMELT_ON_ALTAR = new QuestSmeltOnAltar(QUEST_BUILD_ALTAR_TIER_3.getAchievement(), 
			QUEST_BUILD_ALTAR_TIER_3.getPosX() - 2, 
			QUEST_BUILD_ALTAR_TIER_3.getPosY() + 1, 
			Item.getItemFromBlock(Blocks.FURNACE), 
			"Smelt in Altar", 
			"smeltInAltar", 
			1, 
			EnumWand.STICK);
	public static final QuestQuarry QUEST_QUARRY = new QuestQuarry(QUEST_SMELT_ON_ALTAR.getAchievement(), 
			QUEST_SMELT_ON_ALTAR.getPosX() - 2, 
			QUEST_SMELT_ON_ALTAR.getPosY(), 
			"Quarry !!!", 
			"quarry");
	/*
	public static final QuestFullTreeCut QUEST_FULL_TREE_CUT = new QuestFullTreeCut(QUEST_SMELT_ON_ALTAR.getAchievement(), 
			QUEST_SMELT_ON_ALTAR.getPosX() - 2, 
			QUEST_SMELT_ON_ALTAR.getPosY(), 
			Items.DIAMOND_AXE, 
			"Full Tree Cut", 
			"fullTreeCut");
			*/
			
	public static final QuestCraftOnAltar QUEST_CRAFT_GOLD_INGOT = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_3.getAchievement(), 
			QUEST_BUILD_ALTAR_TIER_3.getPosX() + 2, 
			QUEST_BUILD_ALTAR_TIER_3.getPosY() + 1, 
			Items.GOLD_INGOT, 
			"Craft Gold Ingot", 
			"craftGoldIngot", 
			new ItemStack[]{new ItemStack(Items.IRON_INGOT, 8)}, 
			new ItemStack(Items.GOLD_INGOT), 
			3,
			EnumWand.STICK);
	
	public static final QuestBuildAltar QUEST_BUILD_ALTAR_TIER_4 = new QuestBuildAltar(QUEST_BUILD_ALTAR_TIER_3.getAchievement(), 
			QUEST_BUILD_ALTAR_TIER_3.getPosX(), 
			QUEST_BUILD_ALTAR_TIER_3.getPosY() + 2,
			Items.CAULDRON, 
			"Build Altar Tier 4", 
			"buildAltarTier4", 
			4);
	public static final QuestCraftOnAltar QUEST_CRAFT_ROTTEN_FLESH = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_4.getAchievement(), 
			QUEST_BUILD_ALTAR_TIER_4.getPosX() + 2, 
			QUEST_BUILD_ALTAR_TIER_4.getPosY() + 1, 
			Items.ROTTEN_FLESH, 
			"Craft Rotten Flesh", 
			"craftRottenFlesh", 
			new ItemStack[]{new ItemStack(Items.LEATHER)}, 
			new ItemStack(Items.ROTTEN_FLESH), 
			4,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_STRING = new QuestCraftOnAltar(QUEST_CRAFT_ROTTEN_FLESH.getAchievement(), 
			QUEST_CRAFT_ROTTEN_FLESH.getPosX() + 2, 
			QUEST_CRAFT_ROTTEN_FLESH.getPosY(),  
			"Craft String", 
			"craftString", 
			new ItemStack[]{new ItemStack(Items.ROTTEN_FLESH, 2)}, 
			new ItemStack(Items.STRING), 
			4,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_SPIDER_EYE = new QuestCraftOnAltar(QUEST_CRAFT_STRING.getAchievement(), 
			QUEST_CRAFT_STRING.getPosX() + 2, 
			QUEST_CRAFT_STRING.getPosY(),  
			"Craft Spider Eye", 
			"craftSpiderEye", 
			new ItemStack[]{new ItemStack(Items.STRING, 2)}, 
			new ItemStack(Items.SPIDER_EYE), 
			4,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_BONE = new QuestCraftOnAltar(QUEST_CRAFT_SPIDER_EYE.getAchievement(), 
			QUEST_CRAFT_SPIDER_EYE.getPosX() + 2, 
			QUEST_CRAFT_SPIDER_EYE.getPosY(),  
			"Craft Bone", 
			"craftBone", 
			new ItemStack[]{new ItemStack(Items.SPIDER_EYE, 2)}, 
			new ItemStack(Items.BONE), 
			4,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_ARROW = new QuestCraftOnAltar(QUEST_CRAFT_BONE.getAchievement(), 
			QUEST_CRAFT_BONE.getPosX() + 2, 
			QUEST_CRAFT_BONE.getPosY(), 
			"Craft Arrow", 
			"craftArrow", 
			new ItemStack[]{new ItemStack(Items.BONE, 2)}, 
			new ItemStack(Items.ARROW), 
			4,
			EnumWand.STICK);
	public static final QuestCraftOnAltar QUEST_CRAFT_ENDER_PEARL = new QuestCraftOnAltar(QUEST_CRAFT_ARROW.getAchievement(), 
			QUEST_CRAFT_ARROW.getPosX() + 2, 
			QUEST_CRAFT_ARROW.getPosY(),  
			"Craft Ender Pearl", 
			"craftEnderPearl", 
			new ItemStack[]{new ItemStack(Items.ARROW, 2)}, 
			new ItemStack(Items.ENDER_PEARL), 
			4,
			EnumWand.STICK);
	
	public static final QuestBuildAltar QUEST_BUILD_ALTAR_TIER_5 = new QuestBuildAltar(QUEST_BUILD_ALTAR_TIER_4.getAchievement(), 
			QUEST_BUILD_ALTAR_TIER_4.getPosX(), 
			QUEST_BUILD_ALTAR_TIER_4.getPosY() + 2,
			Items.CAULDRON, 
			"Build Altar Tier 5", 
			"buildAltarTier5", 
			5);
	public static final QuestCraftOnAltar QUEST_CRAFT_GLOWSTONE_DUST = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_5.getAchievement(), 
			QUEST_BUILD_ALTAR_TIER_5.getPosX() - 2, 
			QUEST_BUILD_ALTAR_TIER_5.getPosY() + 1,  
			"Craft Glowstone Dust", 
			"craftGlowstoneDust", 
			new ItemStack[]{new ItemStack(Items.REDSTONE, 2), new ItemStack(Items.COAL, 2), new ItemStack(Blocks.LAPIS_BLOCK)}, 
			new ItemStack(Items.GLOWSTONE_DUST), 
			5,
			EnumWand.STICK);
	
	public static final QuestPick QUEST_PICK_BLAZE_ROD = new QuestPick(QUEST_BUILD_ALTAR_TIER_5.getAchievement(), 
			QUEST_BUILD_ALTAR_TIER_5.getPosX(), 
			QUEST_BUILD_ALTAR_TIER_5.getPosY() + 2,  
			"Pick Blaze Rod", 
			"pickBlazeRod", 
			new ItemStack(Items.BLAZE_ROD));
	public static final QuestBuildAltar QUEST_BUILD_ALTAR_TIER_6 = new QuestBuildAltar(QUEST_PICK_BLAZE_ROD.getAchievement(), 
			QUEST_PICK_BLAZE_ROD.getPosX(), 
			QUEST_PICK_BLAZE_ROD.getPosY() + 2,
			Items.CAULDRON, 
			"Build Altar Tier 6", 
			"buildAltarTier6", 
			6);
	public static final QuestCraftOnAltar QUEST_CRAFT_DIAMOND = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_6.getAchievement(), 
			QUEST_BUILD_ALTAR_TIER_6.getPosX() + 2, 
			QUEST_BUILD_ALTAR_TIER_6.getPosY() + 1,  
			"Craft Diamond", 
			"craftDiamond", 
			new ItemStack[]{new ItemStack(Items.GOLD_INGOT, 8)}, 
			new ItemStack(Items.DIAMOND), 
			6,
			EnumWand.BLAZE_ROD);
	
	public static final QuestBuildAltar QUEST_BUILD_ALTAR_TIER_7 = new QuestBuildAltar(QUEST_BUILD_ALTAR_TIER_6.getAchievement(), 
			QUEST_BUILD_ALTAR_TIER_6.getPosX(), 
			QUEST_BUILD_ALTAR_TIER_6.getPosY() + 2,
			Items.CAULDRON, 
			"Build Altar Tier 7", 
			"buildAltarTier7", 
			7);
	public static final QuestCraftOnAltar QUEST_CRAFT_EMERALD = new QuestCraftOnAltar(QUEST_BUILD_ALTAR_TIER_7.getAchievement(), 
			QUEST_BUILD_ALTAR_TIER_7.getPosX() + 2, 
			QUEST_BUILD_ALTAR_TIER_7.getPosY() + 1,  
			"Craft Emerald", 
			"craftEmerald", 
			new ItemStack[]{new ItemStack(Items.DIAMOND, 8)}, 
			new ItemStack(Items.EMERALD), 
			7,
			EnumWand.BLAZE_ROD);
	public static final QuestCraftOnAltar QUEST_CRAFT_NETHER_STAR = new QuestCraftOnAltar(QUEST_CRAFT_EMERALD.getAchievement(), 
			QUEST_CRAFT_EMERALD.getPosX() + 2, 
			QUEST_CRAFT_EMERALD.getPosY(),  
			"Craft Nether Star", 
			"craftNetherStar", 
			new ItemStack[]{new ItemStack(Blocks.EMERALD_BLOCK, 4)},
			new ItemStack(Items.NETHER_STAR), 
			7,
			EnumWand.BLAZE_ROD);
	public static final QuestItemMagnet QUEST_MAGNET = new QuestItemMagnet(QUEST_CRAFT_NETHER_STAR.getAchievement(), 
			QUEST_CRAFT_NETHER_STAR.getPosX(), 
			QUEST_CRAFT_NETHER_STAR.getPosY() + 2, 
			"Item Magnet", 
			"itemMagnet");
	public static final QuestCraftOnAltar QUEST_CRAFT_DRAGON_EGG = new QuestCraftOnAltar(QUEST_CRAFT_NETHER_STAR.getAchievement(), 
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
		// TODO: for each crafting create an QuestCraftOnAltar
		//List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
	}
}