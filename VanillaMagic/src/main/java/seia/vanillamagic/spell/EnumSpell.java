package seia.vanillamagic.spell;

import javax.annotation.Nullable;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.util.ItemStackHelper;

public enum EnumSpell
{
	LIGHTER(0, "Flint and Steel Clone", "spellFlintAndSteel", EnumWand.STICK, new ItemStack(Items.COAL)),
	SMALL_FIREBALL(1, "Feel like Blaze", "spellSmallFireball", EnumWand.BLAZE_ROD, new ItemStack(Items.REDSTONE, 2)),
	LARGE_FIREBALL(2, "Feel like Ghast", "spellLargeFireball", EnumWand.BLAZE_ROD, new ItemStack(Items.GHAST_TEAR)),
	TELEPORT(3, "Teleportation !!!", "spellTeleport", EnumWand.BLAZE_ROD, new ItemStack(Items.MAGMA_CREAM)),
	METEOR(4, "Meteor !!!", "spellSummonMeteor", EnumWand.NETHER_STAR, new ItemStack(Blocks.GOLD_BLOCK)),
	LIGHTNING_BOLT(5, "Thunder !!!", "spellSummonLightningBolt", EnumWand.BLAZE_ROD, new ItemStack(Items.GUNPOWDER, 32)),
	FUS_RO_DAH(6, "Fus-Ro-Dah !!!", "spellFusRoDah",EnumWand.BLAZE_ROD, new ItemStack(Items.DRAGON_BREATH)),
	TELEPORT_TO_NETHER(7, "Teleport to Nether", "spellTeleportToNether", EnumWand.BLAZE_ROD, new ItemStack(Items.NETHER_WART, 2)),
	TELEPORT_TO_END(8, "Teleport to End", "spellTeleportToEnd", EnumWand.BLAZE_ROD, new ItemStack(Items.END_CRYSTAL)),
	MOVE_IN_AIR(9, "Move in air", "spellMoveInAir", EnumWand.BLAZE_ROD, new ItemStack(Items.FEATHER)),
	PULL_ENTITY_TO_PLAYER(10, "Pull Entity to Player", "spellPullEntityToPlayer", EnumWand.BLAZE_ROD, new ItemStack(Items.STRING, 4)),
	WATER_FREEZE(11, "Freeze Water 3x3", "spellFreezeWater3x3", EnumWand.BLAZE_ROD, new ItemStack(Blocks.SNOW)),
	WEATHER_RAIN(12, "Make rain", "spellWeatherRain", EnumWand.BLAZE_ROD, new ItemStack(Items.POTIONITEM)),
	WEATHER_CLEAR(13, "Clear weather", "spellWeatherClear", EnumWand.BLAZE_ROD, new ItemStack(Items.GLASS_BOTTLE, 1)),
	WEATHER_THUNDERSTORM(14, "Make thunderstorm", "spellWeatherThunderstorm", EnumWand.BLAZE_ROD, ItemStackHelper.getHead(1, 4)),
	
	// EntityAnimal + EntityAgeable + EntityPassive Summons
	SUMMON_CHICKEN(100, "Summon Chicken", "spellSummonChicken", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.CHICKEN, EnumSpellHelper.SUMMON_FRIENDLY_COST_ITEMSTACK)),
	SUMMON_PIG(101, "Summon Pig", "spellSummonPig", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.PORKCHOP, EnumSpellHelper.SUMMON_FRIENDLY_COST_ITEMSTACK)),
	SUMMON_COW(102, "Summon Cow", "spellSummonCow", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.BEEF, EnumSpellHelper.SUMMON_FRIENDLY_COST_ITEMSTACK)),
	SUMMON_MOOSHROOM(103, "Summon Mooshroom", "spellSummonMooshroom", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.COOKED_BEEF, EnumSpellHelper.SUMMON_FRIENDLY_COST_ITEMSTACK)),
	SUMMON_SHEEP(104, "Summon Sheep", "spellSummonSheep", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.MUTTON, EnumSpellHelper.SUMMON_FRIENDLY_COST_ITEMSTACK)),
	SUMMON_WOLF(105, "Summon Wolf", "spellSummonWolf", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.COOKED_MUTTON, EnumSpellHelper.SUMMON_FRIENDLY_COST_ITEMSTACK)),
	SUMMON_RABBIT(106, "Summon Rabbit", "spellSummonRabbit", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.RABBIT, EnumSpellHelper.SUMMON_FRIENDLY_COST_ITEMSTACK)),
	SUMMON_HORSE(107, "Summon Horse", "spellSummonHorse", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.SADDLE)),
	SUMMON_VILLAGER(108, "Summon Villager", "spellSummonVillager", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.BOOK, EnumSpellHelper.SUMMON_FRIENDLY_COST_ITEMSTACK)),
	
	// EntityMob + Monsters Summon
	SUMMON_ZOMBIE(200, "Summon Zombie", "spellSummonZombie", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.ROTTEN_FLESH, EnumSpellHelper.SUMMON_HOSTILE_COST_ITEMSTACK)),
	SUMMON_CREEPER(201, "Summon Creeper", "spellSummonCreeper", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.GUNPOWDER, EnumSpellHelper.SUMMON_HOSTILE_COST_ITEMSTACK)),
	SUMMON_SKELETON(202, "Summon Skeleton", "spellSummonSkeleton", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.BONE, EnumSpellHelper.SUMMON_HOSTILE_COST_ITEMSTACK)),
	SUMMON_BLAZE(203, "Summon Blaze", "spellSummonBlaze", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.BLAZE_ROD, EnumSpellHelper.SUMMON_HOSTILE_COST_ITEMSTACK)),
	SUMMON_MAGMA_CUBE(204, "Summon Magma Cube", "spellSummonMagmaCube", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.MAGMA_CREAM, EnumSpellHelper.SUMMON_HOSTILE_COST_ITEMSTACK)),
	SUMMON_GHAST(205, "Summon Ghast", "spellSummonGhast", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.GHAST_TEAR, EnumSpellHelper.SUMMON_HOSTILE_COST_ITEMSTACK)),
	SUMMON_ENDERMAN(206, "Summon Enderman", "spellSummonEnderman", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.BLAZE_POWDER, EnumSpellHelper.SUMMON_HOSTILE_COST_ITEMSTACK)),
	SUMMON_SPIDER(207, "Summon Spider", "spellSummonSpider", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.SPIDER_EYE, EnumSpellHelper.SUMMON_HOSTILE_COST_ITEMSTACK)),
	SUMMON_SLIME(208, "Summon Slime", "spellSummonSlime", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.SLIME_BALL, EnumSpellHelper.SUMMON_HOSTILE_COST_ITEMSTACK)),
	SUMMON_WITCH(209, "Summon Witch", "spellSummonWitch", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.GLOWSTONE_DUST, EnumSpellHelper.SUMMON_HOSTILE_COST_ITEMSTACK)),
	SUMMON_PIGMAN(210, "Summon Pigman", "spellSummonPigman",  EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.GOLD_NUGGET, EnumSpellHelper.SUMMON_HOSTILE_COST_ITEMSTACK)),
	SUMMON_GUARDIAN(211, "Summon Guardian", "spellSummonGuardian",  EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.PRISMARINE_SHARD, EnumSpellHelper.SUMMON_HOSTILE_COST_ITEMSTACK)),
	SUMMON_POLAR_BEAR(212, "Summon Polar Bear", "spellSummonPolarBear",  EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.FISH, EnumSpellHelper.SUMMON_HOSTILE_COST_ITEMSTACK)),
	SUMMON_SHULKER(213, "Summon Shulker", "spellSummonShulker",  EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Blocks.END_ROD, 1)),
	SUMMON_SILVERFISH(214, "Summon Silverfish", "spellSummonSilverfish",  EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Blocks.STONEBRICK, EnumSpellHelper.SUMMON_HOSTILE_COST_ITEMSTACK)),
	SUMMON_WITHER(215, "Summon Wither", "spellSummonWither",  EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Blocks.EMERALD_BLOCK, 1)),
	SUMMON_GIANT(216, "Summon Giant", "spellSummonGiant", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.ARMOR_STAND, EnumSpellHelper.SUMMON_HOSTILE_COST_ITEMSTACK)),
	SUMMON_SPIDER_JOCKEY(217, "Summon Spider Jockey", "spellSummonSpiderJockey", EnumSpellHelper.SUMMON_REQUIRED_WAND, new ItemStack(Items.SPIDER_EYE, EnumSpellHelper.SUMMON_HOSTILE_COST_ITEMSTACK))
	;

	public final int spellID;
	public final String spellName;
	public final String spellUniqueName;
	public final EnumWand minimalWandTier;
	public final ItemStack itemOffHand;
	
	EnumSpell(int spellID, String spellName, String spellUniqueName, EnumWand minimalWandTier, ItemStack itemOffHand)
	{
		this.spellID = spellID;
		this.spellName = spellName;
		this.spellUniqueName = spellUniqueName;
		this.minimalWandTier = minimalWandTier;
		this.itemOffHand = itemOffHand;
	}
	
	public boolean isItemOffHandRightForSpell(ItemStack stackOffHand)
	{
		return (itemOffHand.getItem().equals(stackOffHand.getItem())) && 
				(itemOffHand.getMetadata() == stackOffHand.getMetadata()) &&
				//(itemOffHand.stackSize <= stackOffHand.stackSize);
				(ItemStackHelper.getStackSize(itemOffHand) <= ItemStackHelper.getStackSize(stackOffHand));
	}

	//================================================================================================
	
	@Nullable
	public static EnumSpell getSpellById(int id)
	{
		EnumSpell[] spells = values();
		for(int i = 0; i < spells.length; i++)
		{
			if(spells[i].spellID == id)
			{
				return spells[i];
			}
		}
		return null;
	}
	
	public static boolean isSpellSummonAnimal(int spellID)
	{
		if((100 <= spellID) && (spellID < 200))
		{
			return true;
		}
		return false;
	}
	
	public static boolean isSpellSummonMob(int spellID)
	{
		if((200 <= spellID) && (spellID < 300))
		{
			return true;
		}
		return false;
	}
	
	public static int[] getSummonMobSpellIDs()
	{
		int max = SUMMON_SPIDER_JOCKEY.spellID;
		int min = SUMMON_ZOMBIE.spellID;
		int[] tab = new int[max - min + 1];
		int index = 0;
		for(int i = min; i <= max; i++)
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
		for(int i = 0; i < all.length; i++)
		{
			if(all[i] != summonSpellID)
			{
				without[index] = all[i];
				index++;
			}
		}
		return without;
	}
}