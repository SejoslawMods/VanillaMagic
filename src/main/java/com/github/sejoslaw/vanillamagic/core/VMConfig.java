package com.github.sejoslaw.vanillamagic.core;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

/**
 * VM Forge Configuration File.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@Mod.EventBusSubscriber
public class VMConfig {
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;

    // Console
    private static final String CATEGORY_CONSOLE = "Console";
    public static ForgeConfigSpec.BooleanValue SHOW_CUSTOM_TILE_ENTITY_SAVING;

    // Player
    private static final String CATEGORY_PLAYER = "Player";
    public static ForgeConfigSpec.BooleanValue GIVE_PLAYER_CUSTOM_BOOKS_ON_LOGIN;

    // Machine
    private static final String CATEGORY_MACHINE = "Machine";
    public static ForgeConfigSpec.IntValue TILE_SPEEDY_TICKS;// = 1000;
    public static ForgeConfigSpec.IntValue TILE_SPEEDY_SIZE;// = 4;
    public static ForgeConfigSpec.IntValue TILE_MACHINE_ONE_OPERATION_COST;// = 100;
    public static ForgeConfigSpec.IntValue TILE_MACHINE_MAX_TICKS;// = 4000;

    // Item
    private static final String CATEGORY_ITEM = "Item";
    public static ForgeConfigSpec.IntValue ACCELERATION_CRYSTAL_UPDATE_TICKS;// = 100;
    public static ForgeConfigSpec.IntValue LIQUID_SUPPRESSION_CRYSTAL_RADIUS;// = 5;
    public static ForgeConfigSpec.IntValue MOTHER_NATURE_CRYSTAL_RANGE;// = 10;
    public static ForgeConfigSpec.IntValue ITEM_MAGNET_RANGE;// = 6;
    public static ForgeConfigSpec.BooleanValue ITEM_CAN_AUTOPLANT;

    // Spell
    private static final String CATEGORY_SPELL = "Spell";
    public static ForgeConfigSpec.IntValue SPELL_COST_SUMMON_FRIENDLY;// = 32;
    public static ForgeConfigSpec.IntValue SPELL_COST_SUMMON_HOSTILE;// = 8;

    // Meteor
    private static final String CATEGORY_METEOR = "Meteor";
    public static ForgeConfigSpec.DoubleValue BASIC_METEOR_SIZE;// = 5.0f;
    public static ForgeConfigSpec.IntValue BASIC_METEOR_EXPLOSION_POWER;// = 25;
    public static ForgeConfigSpec.DoubleValue BASIC_METEOR_EXPLOSION_DROP_RATE;// = 0.1f;

    // Hostile Mobs
    private static final String CATEGORY_HOSTILE_MOBS = "Hostile Mobs";
    public static ForgeConfigSpec.IntValue PERCENT_FOR_SPAWN_HOSTILE_WITH_ARMOR;// = 10;
    public static ForgeConfigSpec.IntValue PERCENT_FOR_SPAWN_HOSTILE_ON_HORSE;// = 15;

    // Evoker Crystal
    private static final String CATEGORY_EVOKER_CRYSTAL = "Evoker Crystal";
    public static ForgeConfigSpec.IntValue VEX_NUMBER;// = 3;
    public static ForgeConfigSpec.BooleanValue VEX_HAS_LIMITED_LIFE;

    // Integration
    private static final String CATEGORY_INTEGRATION = "Integration";
    public static ForgeConfigSpec.BooleanValue SHOW_ADDITIONAL_TOOLTIPS;
    public static ForgeConfigSpec.BooleanValue SHOW_LAST_DEATH_POINT;

    static {
        // Console
        COMMON_BUILDER.comment("Options connected with Console.").push(CATEGORY_CONSOLE);
        SHOW_CUSTOM_TILE_ENTITY_SAVING = COMMON_BUILDER
                .comment("Should console show when World save / load CustomTileEntity (console spam).")
                .define("SHOW_CUSTOM_TILE_ENTITY_SAVING", true);
        COMMON_BUILDER.pop();

        // Player
        COMMON_BUILDER.comment("Options connected with Player").push(CATEGORY_PLAYER);
        GIVE_PLAYER_CUSTOM_BOOKS_ON_LOGIN = COMMON_BUILDER
                .comment("Should Player get Vanilla Magic custom books on spawn (logged in).")
                .define("GIVE_PLAYER_CUSTOM_BOOKS_ON_LOGIN", true);
        COMMON_BUILDER.pop();

        // Machine
        COMMON_BUILDER.comment("Options connected with Vanilla Magic Machines.").push(CATEGORY_MACHINE);
        TILE_SPEEDY_TICKS = COMMON_BUILDER
                .comment("The number of ticks in 1 Minecraft tick that Speedy can do to a single block.")
                .defineInRange("TILE_SPEEDY_TICKS", 1000, 1, Integer.MAX_VALUE);
        TILE_SPEEDY_SIZE = COMMON_BUILDER
                .comment("Size of the Speedy - Area on which Speedy can operate.")
                .defineInRange("TILE_SPEEDY_SIZE", 4, 1, Integer.MAX_VALUE);
        TILE_MACHINE_ONE_OPERATION_COST = COMMON_BUILDER
                .comment("Cost of a single Machine operation.")
                .defineInRange("TILE_MACHINE_ONE_OPERATION_COST", 100, 1, Integer.MAX_VALUE);
        TILE_MACHINE_MAX_TICKS = COMMON_BUILDER
                .comment("Max ticks (internal fuel) that Machine can store.")
                .defineInRange("TILE_MACHINE_MAX_TICKS", 4000, 1, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        // Item
        COMMON_BUILDER.comment("Options connected with Vanilla Magic Items.").push(CATEGORY_ITEM);
        ACCELERATION_CRYSTAL_UPDATE_TICKS = COMMON_BUILDER
                .comment("The number of ticks that should be ticked when Player holds Acceleration Crystal.")
                .defineInRange("ACCELERATION_CRYSTAL_UPDATE_TICKS", 100, 1, Integer.MAX_VALUE);
        LIQUID_SUPPRESSION_CRYSTAL_RADIUS = COMMON_BUILDER
                .comment("Radius on which Liquid Suppression Crystal works (in blocks).")
                .defineInRange("LIQUID_SUPPRESSION_CRYSTAL_RADIUS", 5, 1, Integer.MAX_VALUE);
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