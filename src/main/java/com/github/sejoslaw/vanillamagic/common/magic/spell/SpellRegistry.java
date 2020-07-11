package com.github.sejoslaw.vanillamagic.common.magic.spell;

import com.github.sejoslaw.vanillamagic.api.magic.ISpell;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.core.VMConfig;
import com.github.sejoslaw.vanillamagic.common.magic.spell.spells.*;
import com.github.sejoslaw.vanillamagic.common.magic.spell.spells.summon.hostile.*;
import com.github.sejoslaw.vanillamagic.common.magic.spell.spells.summon.passive.*;
import com.github.sejoslaw.vanillamagic.common.magic.spell.spells.weather.SpellWeatherClear;
import com.github.sejoslaw.vanillamagic.common.magic.spell.spells.weather.SpellWeatherRain;
import com.github.sejoslaw.vanillamagic.common.magic.spell.spells.weather.SpellWeatherThunderStorm;
import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry which store data about all registered Spells.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class SpellRegistry {
    public static final List<ISpell> SPELLS = new ArrayList<>();
    public static final List<ISpell> SPELLS_PASSIVE = new ArrayList<>();
    public static final List<ISpell> SPELLS_HOSTILE = new ArrayList<>();

    private static final IWand _SUMMON_REQUIRED_WAND = WandRegistry.WAND_NETHER_STAR;
    private static final int _SUMMON_FRIENDLY_COST_ITEMSTACK = VMConfig.SPELL_COST_SUMMON_FRIENDLY.get();
    private static final int _SUMMON_HOSTILE_COST_ITEMSTACK = VMConfig.SPELL_COST_SUMMON_HOSTILE.get();

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
    public static final Spell SPELL_WEATHER_RAIN = new SpellWeatherRain(12, "Make rain", "spellWeatherRain", WandRegistry.WAND_BLAZE_ROD, new ItemStack(Items.POTION));
    public static final Spell SPELL_WEATHER_CLEAR = new SpellWeatherClear(13, "Clear weather", "spellWeatherClear", WandRegistry.WAND_BLAZE_ROD, new ItemStack(Items.GLASS_BOTTLE, 1));
    public static final Spell SPELL_WEATHER_THUNDERSTORM = new SpellWeatherThunderStorm(14, "Make thunderstorm", "spellWeatherThunderstorm", WandRegistry.WAND_BLAZE_ROD, new ItemStack(Items.CREEPER_HEAD));

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
    public static final SpellSummonPassive SPELL_SUMMON_SQUID = new SpellSummonSquid(110, "Summon Squid", "spellSummonSquid", _SUMMON_REQUIRED_WAND, new ItemStack(Items.BLACK_DYE, _SUMMON_FRIENDLY_COST_ITEMSTACK));
    public static final SpellSummonPassive SPELL_SUMMON_LAMA = new SpellSummonLama(111, "Summon Lama", "spellSummonLama", _SUMMON_REQUIRED_WAND, new ItemStack(Items.LEATHER, _SUMMON_FRIENDLY_COST_ITEMSTACK));
    public static final SpellSummonPassive SPELL_SUMMON_SNOWMAN = new SpellSummonSnowman(112, "Summon Snowman", "spellSummonSnowman", _SUMMON_REQUIRED_WAND, new ItemStack(Blocks.SNOW, 2));

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
    public static final SpellSummonHostile SPELL_SUMMON_POLAR_BEAR = new SpellSummonPolarBear(212, "Summon Polar Bear", "spellSummonPolarBear", _SUMMON_REQUIRED_WAND, new ItemStack(Items.PUFFERFISH, _SUMMON_HOSTILE_COST_ITEMSTACK));
    public static final SpellSummonHostile SPELL_SUMMON_SHULKER = new SpellSummonShulker(213, "Summon Shulker", "spellSummonShulker", _SUMMON_REQUIRED_WAND, new ItemStack(Blocks.END_ROD, 1));
    public static final SpellSummonHostile SPELL_SUMMON_SILVERFISH = new SpellSummonSilverfish(214, "Summon Silverfish", "spellSummonSilverfish", _SUMMON_REQUIRED_WAND, new ItemStack(Blocks.STONE_BRICKS, _SUMMON_HOSTILE_COST_ITEMSTACK));
    public static final SpellSummonHostile SPELL_SUMMON_WITHER = new SpellSummonWither(215, "Summon Wither", "spellSummonWither", _SUMMON_REQUIRED_WAND, new ItemStack(Blocks.EMERALD_BLOCK, 1));
    public static final SpellSummonHostile SPELL_SUMMON_GIANT = new SpellSummonGiant(216, "Summon Giant", "spellSummonGiant", _SUMMON_REQUIRED_WAND, new ItemStack(Items.ARMOR_STAND, _SUMMON_HOSTILE_COST_ITEMSTACK));
    public static final SpellSummonHostile SPELL_SUMMON_SPIDER = new SpellSummonSpider(217, "Summon Spider", "spellSummonSpider", _SUMMON_REQUIRED_WAND, new ItemStack(Items.SPIDER_EYE, _SUMMON_HOSTILE_COST_ITEMSTACK));
    public static final SpellSummonHostile SPELL_SUMMON_ENDERMITE = new SpellSummonEndermite(218, "Summon Endermite", "spellSummonEndermite", _SUMMON_REQUIRED_WAND, new ItemStack(Items.BLAZE_POWDER, 3));
    public static final SpellSummonHostile SPELL_SUMMON_VINDICATOR = new SpellSummonVindicator(219, "Summon Vindicator", "spellSummonVindicator", _SUMMON_REQUIRED_WAND, new ItemStack(Items.IRON_AXE));
    public static final SpellSummonHostile SPELL_SUMMON_VEX = new SpellSummonVex(220, "Summon Vex", "spellSummonVex", _SUMMON_REQUIRED_WAND, new ItemStack(Items.IRON_SWORD));
    public static final SpellSummonHostile SPELL_SUMMON_EVOKER = new SpellSummonEvoker(221, "Summon Evoker", "spellSummonEvoker", _SUMMON_REQUIRED_WAND, new ItemStack(Items.EMERALD, 2));

    private SpellRegistry() {
    }

    /**
     * Register new Spell. For Mobs use {@link #addEntityHostile(ISpell)} or
     * {@link #addEntityPassive(ISpell)}.
     */
    public static void addSpell(ISpell spell) {
        SPELLS.add(spell);
    }

    /**
     * Register new EntityPassive Spell.
     */
    public static void addEntityPassive(ISpell spell) {
        SPELLS_PASSIVE.add(spell);
    }

    /**
     * Register new EntityHostile Spell.
     */
    public static void addEntityHostile(ISpell spell) {
        SPELLS_HOSTILE.add(spell);
    }

    /**
     * Cast Spell by the given ID.
     */
    public static boolean castSpellById(int spellID, PlayerEntity caster, BlockPos pos, Direction face, Vec3d hitVec) {
        ISpell spell = getSpellById(spellID);

        if (spell == null) {
            return false;
        }

        return spell.castSpell(caster, pos, face, hitVec);
    }

    /**
     * @return Returns the Spell by it ID.
     */
    public static ISpell getSpellById(int id) {
        for (ISpell spell : SPELLS) {
            if (spell.getSpellID() == id) {
                return spell;
            }
        }

        return null;
    }

    /**
     * @return Returns the array with Spell IDs of all registered Hostile Mobs.
     */
    public static int[] getSummonMobSpellIDs() {
        int max = SPELL_SUMMON_SPIDER.getSpellID();
        int min = SPELL_SUMMON_ZOMBIE.getSpellID();
        int[] tab = new int[max - min + 1];
        int index = 0;

        for (int i = min; i <= max; ++i) {
            tab[index] = i;
            index++;
        }

        return tab;
    }

    /**
     * @return Returns the array with all registered Mobs without the specified ID.
     */
    public static int[] getSummonMobSpellIDsWithoutSpecific(int summonSpellID) {
        int[] all = getSummonMobSpellIDs();
        int[] without = new int[all.length - 1];
        int index = 0;

        for (int value : all) {
            if (value != summonSpellID) {
                without[index] = value;
                index++;
            }
        }

        return without;
    }

    /**
     * Cast a single Spell Summon Mob.
     */
    public static void castSummonMob(PlayerEntity player, World world, BlockPos spawnPos, Direction up, int spellID, boolean b) {
        ISpell spell = SPELLS_HOSTILE.get(spellID);
        spell.castSpell(player, spawnPos, up, null);
    }
}
