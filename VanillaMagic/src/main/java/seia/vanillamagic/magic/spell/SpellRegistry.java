package seia.vanillamagic.magic.spell;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import seia.vanillamagic.config.VMConfig;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.magic.spell.spells.SpellFusRoDah;
import seia.vanillamagic.magic.spell.spells.SpellLargeFireball;
import seia.vanillamagic.magic.spell.spells.SpellLighter;
import seia.vanillamagic.magic.spell.spells.SpellLightningBolt;
import seia.vanillamagic.magic.spell.spells.SpellMeteor;
import seia.vanillamagic.magic.spell.spells.SpellMoveInAir;
import seia.vanillamagic.magic.spell.spells.SpellPullEntityToPlayer;
import seia.vanillamagic.magic.spell.spells.SpellSmallFireball;
import seia.vanillamagic.magic.spell.spells.SpellTeleport;
import seia.vanillamagic.magic.spell.spells.SpellTeleportToEnd;
import seia.vanillamagic.magic.spell.spells.SpellTeleportToNether;
import seia.vanillamagic.magic.spell.spells.SpellWaterFreeze;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonBlaze;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonCreeper;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonEnderman;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonEndermite;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonEvoker;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonGhast;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonGiant;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonGuardian;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonHostile;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonMagmaCube;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonPigman;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonPoalrBear;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonShulker;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonSilverfish;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonSkeleton;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonSlime;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonSpider;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonSpiderJockey;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonVex;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonVindicator;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonWitch;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonWither;
import seia.vanillamagic.magic.spell.spells.summon.hostile.SpellSummonZombie;
import seia.vanillamagic.magic.spell.spells.summon.passive.SpellSummonChicken;
import seia.vanillamagic.magic.spell.spells.summon.passive.SpellSummonCow;
import seia.vanillamagic.magic.spell.spells.summon.passive.SpellSummonHorse;
import seia.vanillamagic.magic.spell.spells.summon.passive.SpellSummonIronGolem;
import seia.vanillamagic.magic.spell.spells.summon.passive.SpellSummonLama;
import seia.vanillamagic.magic.spell.spells.summon.passive.SpellSummonMooshroom;
import seia.vanillamagic.magic.spell.spells.summon.passive.SpellSummonPassive;
import seia.vanillamagic.magic.spell.spells.summon.passive.SpellSummonPig;
import seia.vanillamagic.magic.spell.spells.summon.passive.SpellSummonRabbit;
import seia.vanillamagic.magic.spell.spells.summon.passive.SpellSummonSheep;
import seia.vanillamagic.magic.spell.spells.summon.passive.SpellSummonSnowman;
import seia.vanillamagic.magic.spell.spells.summon.passive.SpellSummonSquid;
import seia.vanillamagic.magic.spell.spells.summon.passive.SpellSummonVillager;
import seia.vanillamagic.magic.spell.spells.summon.passive.SpellSummonWolf;
import seia.vanillamagic.magic.spell.spells.weather.SpellWeatherClear;
import seia.vanillamagic.magic.spell.spells.weather.SpellWeatherRain;
import seia.vanillamagic.magic.spell.spells.weather.SpellWeatherThunderStorm;
import seia.vanillamagic.magic.wand.IWand;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.util.ItemStackHelper;

public class SpellRegistry
{
	// All registered Spells.
	private static final List<ISpell> _SPELLS = new ArrayList<>();
	// All passive Entities.
	private static final List<ISpell> _SPELLS_PASSIVE = new ArrayList<>();
	// All hostile Entities.
	private static final List<ISpell> _SPELLS_HOSTILE = new ArrayList();
	
	// Wand that is used for summoning.
	private static final IWand _SUMMON_REQUIRED_WAND = WandRegistry.WAND_NETHER_STAR;
	private static final int _SUMMON_FRIENDLY_COST_ITEMSTACK = VMConfig.spellCostSummonFriendly;
	private static final int _SUMMON_HOSTILE_COST_ITEMSTACK = VMConfig.spellCostSummonHostile;
	
	public static final Spell SPELL_LIGHTER = new SpellLighter(0, "Flint and Steel Clone", "spellFlintAndSteel", WandRegistry.WAND_STICK, new ItemStack(Items.COAL));
	public static final Spell SPELL_SMALL_FIREBALL = new SpellSmallFireball(1, "Feel like Blaze", "spellSmallFireball", WandRegistry.WAND_BLAZE_ROD, new ItemStack(Items.REDSTONE, 2));
	public static final Spell SPELL_LARGE_FIREBALL = new SpellLargeFireball(2, "Feel like Ghast", "spellLargeFireball", WandRegistry.WAND_BLAZE_ROD, new ItemStack(Items.GHAST_TEAR));
	public static final Spell SPELL_TELEPORT = new SpellTeleport(3, "Teleportation !!!", "spellTeleport", WandRegistry.WAND_BLAZE_ROD, new ItemStack(Items.MAGMA_CREAM));
	public static final Spell SPELL_METEOR = new SpellMeteor(4, "Meteor !!!", "spellSummonMeteor", WandRegistry.WAND_NETHER_STAR, new ItemStack(Blocks.GOLD_BLOCK, 10));
	public static final Spell SPELL_LIGHTNING_BOLT = new SpellLightningBolt(5, "Thunder !!!", "spellSummonLightningBolt", WandRegistry.WAND_BLAZE_ROD, new ItemStack(Items.GUNPOWDER, 32));
	public static final Spell SPELL_FUS_RO_DAH = new SpellFusRoDah(6, "Fus-Ro-Dah !!!", "spellFusRoDah", WandRegistry.WAND_BLAZE_ROD, new ItemStack(Items.DRAGON_BREATH));
	public static final Spell SPELL_TELEPORT_TO_NETHER = new SpellTeleportToNether(7, "Teleport to Nether", "spellTeleportToNether", WandRegistry.WAND_BLAZE_ROD, new ItemStack(Items.NETHER_WART, 2));
	public static final Spell SPELL_TELEPORT_TO_END = new SpellTeleportToEnd(8, "Teleport to End", "spellTeleportToEnd", WandRegistry.WAND_BLAZE_ROD, new ItemStack(Items.END_CRYSTAL));
	public static final Spell SPELL_MOVE_IN_AIR = new SpellMoveInAir(9, "Move in air", "spellMoveInAir", WandRegistry.WAND_BLAZE_ROD, new ItemStack(Items.FEATHER));
	public static final Spell SPELL_PULL_ENTITY_TO_PLAYER = new SpellPullEntityToPlayer(10, "Pull Entity to Player", "spellPullEntityToPlayer", WandRegistry.WAND_BLAZE_ROD, new ItemStack(Items.STRING, 4));
	public static final Spell SPELL_WATER_FREEZE = new SpellWaterFreeze(11, "Freeze Water 3x3", "spellFreezeWater3x3", WandRegistry.WAND_BLAZE_ROD, new ItemStack(Blocks.SNOW));
	public static final Spell SPELL_WEATHER_RAIN = new SpellWeatherRain(12, "Make rain", "spellWeatherRain", WandRegistry.WAND_BLAZE_ROD, new ItemStack(Items.POTIONITEM));
	public static final Spell SPELL_WEATHER_CLEAR = new SpellWeatherClear(13, "Clear weather", "spellWeatherClear", WandRegistry.WAND_BLAZE_ROD, new ItemStack(Items.GLASS_BOTTLE, 1));
	public static final Spell SPELL_WEATHER_THUNDERSTORM = new SpellWeatherThunderStorm(14, "Make thunderstorm", "spellWeatherThunderstorm", WandRegistry.WAND_BLAZE_ROD, ItemStackHelper.getHead(1, 4));
	
	// Passive Mobs Summon
	public static final SpellSummonPassive SPELL_SUMMON_CHICKEN = new SpellSummonChicken(100, "Summon Chicken", "spellSummonChicken", _SUMMON_REQUIRED_WAND, new ItemStack(Items.CHICKEN, _SUMMON_FRIENDLY_COST_ITEMSTACK));
	public static final SpellSummonPassive SPELL_SUMMON_PIG = new SpellSummonPig(101, "Summon Pig", "spellSummonPig", _SUMMON_REQUIRED_WAND, new ItemStack(Items.PORKCHOP, _SUMMON_FRIENDLY_COST_ITEMSTACK));
	public static final SpellSummonPassive SPELL_SUMMON_COW = new SpellSummonCow(102, "Summon Cow", "spellSummonCow", _SUMMON_REQUIRED_WAND, new ItemStack(Items.BEEF, _SUMMON_FRIENDLY_COST_ITEMSTACK));
	public static final SpellSummonPassive SPELL_SUMMON_MOOSHROOM = new SpellSummonMooshroom(103, "Summon Mooshroom", "spellSummonMooshroom", _SUMMON_REQUIRED_WAND, new ItemStack(Items.COOKED_BEEF, _SUMMON_FRIENDLY_COST_ITEMSTACK));
	public static final SpellSummonPassive SPELL_SUMMON_SHEEP = new SpellSummonSheep(104, "Summon Sheep", "spellSummonSheep", _SUMMON_REQUIRED_WAND, new ItemStack(Items.MUTTON, _SUMMON_FRIENDLY_COST_ITEMSTACK));
	public static final SpellSummonPassive SPELL_SUMMON_WOLF = new SpellSummonWolf(105, "Summon Wolf", "spellSummonWolf", _SUMMON_REQUIRED_WAND, new ItemStack(Items.COOKED_MUTTON, _SUMMON_FRIENDLY_COST_ITEMSTACK));
	public static final SpellSummonPassive SPELL_SUMMON_RABBIT = new SpellSummonRabbit(106, "Summon Rabbit", "spellSummonRabbit", _SUMMON_REQUIRED_WAND, new ItemStack(Items.RABBIT, _SUMMON_FRIENDLY_COST_ITEMSTACK));
	public static final SpellSummonPassive SPELL_SUMMON_HORSE = new SpellSummonHorse(107, "Summon Horse", "spellSummonHorse", _SUMMON_REQUIRED_WAND, new ItemStack(Items.SADDLE));
	public static final SpellSummonPassive SPELL_SUMMON_VILLAGER = new SpellSummonVillager(108, "Summon Villager", "spellSummonVillager", _SUMMON_REQUIRED_WAND, new ItemStack(Items.BOOK, _SUMMON_FRIENDLY_COST_ITEMSTACK));
	public static final SpellSummonPassive SPELL_SUMMON_IRON_GOLEM = new SpellSummonIronGolem(109, "Summon IronGolem", "spellSummonIronGolem", _SUMMON_REQUIRED_WAND, new ItemStack(Items.IRON_INGOT, _SUMMON_FRIENDLY_COST_ITEMSTACK));
	public static final SpellSummonPassive SPELL_SUMMON_SQUID = new SpellSummonSquid(110, "Summon Squid", "spellSummonSquid", _SUMMON_REQUIRED_WAND, new ItemStack(Items.DYE, _SUMMON_FRIENDLY_COST_ITEMSTACK, 0));
	public static final SpellSummonPassive SPELL_SUMMON_LAMA = new SpellSummonLama(111, "Summon Lama", "spellSummonLama", _SUMMON_REQUIRED_WAND, new ItemStack(Items.LEATHER, _SUMMON_FRIENDLY_COST_ITEMSTACK));
	public static final SpellSummonPassive SPELL_SUMMON_SNOWMAN = new SpellSummonSnowman(112, "Summon Snowman", "spellSummonSnowman", _SUMMON_REQUIRED_WAND, new ItemStack(Blocks.SNOW, 2));
	
	// Hostile Mobs Summon
	public static final SpellSummonHostile SPELL_SUMMON_ZOMBIE = new SpellSummonZombie(200, "Summon Zombie", "spellSummonZombie", _SUMMON_REQUIRED_WAND, new ItemStack(Items.ROTTEN_FLESH, _SUMMON_HOSTILE_COST_ITEMSTACK));
	public static final SpellSummonHostile SPELL_SUMMON_CREEPER = new SpellSummonCreeper(201, "Summon Creeper", "spellSummonCreeper", _SUMMON_REQUIRED_WAND, new ItemStack(Items.GUNPOWDER, _SUMMON_HOSTILE_COST_ITEMSTACK));
	public static final SpellSummonHostile SPELL_SUMMON_SKELETON = new SpellSummonSkeleton(202, "Summon Skeleton", "spellSummonSkeleton", _SUMMON_REQUIRED_WAND, new ItemStack(Items.BONE, _SUMMON_HOSTILE_COST_ITEMSTACK));
	public static final SpellSummonHostile SPELL_SUMMON_BLAZE = new SpellSummonBlaze(203, "Summon Blaze", "spellSummonBlaze", _SUMMON_REQUIRED_WAND, new ItemStack(Items.BLAZE_ROD, _SUMMON_HOSTILE_COST_ITEMSTACK));
	public static final SpellSummonHostile SPELL_SUMMON_MAGMA_CUBE = new SpellSummonMagmaCube(204, "Summon Magma Cube", "spellSummonMagmaCube", _SUMMON_REQUIRED_WAND, new ItemStack(Items.MAGMA_CREAM, _SUMMON_HOSTILE_COST_ITEMSTACK));
	public static final SpellSummonHostile SPELL_SUMMON_GHAST = new SpellSummonGhast(205, "Summon Ghast", "spellSummonGhast", _SUMMON_REQUIRED_WAND, new ItemStack(Items.GHAST_TEAR, _SUMMON_HOSTILE_COST_ITEMSTACK));
	public static final SpellSummonHostile SPELL_SUMMON_ENDERMAN = new SpellSummonEnderman(206, "Summon Enderman", "spellSummonEnderman", _SUMMON_REQUIRED_WAND, new ItemStack(Items.BLAZE_POWDER, _SUMMON_HOSTILE_COST_ITEMSTACK));
	public static final SpellSummonHostile SPELL_SUMMON_SPIDER_JOCKEY = new SpellSummonSpiderJockey(207, "Summon Spider Jockey", "spellSummonSpiderJockey", _SUMMON_REQUIRED_WAND, new ItemStack(Items.SPIDER_EYE, _SUMMON_HOSTILE_COST_ITEMSTACK));
	public static final SpellSummonHostile SPELL_SUMMON_SLIME = new SpellSummonSlime(208, "Summon Slime", "spellSummonSlime", _SUMMON_REQUIRED_WAND, new ItemStack(Items.SLIME_BALL, _SUMMON_HOSTILE_COST_ITEMSTACK));
	public static final SpellSummonHostile SPELL_SUMMON_WITCH = new SpellSummonWitch(209, "Summon Witch", "spellSummonWitch", _SUMMON_REQUIRED_WAND, new ItemStack(Items.GLOWSTONE_DUST, _SUMMON_HOSTILE_COST_ITEMSTACK));
	public static final SpellSummonHostile SPELL_SUMMON_PIGMAN = new SpellSummonPigman(210, "Summon Pigman", "spellSummonPigman", _SUMMON_REQUIRED_WAND, new ItemStack(Items.GOLD_NUGGET, _SUMMON_HOSTILE_COST_ITEMSTACK));
	public static final SpellSummonHostile SPELL_SUMMON_GUARDIAN = new SpellSummonGuardian(211, "Summon Guardian", "spellSummonGuardian", _SUMMON_REQUIRED_WAND, new ItemStack(Items.PRISMARINE_SHARD, _SUMMON_HOSTILE_COST_ITEMSTACK));
	public static final SpellSummonHostile SPELL_SUMMON_POLAR_BEAR = new SpellSummonPoalrBear(212, "Summon Polar Bear", "spellSummonPolarBear", _SUMMON_REQUIRED_WAND, new ItemStack(Items.FISH, _SUMMON_HOSTILE_COST_ITEMSTACK));
	public static final SpellSummonHostile SPELL_SUMMON_SHULKER = new SpellSummonShulker(213, "Summon Shulker", "spellSummonShulker", _SUMMON_REQUIRED_WAND, new ItemStack(Blocks.END_ROD, 1));
	public static final SpellSummonHostile SPELL_SUMMON_SILVERFISH = new SpellSummonSilverfish(214, "Summon Silverfish", "spellSummonSilverfish", _SUMMON_REQUIRED_WAND, new ItemStack(Blocks.STONEBRICK, _SUMMON_HOSTILE_COST_ITEMSTACK));
	public static final SpellSummonHostile SPELL_SUMMON_WITHER = new SpellSummonWither(215, "Summon Wither", "spellSummonWither", _SUMMON_REQUIRED_WAND, new ItemStack(Blocks.EMERALD_BLOCK, 1));
	public static final SpellSummonHostile SPELL_SUMMON_GIANT = new SpellSummonGiant(216, "Summon Giant", "spellSummonGiant", _SUMMON_REQUIRED_WAND, new ItemStack(Items.ARMOR_STAND, _SUMMON_HOSTILE_COST_ITEMSTACK));
	public static final SpellSummonHostile SPELL_SUMMON_SPIDER = new SpellSummonSpider(217, "Summon Spider", "spellSummonSpider", _SUMMON_REQUIRED_WAND, new ItemStack(Items.SPIDER_EYE, _SUMMON_HOSTILE_COST_ITEMSTACK));
	public static final SpellSummonHostile SPELL_SUMMON_ENDERMITE = new SpellSummonEndermite(218, "Summon Endermite", "spellSummonEndermite", _SUMMON_REQUIRED_WAND, new ItemStack(Items.BLAZE_POWDER, 3));
	public static final SpellSummonHostile SPELL_SUMMON_VINDICATOR = new SpellSummonVindicator(219, "Summon Vindicator", "spellSummonVindicator", _SUMMON_REQUIRED_WAND, new ItemStack(Items.IRON_AXE));
	public static final SpellSummonHostile SPELL_SUMMON_VEX = new SpellSummonVex(220, "Summon Vex", "spellSummonVex", _SUMMON_REQUIRED_WAND, new ItemStack(Items.IRON_SWORD));
	public static final SpellSummonHostile SPELL_SUMMON_EVOKER = new SpellSummonEvoker(221, "Summon Evoker", "spellSummonEvoker", _SUMMON_REQUIRED_WAND, new ItemStack(Items.EMERALD, 2));
	
	private SpellRegistry()
	{
	}
	
	public static void preInit()
	{
		VanillaMagic.LOGGER.log(Level.INFO, "Registered spells: " + _SPELLS.size());
		VanillaMagic.LOGGER.log(Level.INFO, "Registered spells (passive entities): " + _SPELLS_PASSIVE.size());
		VanillaMagic.LOGGER.log(Level.INFO, "Registered spells (hostile entities): " + _SPELLS_HOSTILE.size());
	}
	
	public static void addSpell(ISpell spell)
	{
		_SPELLS.add(spell);
	}
	
	public static void addEntityPassive(ISpell spell)
	{
		_SPELLS_PASSIVE.add(spell);
	}
	
	public static void addEntityHostile(ISpell spell)
	{
		_SPELLS_HOSTILE.add(spell);
	}
	
	public static List<ISpell> getSpells()
	{
		return _SPELLS;
	}
	
	public static boolean castSpellById(int spellID, EntityPlayer caster, BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		ISpell spell = getSpellById(spellID);
		if(spell == null)
		{
			return false;
		}
		boolean casted = spell.castSpell(caster, pos, face, hitVec);
		return casted;
	}
	
	@Nullable
	public static ISpell getSpellById(int id)
	{
		for(int i = 0; i < _SPELLS.size(); ++i)
		{
			ISpell spell = _SPELLS.get(i);
			if(spell.getSpellID() == id)
			{
				return spell;
			}
		}
		return null;
	}
	
	public static int[] getSummonMobSpellIDs()
	{
		int max = SPELL_SUMMON_SPIDER.getSpellID();
		int min = SPELL_SUMMON_ZOMBIE.getSpellID();
		int[] tab = new int[max - min + 1];
		int index = 0;
		for(int i = min; i <= max; ++i)
		{
			tab[index] = i;
			index++;
		}
		return tab;
	}
	
	public static int[] getSummonMobSpellIDsWithoutSpecific(int summonSpellID)
	{
		int[] all = getSummonMobSpellIDs();
		int[] without = new int[all.length - 1];
		int index = 0;
		for(int i = 0; i < all.length; ++i)
		{
			if(all[i] != summonSpellID)
			{
				without[index] = all[i];
				index++;
			}
		}
		return without;
	}

	public static void castSummonMob(EntityPlayer player, World world, BlockPos spawnPos, EnumFacing up, int randID, boolean b) 
	{
		ISpell spell = _SPELLS_HOSTILE.get(randID);
		spell.castSpell(player, spawnPos, up, null);
	}
}