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

    private static final String CATEGORY_PLAYER = "Player";
    public static ForgeConfigSpec.BooleanValue GIVE_PLAYER_VM_BOOKS_ON_LOGIN;

    private static final String CATEGORY_MACHINE = "Machine";
    public static ForgeConfigSpec.IntValue TILE_ACCELERANT_TICKS;
    public static ForgeConfigSpec.IntValue TILE_ACCELERANT_SIZE;
    public static ForgeConfigSpec.DoubleValue TILE_MACHINE_ONE_OPERATION_COST;
    public static ForgeConfigSpec.IntValue TILE_ABSORBER_PULLING_SPEED;
    public static ForgeConfigSpec.IntValue QUARRY_SIZE;
    public static ForgeConfigSpec.IntValue FARM_SIZE;

    private static final String CATEGORY_ITEM = "Item";
    public static ForgeConfigSpec.IntValue ACCELERATION_CRYSTAL_UPDATE_TICKS;
    public static ForgeConfigSpec.IntValue LIQUID_SUPPRESSION_CRYSTAL_RADIUS;
    public static ForgeConfigSpec.IntValue LIQUID_SUPPRESSION_CRYSTAL_REFRESH_RATE;
    public static ForgeConfigSpec.IntValue MOTHER_NATURE_CRYSTAL_RANGE;
    public static ForgeConfigSpec.IntValue ITEM_MAGNET_RANGE;
    public static ForgeConfigSpec.IntValue ITEM_MAGNET_STACK_NUMBER;
    public static ForgeConfigSpec.BooleanValue SHOW_VMITEM_TOOLTIPS;

    private static final String CATEGORY_SPELL = "Spell";
    public static ForgeConfigSpec.IntValue SPELL_FUS_RO_DAH_DISTANCE;
    public static ForgeConfigSpec.DoubleValue SPELL_FUS_RO_DAH_STRENGTH;
    public static ForgeConfigSpec.DoubleValue SPELL_MOVE_IN_AIR_DISTANCE;

    private static final String CATEGORY_METEOR = "Meteor";
    public static ForgeConfigSpec.IntValue METEOR_EXPLOSION_POWER;
    public static ForgeConfigSpec.DoubleValue METEOR_ACCELERATION;

    private static final String CATEGORY_HOSTILE_MOBS = "HostileMobs";
    public static ForgeConfigSpec.IntValue PERCENT_FOR_SPAWN_HOSTILE_ON_HORSE;

    private static final String CATEGORY_EVOKER_CRYSTAL = "EvokerCrystal";
    public static ForgeConfigSpec.IntValue VEX_NUMBER;
    public static ForgeConfigSpec.BooleanValue VEX_HAS_LIMITED_LIFE;

    private static final String CATEGORY_ENHANCEMENTS = "Enhancements";
    public static ForgeConfigSpec.BooleanValue SHOW_LAST_DEATH_POINT;
    public static ForgeConfigSpec.BooleanValue SHOW_HUNGER_TOOLTIP;
    public static ForgeConfigSpec.BooleanValue SHOW_SATURATION_TOOLTIP;
    public static ForgeConfigSpec.BooleanValue SHOW_DURABILITY_TOOLTIP;
    public static ForgeConfigSpec.BooleanValue CAN_AUTOPLANT;

    static {
        COMMON_BUILDER.push(CATEGORY_PLAYER);
        GIVE_PLAYER_VM_BOOKS_ON_LOGIN = COMMON_BUILDER.define("GIVE_PLAYER_VM_BOOKS_ON_LOGIN", true);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.push(CATEGORY_MACHINE);
        TILE_ACCELERANT_TICKS = COMMON_BUILDER.defineInRange("TILE_ACCELERANT_TICKS", 1000, 1, Integer.MAX_VALUE);
        TILE_ACCELERANT_SIZE = COMMON_BUILDER.defineInRange("TILE_ACCELERANT_SIZE", 4, 1, Integer.MAX_VALUE);
        TILE_MACHINE_ONE_OPERATION_COST = COMMON_BUILDER.defineInRange("TILE_MACHINE_ONE_OPERATION_COST", 100, 1, Double.MAX_VALUE);
        TILE_ABSORBER_PULLING_SPEED = COMMON_BUILDER.defineInRange("TILE_ABSORBER_PULLING_SPEED", 100, 1, Integer.MAX_VALUE);
        QUARRY_SIZE = COMMON_BUILDER.defineInRange("QUARRY_SIZE", 16, 1, Integer.MAX_VALUE);
        FARM_SIZE = COMMON_BUILDER.defineInRange("FARM_SIZE", 8, 1, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.push(CATEGORY_ITEM);
        ACCELERATION_CRYSTAL_UPDATE_TICKS = COMMON_BUILDER.defineInRange("ACCELERATION_CRYSTAL_UPDATE_TICKS", 100, 1, Integer.MAX_VALUE);
        LIQUID_SUPPRESSION_CRYSTAL_RADIUS = COMMON_BUILDER.defineInRange("LIQUID_SUPPRESSION_CRYSTAL_RADIUS", 5, 1, Integer.MAX_VALUE);
        LIQUID_SUPPRESSION_CRYSTAL_REFRESH_RATE = COMMON_BUILDER.defineInRange("LIQUID_SUPPRESSION_CRYSTAL_REFRESH_RATE", 100, 1, Integer.MAX_VALUE);
        MOTHER_NATURE_CRYSTAL_RANGE = COMMON_BUILDER.defineInRange("MOTHER_NATURE_CRYSTAL_RANGE", 10, 1, Integer.MAX_VALUE);
        ITEM_MAGNET_RANGE = COMMON_BUILDER.defineInRange("ITEM_MAGNET_RANGE", 6, 1, Integer.MAX_VALUE);
        ITEM_MAGNET_STACK_NUMBER = COMMON_BUILDER.defineInRange("ITEM_MAGNET_STACK_NUMBER", 2, 1, Integer.MAX_VALUE);
        SHOW_VMITEM_TOOLTIPS = COMMON_BUILDER.define("SHOW_VMITEM_TOOLTIPS", true);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.push(CATEGORY_SPELL);
        SPELL_FUS_RO_DAH_DISTANCE = COMMON_BUILDER.defineInRange("SPELL_FUS_RO_DAH_DISTANCE", 8, 1, Integer.MAX_VALUE);
        SPELL_FUS_RO_DAH_STRENGTH = COMMON_BUILDER.defineInRange("SPELL_FUS_RO_DAH_STRENGTH", 2, 1, Double.MAX_VALUE);
        SPELL_MOVE_IN_AIR_DISTANCE = COMMON_BUILDER.defineInRange("SPELL_MOVE_IN_AIR_DISTANCE", 10, 1, Double.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.push(CATEGORY_METEOR);
        METEOR_EXPLOSION_POWER = COMMON_BUILDER.defineInRange("METEOR_EXPLOSION_POWER", 25, 1, Integer.MAX_VALUE);
        METEOR_ACCELERATION = COMMON_BUILDER.defineInRange("METEOR_ACCELERATION", 10, 1, Double.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.push(CATEGORY_HOSTILE_MOBS);
        PERCENT_FOR_SPAWN_HOSTILE_ON_HORSE = COMMON_BUILDER.defineInRange("PERCENT_FOR_SPAWN_HOSTILE_ON_HORSE", 15, 1, 100);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.push(CATEGORY_EVOKER_CRYSTAL);
        VEX_NUMBER = COMMON_BUILDER.defineInRange("VEX_NUMBER", 3, 1, Integer.MAX_VALUE);
        VEX_HAS_LIMITED_LIFE = COMMON_BUILDER.define("VEX_HAS_LIMITED_LIFE", true);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.push(CATEGORY_ENHANCEMENTS);
        SHOW_LAST_DEATH_POINT = COMMON_BUILDER.define("SHOW_LAST_DEATH_POINT", true);
        SHOW_HUNGER_TOOLTIP = COMMON_BUILDER.define("SHOW_HUNGER_TOOLTIP", true);
        SHOW_SATURATION_TOOLTIP = COMMON_BUILDER.define("SHOW_SATURATION_TOOLTIP", true);
        SHOW_DURABILITY_TOOLTIP = COMMON_BUILDER.define("SHOW_DURABILITY_TOOLTIP", true);
        CAN_AUTOPLANT = COMMON_BUILDER.define("CAN_AUTOPLANT", true);
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
