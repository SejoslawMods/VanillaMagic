package com.github.sejoslaw.vanillamagic2.common.files;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@Mod.EventBusSubscriber
public final class VMForgeConfig {
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;

    // Console
    private static final String CATEGORY_CONSOLE = "Console";
    public static ForgeConfigSpec.BooleanValue SHOW_VM_TILE_ENTITY_SAVING;

    // Player
    private static final String CATEGORY_PLAYER = "Player";
    public static ForgeConfigSpec.BooleanValue GIVE_PLAYER_VM_BOOKS_ON_LOGIN;

    // Machine
    private static final String CATEGORY_MACHINE = "Machine";
    public static ForgeConfigSpec.IntValue TILE_ACCELERANT_TICKS;
    public static ForgeConfigSpec.IntValue TILE_ACCELERANT_SIZE;
    public static ForgeConfigSpec.DoubleValue TILE_MACHINE_ONE_OPERATION_COST;
    public static ForgeConfigSpec.IntValue TILE_ABSORBER_PULLING_SPEED;
    public static ForgeConfigSpec.IntValue BASIC_QUARRY_SIZE;

    // Item
    private static final String CATEGORY_ITEM = "Item";
    public static ForgeConfigSpec.IntValue ACCELERATION_CRYSTAL_UPDATE_TICKS;
    public static ForgeConfigSpec.IntValue LIQUID_SUPPRESSION_CRYSTAL_RADIUS;
    public static ForgeConfigSpec.IntValue LIQUID_SUPPRESSION_CRYSTAL_REFRESH_RATE;
    public static ForgeConfigSpec.IntValue MOTHER_NATURE_CRYSTAL_RANGE;
    public static ForgeConfigSpec.IntValue ITEM_MAGNET_RANGE;
    public static ForgeConfigSpec.BooleanValue ITEM_CAN_AUTOPLANT;

    // Spell
    private static final String CATEGORY_SPELL = "Spell";
    public static ForgeConfigSpec.IntValue SPELL_COST_SUMMON_FRIENDLY;
    public static ForgeConfigSpec.IntValue SPELL_COST_SUMMON_HOSTILE;
    public static ForgeConfigSpec.IntValue SPELL_FUS_RO_DAH_DISTANCE;
    public static ForgeConfigSpec.DoubleValue SPELL_FUS_RO_DAH_STRENGTH;
    public static ForgeConfigSpec.DoubleValue SPELL_MOVE_IN_AIR_DISTANCE;

    // Meteor
    private static final String CATEGORY_METEOR = "Meteor";
    public static ForgeConfigSpec.DoubleValue BASIC_METEOR_SIZE;
    public static ForgeConfigSpec.IntValue BASIC_METEOR_EXPLOSION_POWER;
    public static ForgeConfigSpec.DoubleValue BASIC_METEOR_EXPLOSION_DROP_RATE;

    // Hostile Mobs
    private static final String CATEGORY_HOSTILE_MOBS = "HostileMobs";
    public static ForgeConfigSpec.IntValue PERCENT_FOR_SPAWN_HOSTILE_WITH_ARMOR;
    public static ForgeConfigSpec.IntValue PERCENT_FOR_SPAWN_HOSTILE_ON_HORSE;

    // Evoker Crystal
    private static final String CATEGORY_EVOKER_CRYSTAL = "EvokerCrystal";
    public static ForgeConfigSpec.IntValue VEX_NUMBER;
    public static ForgeConfigSpec.BooleanValue VEX_HAS_LIMITED_LIFE;

    // Integration
    private static final String CATEGORY_INTEGRATION = "Integration";
    public static ForgeConfigSpec.BooleanValue SHOW_ADDITIONAL_TOOLTIPS;
    public static ForgeConfigSpec.BooleanValue SHOW_LAST_DEATH_POINT;

    static {
        // Console
        COMMON_BUILDER.comment("Options connected with Console.").push(CATEGORY_CONSOLE);
        SHOW_VM_TILE_ENTITY_SAVING = COMMON_BUILDER
                .comment("Should console show when World save / load VMTileEntity (console spam).")
                .define("SHOW_VM_TILE_ENTITY_SAVING", true);
        COMMON_BUILDER.pop();

        // Player
        COMMON_BUILDER.comment("Options connected with Player").push(CATEGORY_PLAYER);
        GIVE_PLAYER_VM_BOOKS_ON_LOGIN = COMMON_BUILDER
                .comment("Should Player get Vanilla Magic VM books on spawn (logged in).")
                .define("GIVE_PLAYER_VM_BOOKS_ON_LOGIN", true);
        COMMON_BUILDER.pop();

        // Machine
        COMMON_BUILDER.comment("Options connected with Vanilla Magic Machines.").push(CATEGORY_MACHINE);
        TILE_ACCELERANT_TICKS = COMMON_BUILDER
                .comment("The number of ticks in 1 Minecraft tick that Speedy can do to a single block.")
                .defineInRange("TILE_ACCELERANT_TICKS", 1000, 1, Integer.MAX_VALUE);
        TILE_ACCELERANT_SIZE = COMMON_BUILDER
                .comment("Size of the Speedy - Area on which Speedy can operate.")
                .defineInRange("TILE_ACCELERANT_SIZE", 4, 1, Integer.MAX_VALUE);
        TILE_MACHINE_ONE_OPERATION_COST = COMMON_BUILDER
                .comment("Cost of a single Machine operation. (in ticks) (i.e.: for Furnace recipes it's 800)")
                .defineInRange("TILE_MACHINE_ONE_OPERATION_COST", 100, 1, Double.MAX_VALUE);
        TILE_ABSORBER_PULLING_SPEED = COMMON_BUILDER
                .comment("Speed (in ticks) how fast the Block Absorber should take items from connected inventory.")
                .defineInRange("TILE_ABSORBER_PULLING_SPEED", 100, 1, Integer.MAX_VALUE);
        BASIC_QUARRY_SIZE = COMMON_BUILDER
                .comment("Number of blocks per each diamond block in Quarry.")
                .defineInRange("BASIC_QUARRY_SIZE", 16, 1, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        // Item
        COMMON_BUILDER.comment("Options connected with Vanilla Magic Items.").push(CATEGORY_ITEM);
        ACCELERATION_CRYSTAL_UPDATE_TICKS = COMMON_BUILDER
                .comment("The number of ticks that should be ticked when Player holds Acceleration Crystal.")
                .defineInRange("ACCELERATION_CRYSTAL_UPDATE_TICKS", 100, 1, Integer.MAX_VALUE);
        LIQUID_SUPPRESSION_CRYSTAL_RADIUS = COMMON_BUILDER
                .comment("Radius on which Liquid Suppression Crystal works (in blocks).")
                .defineInRange("LIQUID_SUPPRESSION_CRYSTAL_RADIUS", 5, 1, Integer.MAX_VALUE);
        LIQUID_SUPPRESSION_CRYSTAL_REFRESH_RATE = COMMON_BUILDER
                .comment("Defines how often the suppression blocks should be refreshed.")
                .defineInRange("LIQUID_SUPPRESSION_CRYSTAL_REFRESH_RATE", 100, 1, Integer.MAX_VALUE);
        MOTHER_NATURE_CRYSTAL_RANGE = COMMON_BUILDER
                .comment("Range on which Mother Nature Crystal will work.")
                .defineInRange("MOTHER_NATURE_CRYSTAL_RANGE", 10, 1, Integer.MAX_VALUE);
        ITEM_MAGNET_RANGE = COMMON_BUILDER
                .comment("Range on which Item Magnet will work.")
                .defineInRange("ITEM_MAGNET_RANGE", 6, 1, Integer.MAX_VALUE);
        ITEM_CAN_AUTOPLANT = COMMON_BUILDER
                .comment("If autoplanting items like Sapling, etc should be enabled.")
                .define("ITEM_CAN_AUTOPLANT", true);
        COMMON_BUILDER.pop();

        // Spell
        COMMON_BUILDER.comment("Options connected with Vanilla Magic Spells.").push(CATEGORY_SPELL);
        SPELL_COST_SUMMON_FRIENDLY = COMMON_BUILDER
                .comment("Cost of one friendly mob spawn (in items from hand).")
                .defineInRange("SPELL_COST_SUMMON_FRIENDLY", 8, 1, 64);
        SPELL_COST_SUMMON_HOSTILE = COMMON_BUILDER
                .comment("Cost of one hostile mob spawn (in items from hand).")
                .defineInRange("SPELL_COST_SUMMON_HOSTILE", 8, 1, 64);
        SPELL_FUS_RO_DAH_DISTANCE = COMMON_BUILDER
                .comment("Area of effect of Fus-Ro-Dah spell.")
                .defineInRange("SPELL_FUS_RO_DAH_DISTANCE", 8, 1, Integer.MAX_VALUE);
        SPELL_FUS_RO_DAH_STRENGTH = COMMON_BUILDER
                .comment("Strength of the Fus-Ro-Dah spell.")
                .defineInRange("SPELL_FUS_RO_DAH_STRENGTH", 2, 1, Double.MAX_VALUE);
        SPELL_MOVE_IN_AIR_DISTANCE = COMMON_BUILDER
                .comment("Number of blocks by which the Player will move in air.")
                .defineInRange("SPELL_MOVE_IN_AIR_DISTANCE", 10, 1, Double.MAX_VALUE);
        COMMON_BUILDER.pop();

        // Meteor
        COMMON_BUILDER.comment("Options connected with Meteor Explosion.").push(CATEGORY_METEOR);
        BASIC_METEOR_SIZE = COMMON_BUILDER
                .comment("Basic size of the Meteor.")
                .defineInRange("BASIC_METEOR_SIZE", 5, 1, Double.MAX_VALUE);
        BASIC_METEOR_EXPLOSION_POWER = COMMON_BUILDER
                .comment("Meteor explosion power.")
                .defineInRange("BASIC_METEOR_EXPLOSION_POWER", 25, 1, Integer.MAX_VALUE);
        BASIC_METEOR_EXPLOSION_DROP_RATE = COMMON_BUILDER
                .comment("Rate between 0-1 for block drops from explosion.")
                .defineInRange("BASIC_METEOR_EXPLOSION_DROP_RATE", 0.1D, 0.0D, 1.0D);
        COMMON_BUILDER.pop();

        // Hostile Mobs
        COMMON_BUILDER.comment("Options connected with Hostile Mobs Spawning with Spell.").push(CATEGORY_HOSTILE_MOBS);
        PERCENT_FOR_SPAWN_HOSTILE_WITH_ARMOR = COMMON_BUILDER
                .comment("Percent with which there is a chance for spawning a Mob with Armor (if possible for Armor).")
                .defineInRange("PERCENT_FOR_SPAWN_HOSTILE_WITH_ARMOR", 10, 1, 100);
        PERCENT_FOR_SPAWN_HOSTILE_ON_HORSE = COMMON_BUILDER
                .comment("Percent with which there is a chance for spawning a Mob on Horse (if Mob has equal Horse).")
                .defineInRange("PERCENT_FOR_SPAWN_HOSTILE_ON_HORSE", 15, 1, 100);
        COMMON_BUILDER.pop();

        // Evoker Crystal
        COMMON_BUILDER.comment("Options connected with Evoker Crystal.").push(CATEGORY_EVOKER_CRYSTAL);
        VEX_NUMBER = COMMON_BUILDER
                .comment("The number of Vex's that can be spawn using Evoker Crystal.")
                .defineInRange("VEX_NUMBER", 3, 1, Integer.MAX_VALUE);
        VEX_HAS_LIMITED_LIFE = COMMON_BUILDER
                .comment("Should spawned Vex have limited life.")
                .define("VEX_HAS_LIMITED_LIFE", true);
        COMMON_BUILDER.pop();

        // Integration
        COMMON_BUILDER.comment("Options connected with Integrations with VM").push(CATEGORY_INTEGRATION);
        SHOW_ADDITIONAL_TOOLTIPS = COMMON_BUILDER
                .comment("Additional tooltips (weapons durability, food saturation, etc.)")
                .define("SHOW_ADDITIONAL_TOOLTIPS", true);
        SHOW_LAST_DEATH_POINT = COMMON_BUILDER
                .comment("Death info (last death position)")
                .define("SHOW_LAST_DEATH_POINT", true);
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
