package seia.vanillamagic.utils.spell;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public enum EnumSpell
{
	LIGHTER(0, "Flint and Steel Clone", "spellFlintAndSteel", 
			EnumWand.STICK, 
			new ItemStack(Items.COAL)),
	SMALL_FIREBALL(1, "Feel like Blaze", "spellSmallFireball", 
			EnumWand.BLAZE_ROD, 
			new ItemStack(Items.REDSTONE, 2)),
	LARGE_FIREBALL(2, "Feel like Ghast", "spellLargeFireball", 
			EnumWand.BLAZE_ROD, 
			new ItemStack(Items.GHAST_TEAR)),
	TELEPORT(3, "Teleportation !!!", "spellTeleport", 
			EnumWand.BLAZE_ROD, 
			new ItemStack(Items.MAGMA_CREAM)),
	METEOR(4, "Meteor !!!", "spellSummonMeteor", 
			EnumWand.NETHER_STAR, 
			new ItemStack(Blocks.GOLD_BLOCK)),
	LIGHTNING_BOLT(5, "Thunder !!!", "spellSummonLightningBolt", 
			EnumWand.BLAZE_ROD, 
			new ItemStack(Items.GUNPOWDER, 32)),
	FUS_RO_DAH(6, "Fus-Ro-Dah !!!", "spellFusRoDah",
			EnumWand.BLAZE_ROD,
			new ItemStack(Items.DRAGON_BREATH)),
	TELEPORT_TO_NETHER(7, "Teleport to Nether", "spellTeleportToNether",
			EnumWand.BLAZE_ROD,
			new ItemStack(Items.NETHER_WART, 2)),
	TELEPORT_TO_END(8, "Teleport to End", "spellTeleportToEnd",
			EnumWand.BLAZE_ROD,
			new ItemStack(Items.END_CRYSTAL)),
	MOVE_IN_AIR(9, "Move in air", "spellMoveInAir",
			EnumWand.BLAZE_ROD,
			new ItemStack(Items.FEATHER)),
	
	// EntityAnimal + EntityAgeable + EntityPassive Summons
	SUMMON_CHICKEN(100, "Summon Chicken", "spellSummonChicken", 
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.CHICKEN, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
	SUMMON_PIG(101, "Summon Pig", "spellSummonPig", 
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.PORKCHOP, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
	SUMMON_COW(102, "Summon Cow", "spellSummonCow", 
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.BEEF, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
	SUMMON_MOOSHROOM(103, "Summon Mooshroom", "spellSummonMooshroom", 
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.COOKED_BEEF, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
	SUMMON_SHEEP(104, "Summon Sheep", "spellSummonSheep",
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.MUTTON, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
	SUMMON_WOLF(105, "Summon Wolf", "spellSummonWolf",
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.COOKED_MUTTON, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
	SUMMON_RABBIT(106, "Summon Rabbit", "spellSummonRabbit",
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.RABBIT, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
	SUMMON_HORSE(107, "Summon Horse", "spellSummonHorse",
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.SADDLE)),
	SUMMON_VILLAGER(108, "Summon Villager", "spellSummonVillager",
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.BOOK, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
	
	// EntityMob + Monsters Summon
	SUMMON_ZOMBIE(200, "Summon Zombie", "spellSummonZombie",
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.ROTTEN_FLESH, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
	SUMMON_CREEPER(201, "Summon Creeper", "spellSummonCreeper",
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.GUNPOWDER, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
	SUMMON_SKELETON(202, "Summon Skeleton", "spellSummonSkeleton",
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.BONE, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
	SUMMON_BLAZE(203, "Summon Blaze", "spellSummonBlaze",
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.BLAZE_ROD, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
	SUMMON_MAGMA_CUBE(204, "Summon Magma Cube", "spellSummonMagmaCube",
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.MAGMA_CREAM, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
	SUMMON_GHAST(205, "Summon Ghast", "spellSummonGhast",
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.GHAST_TEAR, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
	SUMMON_ENDERMAN(206, "Summon Enderman", "spellSummonEnderman",
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.BLAZE_POWDER, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
	SUMMON_SPIDER(207, "Summon Spider", "spellSummonSpider",
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.STRING, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
	SUMMON_SLIME(208, "Summon Slime", "spellSummonSlime",
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.SLIME_BALL, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
	SUMMON_WITCH(209, "Summon With", "spellSummonWitch",
			EnumSpellHelper.SUMMON_REQUIRED_WAND, 
			new ItemStack(Items.GLOWSTONE_DUST, EnumSpellHelper.SUMMON_COST_ITEMSTACK)),
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
				(stackOffHand.stackSize >= itemOffHand.stackSize);
	}

	//================================================================================================
	
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
		if((100 <= spellID) && (spellID <= 108)) // TODO: Change if added an Passive Mob e.g. Pig, Cow, etc.
		{
			return true;
		}
		return false;
	}
	
	public static boolean isSpellSummonMob(int spellID)
	{
		if((200 <= spellID) && (spellID <= 209)) // TODO: Change if added a Monster e.g. Skeleton, Creeper
		{
			return true;
		}
		return false;
	}
}