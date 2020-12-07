package net.cpp.init;

import net.cpp.api.CppPotion;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.cpp.Craftingpp.CPP_GROUP_MACHINE;
import static net.cpp.Craftingpp.CPP_GROUP_MISC;
import static net.cpp.Craftingpp.CPP_GROUP_TOOL;
import static net.cpp.init.CppItems.APRICOT;
import static net.cpp.init.CppItems.BANANA;
import static net.cpp.init.CppItems.BAUHINIA;
import static net.cpp.init.CppItems.BAUHINIA_SEED;
import static net.cpp.init.CppItems.BLACKTHORN;
import static net.cpp.init.CppItems.BLACKTHORN_SEED;
import static net.cpp.init.CppItems.BLUEBERRY;
import static net.cpp.init.CppItems.BURNING_CHRYSANTHE;
import static net.cpp.init.CppItems.BURNING_CHRYSANTHE_SEED;
import static net.cpp.init.CppItems.CALAMUS;
import static net.cpp.init.CppItems.CALAMUS_SEED;
import static net.cpp.init.CppItems.CATTAIL;
import static net.cpp.init.CppItems.CATTAIL_SEED;
import static net.cpp.init.CppItems.CHERRY;
import static net.cpp.init.CppItems.CHINESE_DATE;
import static net.cpp.init.CppItems.COCONUT;
import static net.cpp.init.CppItems.ESPARTO;
import static net.cpp.init.CppItems.ESPARTO_SEED;
import static net.cpp.init.CppItems.FLUFFY_GRASS;
import static net.cpp.init.CppItems.FLUFFY_GRASS_SEED;
import static net.cpp.init.CppItems.FORAGE_CRYSTAL;
import static net.cpp.init.CppItems.FORAGE_CRYSTAL_SEED;
import static net.cpp.init.CppItems.GERBERA;
import static net.cpp.init.CppItems.GERBERA_SEED;
import static net.cpp.init.CppItems.GLAZED_SHADE;
import static net.cpp.init.CppItems.GLAZED_SHADE_SEED;
import static net.cpp.init.CppItems.GLOW_FORSYTHIA;
import static net.cpp.init.CppItems.GLOW_FORSYTHIA_SEED;
import static net.cpp.init.CppItems.GOLDEN_GRAPE;
import static net.cpp.init.CppItems.GRAPE;
import static net.cpp.init.CppItems.GRAPEFRUIT;
import static net.cpp.init.CppItems.HAWTHORN;
import static net.cpp.init.CppItems.HIBISCUS;
import static net.cpp.init.CppItems.HIBISCUS_SEED;
import static net.cpp.init.CppItems.HYACINTH;
import static net.cpp.init.CppItems.HYACINTH_SEED;
import static net.cpp.init.CppItems.ISORCHID;
import static net.cpp.init.CppItems.ISORCHID_SEED;
import static net.cpp.init.CppItems.LEMON;
import static net.cpp.init.CppItems.LONGAN;
import static net.cpp.init.CppItems.LOQUAT;
import static net.cpp.init.CppItems.LYCHEE;
import static net.cpp.init.CppItems.LYCORIS_RADIATA;
import static net.cpp.init.CppItems.LYCORIS_RADIATA_SEED;
import static net.cpp.init.CppItems.MANGO;
import static net.cpp.init.CppItems.MARIGOLD;
import static net.cpp.init.CppItems.MARIGOLD_SEED;
import static net.cpp.init.CppItems.ORANGE;
import static net.cpp.init.CppItems.OXALIS;
import static net.cpp.init.CppItems.OXALIS_SEED;
import static net.cpp.init.CppItems.PAYAPA;
import static net.cpp.init.CppItems.PEACH;
import static net.cpp.init.CppItems.PEAR;
import static net.cpp.init.CppItems.PERSIMMON;
import static net.cpp.init.CppItems.PLUM;
import static net.cpp.init.CppItems.POMEGRANATE;
import static net.cpp.init.CppItems.STELERA;
import static net.cpp.init.CppItems.STELERA_SEED;
import static net.cpp.init.CppItems.STRAWBERRY;
import static net.cpp.init.CppItems.TOMATO;
import static net.cpp.init.CppItems.TRIFOLIUM;
import static net.cpp.init.CppItems.TRIFOLIUM_SEED;
import static net.cpp.init.CppItems.WILD_LILIUM;
import static net.cpp.init.CppItems.WILD_LILIUM_SEED;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static net.cpp.Craftingpp.CPP_GROUP_FOOD;
import static net.cpp.Craftingpp.CPP_GROUP_PLANT;

public final class CppItems {

	public static final String MODID = "cpp";

	// 稀有物品
	public static final Item SHARD_OF_THE_DARKNESS;
	public static final Item WING_OF_SKY;
	public static final Item HEART_OF_CRYSTAL;
	public static final Item LIMB_OF_RIDGE;
	public static final Item SOUL_OF_DIRT;
	public static final Item CERTIFICATION_OF_EARTH;
	public static final Item NOVA_OF_FIRE;
	public static final Item SPIRIT_OF_LIFE;
	// 插件
	public static final Item EMPTY_PLUGIN;
	public static final Item LOW_TEMPERATURE_PLUGIN;
	public static final Item LOW_PRESSURE_PLUGIN;
	public static final Item HIGH_TEMPERATURE_PLUGIN;
	public static final Item HIGH_PRESSURE_PLUGIN;
	public static final Item COBBLESTONE_PLUGIN;
	public static final Item STONE_PLUGIN;
	public static final Item BLACKSTONE_PLUGIN;
	public static final Item NETHERRACK_PLUGIN;
	public static final Item END_STONE_PLUGIN;
	public static final Item BASALT_PLUGIN;
	// 上古卷轴
	public static final Item ANCIENT_SCROLL;
	// 附魔金属
	public static final Item ENCHANTED_IRON;
	public static final Item ENCHANTED_GOLD;
	public static final Item ENCHANTED_DIAMOND;
	// 材料
	public static final Item COAL_NUGGET;
	public static final Item CINDER;
	public static final Item SPLINT;
	public static final Item SILICON_PLATE;
	public static final Item MOON_SHARD;
	public static final Item SUN_SHARD;
	public static final Item CLAY_BUCKET;
	// 粉末和瓶装物
	public static final Item COPPER_DUST;
	public static final Item IRON_DUST;
	public static final Item GOLD_DUST;
	public static final Item CARBON_DUST;
	public static final Item EMERALD_DUST;
	public static final Item DIAMOND_DUST;
	public static final Item QUARTZ_DUST;
	public static final Item RARE_EARTH_SALT;
	public static final Item ALKALOID_RARE_EARTH;
	public static final Item RARE_EARTH_DUST;
	public static final Item COARSE_SILICON;
	public static final Item SILICON_DUST;
	public static final Item STEEL_DUST;
	public static final Item FERTILIZER;
	public static final Item BOTTLE_OF_SALT;
	public static final Item ACID;
	public static final Item SODA_WATER;
	public static final Item BIONIC_ACID;
	public static final Item ALKALOID;
	public static final Item BOTTLE_OF_AIR;
	public static final Item AMMONIA_REFRIGERANT;
	// 工具
	public static final Item BLUE_FORCE_OF_SKY;
	public static final Item GREEN_FORCE_OF_WATER;
	public static final Item CYAN_FORCE_OF_MOUNTAIN;
	public static final Item ORANGE_FORCE_OF_DIRT;
	public static final Item YELLOW_FORCE_OF_EARTH;
	public static final Item RED_FORCE_OF_FIRE;
	public static final Item PURPLE_FORCE_OF_LIFE;
	public static final Item WHITE_FORCE_OF_LIGHTNING;
	public static final Item BLACK_FORCE_OF_MOON;
	public static final Item PORTABLE_CRAFTING_TABLE;
	public static final Item PORTABLE_CRAFTING_MACHINE;
	public static final Item MAGNET;
	public static final Item TIME_CHECKER;
	public static final Item TIME_CONDITIONER;
	public static final Item ORIGIN_OF_THE_WORLD;
	public static final Item COMPRESSOR;
	public static final Item ELDER_S_WORDS;
	public static final Item COORDINATE_RECORDER;
	public static final Item MUFFLER;
	public static final Item SACHET;
	public static final Item GRAFTER;
	public static final Item GLASS_PICKAXE;
	public static final Item CHEST_TRANSPORTER;
	public static final Item FIRECRACKERS;
	public static final Item BROOM;
	public static final Item SHOOTING_STAR;
	public static final Item INDUSTRIOUS_HAND;
	public static final Item BREAK_HAND;
	public static final Item SMART_HAND;
	public static final Item ANGRY_HAND;
	public static final Item TOUGHEN_HAND;
	public static final Item GOLEM;
	public static final Item FARMER_GOLEM;
	public static final Item MINER_GOLEM;
	public static final Item FISHER_GOLEM;
	public static final Item WARRIOR_GOLEM;
	public static final Item HERDER_GOLEM;
	public static final Item SANTA_GIFT;
	// 食物
	public static final Item KETCHUP;
	public static final Item STRAWBERRY_JAM;
	public static final Item BLUE_BERRY_JAM;
	public static final Item ORANGE_JAM;
	public static final Item CHERRY_JAM;
	public static final Item APPLE_JAM;
	public static final Item YEAST_POWDER;
	public static final Item BUTTER;
	public static final Item CHEESE;
	public static final Item RAISIN;
	public static final Item TOFFEE_APPLE;
	public static final Item CANDIED_HAW;
	public static final Item CUPCAKE;
	public static final Item CAKE_ROLL;
	public static final Item CROISSANT;
	public static final Item CARAMEL_PUDDING;
	public static final Item DOUGHNUT;
	public static final Item PURPLE_DOUGHNUT;
	public static final Item PINK_DOUGHNUT;
	public static final Item WHITE_DOUGHNUT;
	public static final Item BLUE_DOUGHNUT;
	public static final Item GREEN_DOUGHNUT;
	public static final Item HONEY_PANCAKE;
	public static final Item BOILED_EGG;
	public static final Item BAKED_CARROT;
	public static final Item BACON;
	public static final Item VEGETABLE_KEBAB;
	public static final Item BEEF_KEBAB;
	public static final Item PORK_KEBAB;
	public static final Item LAMB_KEBAB;
	public static final Item CHICKEN_KEBAB;
	public static final Item RAW_COLORFUL_VEGETABLE;
	public static final Item RAW_BRAISED_PORK_WITH_POTATOES;
	public static final Item RAW_BRAISED_BEEF_WITH_POTATOES;
	public static final Item RAW_RABBIT_STEW;
	public static final Item COLORFUL_VEGETABLE;
	public static final Item BRAISED_PORK_WITH_POTATOES;
	public static final Item BRAISED_BEEF_WITH_POTATOES;
	public static final Item RABBIT_STEW;
	public static final Item DUMPLING;
	public static final Item MEAT_FLOSS_BREAD;
	public static final Item CHRISTMAS_ROAST_CHICKEN;
	public static final Item FISH_AND_CHIPS;
	public static final Item BURGER;
	public static final Item PIZZA;
	public static final Item SUSHI;
	public static final Item QINGTUAN;
	public static final Item SAKURA_MOCHI;
	public static final Item ZONGZI;
	public static final Item ZONGZI_BOILED_EGG;
	public static final Item ZONGZI_CHINESE_DATA;
	public static final Item ZONGZI_COOKED_PORKCHOP;
	public static final Item ZONGZI_SALT;
	public static final Item ZONGZI_SODA_WATER;
	public static final Item ZONGZI_HONEY_BOTTLE;
	public static final Item SANDWICH;
	public static final Item STARGAZY_PIE;
	public static final Item BAKED_SHROOMLIGHT;
	public static final Item SOUL_FRIED_CHICKEN;
	public static final Item LUXURIOUS_NETHERITE_BARBECUE;
	public static final Item ASSORTED_ROOT_CANDY;
	public static final Item NETHERITE_MUSHROOM_STEW;
	public static final Item DOUBLE_COLOR_VINE_SALAD;
	public static final Item COLD_DRINK;
	// 水果
	public static final Item APRICOT;
	public static final Item BANANA;
	public static final Item BLUEBERRY;
	public static final Item CHERRY;
	public static final Item CHINESE_DATE;
	public static final Item COCONUT;
	public static final Item GOLDEN_GRAPE;
	public static final Item GRAPE;
	public static final Item GRAPEFRUIT;
	public static final Item HAWTHORN;
	public static final Item LEMON;
	public static final Item LONGAN;
	public static final Item LOQUAT;
	public static final Item LYCHEE;
	public static final Item MANGO;
	public static final Item ORANGE;
	public static final Item PAYAPA;
	public static final Item PEACH;
	public static final Item PEAR;
	public static final Item PERSIMMON;
	public static final Item PLUM;
	public static final Item POMEGRANATE;
	public static final Item STRAWBERRY;
	public static final Item TOMATO;
	public static final Item CITRUS;
	// 药剂
	public static final Item AGENTIA_OF_LIGHTNESS;
	public static final Item AGENTIA_OF_EYESIGHT;
	public static final Item AGENTIA_OF_FIRE_SHIELD;
	public static final Item AGENTIA_OF_WATERLESS;
	public static final Item AGENTIA_OF_TRANSPARENTNESS;
	public static final Item AGENTIA_OF_BOUNCE;
	public static final Item AGENTIA_OF_AGILENESS;
	public static final Item AGENTIA_OF_SHARPNESS;
	public static final Item AGENTIA_OF_REJUVENESS;
	public static final Item AGENTIA_OF_BLOOD;
	public static final Item AGENTIA_OF_EXTREMENESS;
	public static final Item AGENTIA_OF_SHIELD;
	public static final Item AGENTIA_OF_TIDE;
	public static final Item AGENTIA_OF_CHAIN;
	public static final Item AGENTIA_OF_SKY;
	public static final Item AGENTIA_OF_OCEAN;
	public static final Item AGENTIA_OF_RIDGE;
	public static final Item AGENTIA_OF_DIRT;
	public static final Item AGENTIA_OF_EARTH;
	public static final Item AGENTIA_OF_FIRE;
	public static final Item AGENTIA_OF_LIFE;
	// 植物
	public static final Item LYCORIS_RADIATA_SEED;
	public static final Item TRIFOLIUM_SEED;
	public static final Item BLACKTHORN_SEED;
	public static final Item CATTAIL_SEED;
	public static final Item MARIGOLD_SEED;
	public static final Item HIBISCUS_SEED;
	public static final Item HYACINTH_SEED;
	public static final Item CALAMUS_SEED;
	public static final Item WILD_LILIUM_SEED;
	public static final Item BAUHINIA_SEED;
	public static final Item FLUFFY_GRASS_SEED;
	public static final Item GERBERA_SEED;
	public static final Item ESPARTO_SEED;
	public static final Item GLOW_FORSYTHIA_SEED;
	public static final Item GLAZED_SHADE_SEED;
	public static final Item STELERA_SEED;
	public static final Item FORAGE_CRYSTAL_SEED;
	public static final Item ISORCHID_SEED;
	public static final Item BURNING_CHRYSANTHE_SEED;
	public static final Item OXALIS_SEED;
	public static final Item LYCORIS_RADIATA;
	public static final Item TRIFOLIUM;
	public static final Item BLACKTHORN;
	public static final Item CATTAIL;
	public static final Item MARIGOLD;
	public static final Item HIBISCUS;
	public static final Item HYACINTH;
	public static final Item CALAMUS;
	public static final Item WILD_LILIUM;
	public static final Item BAUHINIA;
	public static final Item FLUFFY_GRASS;
	public static final Item GERBERA;
	public static final Item ESPARTO;
	public static final Item GLOW_FORSYTHIA;
	public static final Item GLAZED_SHADE;
	public static final Item STELERA;
	public static final Item FORAGE_CRYSTAL;
	public static final Item ISORCHID;
	public static final Item BURNING_CHRYSANTHE;
	public static final Item OXALIS;
	public static final Item CALLIOPSIS;
	public static final Item CYCLAMEN;
	public static final Item IRIS;
	public static final Item LILIUM_PUMILUM;
	public static final Item SNOWDROP;
	public static final Item NARCISSUS;
	public static final Item COLE_FLOWER;
	public static final Item LUPINE;
	public static final Item CROCU;
	public static final Item PANSY;
	public static final Item ARABIA_SPEEDWELL;
	public static final Item SILENE_PENDULA;
	public static final Item ARTEMISIA_ARGYI;
	public static final Item BLUE_ROSE;
	public static final Item POINSETTIA;
	public static final Item CHRISTMAS_TREE;
	public static final Item RICE_SEED;
	public static final Item RICE;
	public static final Item FRUIT_SAPLING;
	public static final Item ORE_SAPLING;
	public static final Item WOOL_SAPLING;
	public static final Item SAKURA_SAPLING;
	public static final Item FRUIT_LEAVES;
	public static final Item ORE_LEAVES;
	public static final Item WOOL_LEAVES;
	public static final Item SAKURA_LEAVES;
	// 仪式和魔法
	public static final Item SEALING_WAND;
	public static final Item STAR_WAND;
	public static final Item DREAM_WAND;
	public static final Item TEMPERANCER;
	public static final Item WAND_OF_THE_DARKNESS;
	// 装饰-装备
	public static final Item RED_LIP;
	public static final Item PURPLE_EYE;
	public static final Item LASH;
	public static final Item CAT_BREED;
	public static final Item GARLAND;
	public static final Item BLACK_FRAMED_GLASSES;
	public static final Item ORANGE_FRAMED_GLASSES;
	public static final Item JOKING_GLASSES;
	public static final Item MINION_GOGGLES;
	public static final Item PANTS;
	public static final Item EMPIRE_HAT;
	public static final Item GLASS_HELMET;
	public static final Item GLOW_HAT;
	public static final Item GREEN_HAT;
	public static final Item BLACK_HAT;
	public static final Item NURSE_HAT;
	public static final Item JACKET;
	public static final Item RED_COAT;
	public static final Item TIGHT_LEATHER_LEGGINGS;
	public static final Item BLUE_JEANS;
	public static final Item SNOW_BOOTS;
	// 装饰-杂
	public static final Item CLASSICAL_PAINTING;
	public static final Item SPRING_FESTIVAL_DECORATIONS;
	// 纸片人
	public static final Item CHARACTER;
	// 标志和图案
	public static final Item CHINA_FLAG;
	public static final Item USA_FLAG;
	public static final Item RUSSIA_FLAG;
	public static final Item UK_FLAG;
	public static final Item FRANCE_FLAG;
	public static final Item BILIBILI_LOGO;
	public static final Item GITHUB_LOGO;
	public static final Item MCMOD_LOGO;
	public static final Item TCP_LOGO;
	public static final Item CBL_LOGO;
	public static final Item NF_LOGO;
	public static final Item SHIELD_CHINA_FLAG;
	public static final Item SHIELD_USA_FLAG;
	public static final Item SHIELD_RUSSIA_FLAG;
	public static final Item SHIELD_UK_FLAG;
	public static final Item SHIELD_FRANCE_FLAG;
	public static final Item SHIELD_BILIBILI;
	public static final Item SHIELD_GITHUB;
	public static final Item SHIELD_MCMOD;
	public static final Item SHIELD_TCP;
	public static final Item SHIELD_CBL;
	public static final Item SHIELD_NF;
	// 告示牌
	public static final Item WHITE_SIGN;
	public static final Item ORANGE_SIGN;
	public static final Item MAGENTA_SIGN;
	public static final Item LIGHT_BLUE_SIGN;
	public static final Item YELLOW_SIGN;
	public static final Item LIME_SIGN;
	public static final Item PINK_SIGN;
	public static final Item GRAY_SIGN;
	public static final Item LIGHT_GRAY_SIGN;
	public static final Item CYAN_SIGN;
	public static final Item PURPLE_SIGN;
	public static final Item BLUE_SIGN;
	public static final Item BROWN_SIGN;
	public static final Item GREEN_SIGN;
	public static final Item RED_SIGN;
	public static final Item BLACK_SIGN;
	public static final Item GLASS_SIGN;
	public static final Map<Item, Item> SEEDS_TO_FLOWERS;
	public static final Set<Item> FRUITS;
	static {
		SHARD_OF_THE_DARKNESS = registerItem("shard_of_the_darkness",
				new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		WING_OF_SKY = registerItem("wing_of_sky", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		HEART_OF_CRYSTAL = registerItem("heart_of_crystal", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		LIMB_OF_RIDGE = registerItem("limb_of_ridge", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SOUL_OF_DIRT = registerItem("soul_of_dirt", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		CERTIFICATION_OF_EARTH = registerItem("certification_of_earth",
				new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		NOVA_OF_FIRE = registerItem("nova_of_fire", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SPIRIT_OF_LIFE = registerItem("spirit_of_life", new Item(new Item.Settings().group(CPP_GROUP_MISC)));

		EMPTY_PLUGIN = registerItem("empty_plugin", new Item(new Item.Settings().group(CPP_GROUP_MACHINE)));
		LOW_TEMPERATURE_PLUGIN = registerItem("low_temperature_plugin",
				new Item(new Item.Settings().group(CPP_GROUP_MACHINE)));
		LOW_PRESSURE_PLUGIN = registerItem("low_pressure_plugin",
				new Item(new Item.Settings().group(CPP_GROUP_MACHINE)));
		HIGH_TEMPERATURE_PLUGIN = registerItem("high_temperature_plugin",
				new Item(new Item.Settings().group(CPP_GROUP_MACHINE)));
		HIGH_PRESSURE_PLUGIN = registerItem("high_pressure_plugin",
				new Item(new Item.Settings().group(CPP_GROUP_MACHINE)));
		COBBLESTONE_PLUGIN = registerItem("cobblestone_plugin", new Item(new Item.Settings().group(CPP_GROUP_MACHINE)));
		STONE_PLUGIN = registerItem("stone_plugin", new Item(new Item.Settings().group(CPP_GROUP_MACHINE)));
		BLACKSTONE_PLUGIN = registerItem("blackstone_plugin", new Item(new Item.Settings().group(CPP_GROUP_MACHINE)));
		NETHERRACK_PLUGIN = registerItem("netherrack_plugin", new Item(new Item.Settings().group(CPP_GROUP_MACHINE)));
		END_STONE_PLUGIN = registerItem("end_stone_plugin", new Item(new Item.Settings().group(CPP_GROUP_MACHINE)));
		BASALT_PLUGIN = registerItem("basalt_plugin", new Item(new Item.Settings().group(CPP_GROUP_MACHINE)));

		ANCIENT_SCROLL = registerItem("ancient_scroll", new Item(new Item.Settings().group(CPP_GROUP_MISC)));

		ENCHANTED_IRON = registerItem("enchanted_iron", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		ENCHANTED_GOLD = registerItem("enchanted_gold", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		ENCHANTED_DIAMOND = registerItem("enchanted_diamond", new Item(new Item.Settings().group(CPP_GROUP_MISC)));

		COAL_NUGGET = registerItem("coal_nugget", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		CINDER = registerItem("cinder", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SPLINT = registerItem("splint", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		Registry.register(Registry.ITEM, new Identifier("cpp:rare_earth_glass"),
				new BlockItem(CppBlocks.RARE_EARTH_GLASS, new Item.Settings().group(CPP_GROUP_MISC)));
		Registry.register(Registry.ITEM, new Identifier("cpp:reinforced_glass"),
				new BlockItem(CppBlocks.REINFORCED_GLASS, new Item.Settings().group(CPP_GROUP_MISC)));
		SILICON_PLATE = registerItem("silicon_plate", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		MOON_SHARD = registerItem("moon_shard", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SUN_SHARD = registerItem("sun_shard", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		Registry.register(Registry.ITEM, new Identifier("cpp:moon_stone"),
				new BlockItem(CppBlocks.MOON_STONE, new Item.Settings().group(CPP_GROUP_MISC)));
		Registry.register(Registry.ITEM, new Identifier("cpp:sun_stone"),
				new BlockItem(CppBlocks.SUN_STONE, new Item.Settings().group(CPP_GROUP_MISC)));
		CLAY_BUCKET = registerItem("clay_bucket", new Item(new Item.Settings().group(CPP_GROUP_MISC)));

		COPPER_DUST = registerItem("copper_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		IRON_DUST = registerItem("iron_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		GOLD_DUST = registerItem("gold_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		CARBON_DUST = registerItem("carbon_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		EMERALD_DUST = registerItem("emerald_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		DIAMOND_DUST = registerItem("diamond_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		QUARTZ_DUST = registerItem("quartz_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		RARE_EARTH_SALT = registerItem("rare_earth_salt", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		ALKALOID_RARE_EARTH = registerItem("alkaloid_rare_earth", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		RARE_EARTH_DUST = registerItem("rare_earth_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		COARSE_SILICON = registerItem("coarse_silicon", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SILICON_DUST = registerItem("silicon_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		STEEL_DUST = registerItem("steel_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		FERTILIZER = registerItem("fertilizer", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		BOTTLE_OF_SALT = registerItem("bottle_of_salt", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		ACID = registerItem("acid", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SODA_WATER = registerItem("soda_water", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		BIONIC_ACID = registerItem("bionic_acid", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		ALKALOID = registerItem("alkaloid", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		BOTTLE_OF_AIR = registerItem("bottle_of_air", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		AMMONIA_REFRIGERANT = registerItem("ammonia_refrigerant", new Item(new Item.Settings().group(CPP_GROUP_MISC)));

		BLUE_FORCE_OF_SKY = registerItem("blue_force_of_sky", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		GREEN_FORCE_OF_WATER = registerItem("green_force_of_water",
				new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		CYAN_FORCE_OF_MOUNTAIN = registerItem("cyan_force_of_mountain",
				new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		ORANGE_FORCE_OF_DIRT = registerItem("orange_force_of_dirt",
				new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		YELLOW_FORCE_OF_EARTH = registerItem("yellow_force_of_earth",
				new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		RED_FORCE_OF_FIRE = registerItem("red_force_of_fire", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		PURPLE_FORCE_OF_LIFE = registerItem("purple_force_of_life",
				new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		WHITE_FORCE_OF_LIGHTNING = registerItem("white_force_of_lightning",
				new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		BLACK_FORCE_OF_MOON = registerItem("black_force_of_moon", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		PORTABLE_CRAFTING_TABLE = registerItem("portable_crafting_table",
				new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		PORTABLE_CRAFTING_MACHINE = registerItem("portable_crafting_machine",
				new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		MAGNET = registerItem("magnet", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		TIME_CHECKER = registerItem("time_checker", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		TIME_CONDITIONER = registerItem("time_conditioner", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		ORIGIN_OF_THE_WORLD = registerItem("origin_of_the_world", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		COMPRESSOR = registerItem("compressor", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		ELDER_S_WORDS = registerItem("elder_s_words", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		COORDINATE_RECORDER = registerItem("coordinate_recorder", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		MUFFLER = registerItem("muffler", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		SACHET = registerItem("sachet", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		GRAFTER = registerItem("grafter", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		GLASS_PICKAXE = registerItem("glass_pickaxe", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		CHEST_TRANSPORTER = registerItem("chest_transporter", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		FIRECRACKERS = registerItem("firecrackers", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		BROOM = registerItem("broom", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		SHOOTING_STAR = registerItem("shooting_star", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		INDUSTRIOUS_HAND = registerItem("industrious_hand", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		BREAK_HAND = registerItem("break_hand", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		SMART_HAND = registerItem("smart_hand", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		ANGRY_HAND = registerItem("angry_hand", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		TOUGHEN_HAND = registerItem("toughen_hand", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		GOLEM = registerItem("golem", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		FARMER_GOLEM = registerItem("farmer_golem", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		MINER_GOLEM = registerItem("miner_golem", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		FISHER_GOLEM = registerItem("fisher_golem", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		WARRIOR_GOLEM = registerItem("warrior_golem", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		HERDER_GOLEM = registerItem("herder_golem", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		SANTA_GIFT = registerItem("santa_gift", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));

		KETCHUP = registerItem("ketchup", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
		STRAWBERRY_JAM = registerItem("strawberry_jam", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
		BLUE_BERRY_JAM = registerItem("blue_berry_jam", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
		ORANGE_JAM = registerItem("orange_jam", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
		CHERRY_JAM = registerItem("cherry_jam", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
		APPLE_JAM = registerItem("apple_jam", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
		YEAST_POWDER = registerItem("yeast_powder", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
		BUTTER = registerItem("butter", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
		CHEESE = registerItem("cheese", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
		RAISIN = registerItem("raisin",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 0), 1.0F).build())));
		TOFFEE_APPLE = registerItem("toffee_apple",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())));
		CANDIED_HAW = registerItem("candied_haw",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())
						.maxCount(16)));
		CUPCAKE = registerItem("cupcake", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).build())));
		CAKE_ROLL = registerItem("cake_roll", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).build())));
		CROISSANT = registerItem("croissant", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).build())));
		CARAMEL_PUDDING = registerItem("caramel_pudding",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())
						.maxCount(16)));
		DOUGHNUT = registerItem("doughnut",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(7).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())));
		PURPLE_DOUGHNUT = registerItem("purple_doughnut",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(7).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())));
		PINK_DOUGHNUT = registerItem("pink_doughnut",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(7).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())));
		WHITE_DOUGHNUT = registerItem("white_doughnut",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(7).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())));
		BLUE_DOUGHNUT = registerItem("blue_doughnut",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(7).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())));
		GREEN_DOUGHNUT = registerItem("green_doughnut",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(7).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())));
		HONEY_PANCAKE = registerItem("honey_pancake",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())
						.maxCount(16)));
		BOILED_EGG = registerItem("boiled_egg", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
		BAKED_CARROT = registerItem("baked_carrot", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).build())));
		BACON = registerItem("bacon", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F).build())));
		VEGETABLE_KEBAB = registerItem("vegetable_kebab",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 600, 0), 1.0F).build())
						.maxCount(16)));
		BEEF_KEBAB = registerItem("beef_kebab",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(16).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build())
						.maxCount(16)));
		PORK_KEBAB = registerItem("pork_kebab",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(16).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build())
						.maxCount(16)));
		LAMB_KEBAB = registerItem("lamb_kebab",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(14).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build())
						.maxCount(16)));
		CHICKEN_KEBAB = registerItem("chicken_kebab",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(14).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build())
						.maxCount(16)));
		RAW_COLORFUL_VEGETABLE = registerItem("raw_colorful_vegetable",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F).build()).maxCount(1)));
		RAW_BRAISED_PORK_WITH_POTATOES = registerItem("raw_braised_pork_with_potatoes",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F).build()).maxCount(1)));
		RAW_BRAISED_BEEF_WITH_POTATOES = registerItem("raw_braised_beef_with_potatoes",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F).build()).maxCount(1)));
		RAW_RABBIT_STEW = registerItem("raw_rabbit_stew", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F).build()).maxCount(1)));
		COLORFUL_VEGETABLE = registerItem("colorful_vegetable",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(14).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 600, 0), 1.0F).build())
						.maxCount(1)));
		BRAISED_PORK_WITH_POTATOES = registerItem("braised_pork_with_potatoes",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(16).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build())
						.maxCount(1)));
		BRAISED_BEEF_WITH_POTATOES = registerItem("braised_beef_with_potatoes",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(16).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build())
						.maxCount(1)));
		RABBIT_STEW = registerItem("rabbit_stew",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(16).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build())
						.maxCount(1)));
		DUMPLING = registerItem("dumpling", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(8).saturationModifier(0.3F).build())));
		MEAT_FLOSS_BREAD = registerItem("meat_floss_bread",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())
						.maxCount(16)));
		CHRISTMAS_ROAST_CHICKEN = registerItem("christmas_roast_chicken",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(16).saturationModifier(0.3F).build()).maxCount(8)));
		FISH_AND_CHIPS = registerItem("fish_and_chips",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(16).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())
						.maxCount(8)));
		BURGER = registerItem("burger", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(20).saturationModifier(0.3F).build()).maxCount(8)));
		PIZZA = registerItem("pizza", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).build())));
		SUSHI = registerItem("sushi", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).build())));
		QINGTUAN = registerItem("qingtuan", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).build())));
		SAKURA_MOCHI = registerItem("sakura_mochi", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).build())));
		ZONGZI = registerItem("zongzi", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(9).saturationModifier(0.3F).build()).maxCount(16)));
		ZONGZI_BOILED_EGG = registerItem("zongzi_boiled_egg", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(9).saturationModifier(0.3F).build()).maxCount(16)));
		ZONGZI_CHINESE_DATA = registerItem("zongzi_chinese_data", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(9).saturationModifier(0.3F).build()).maxCount(16)));
		ZONGZI_COOKED_PORKCHOP = registerItem("zongzi_cooked_porkchop",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(9).saturationModifier(0.3F).build()).maxCount(16)));
		ZONGZI_SALT = registerItem("zongzi_salt", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(9).saturationModifier(0.3F).build()).maxCount(16)));
		ZONGZI_SODA_WATER = registerItem("zongzi_soda_water", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(9).saturationModifier(0.3F).build()).maxCount(16)));
		ZONGZI_HONEY_BOTTLE = registerItem("zongzi_honey_bottle", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(9).saturationModifier(0.3F).build()).maxCount(16)));
		SANDWICH = registerItem("sandwich",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build())
						.maxCount(16)));
		STARGAZY_PIE = registerItem("stargazy_pie", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(16).saturationModifier(0.3F).build()).maxCount(8)));
		BAKED_SHROOMLIGHT = registerItem("baked_shroomlight", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F)
						.statusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 600, 0), 1.0F)
						.statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600, 0), 1.0F).build())));
		SOUL_FRIED_CHICKEN = registerItem("soul_fried_chicken", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F)
						.statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 600, 0), 1.0F).build())
				.maxCount(16)));
		LUXURIOUS_NETHERITE_BARBECUE = registerItem("luxurious_netherite_barbecue",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(20).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build())
						.maxCount(1)));
		ASSORTED_ROOT_CANDY = registerItem("assorted_root_candy",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())
						.maxCount(16)));
		NETHERITE_MUSHROOM_STEW = registerItem("netherite_mushroom_stew",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(12).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0), 1.0F).build())
						.maxCount(1)));
		DOUBLE_COLOR_VINE_SALAD = registerItem("double_color_vine_salad",
				new Item(new Item.Settings().group(CPP_GROUP_FOOD)
						.food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F)
								.statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 600, 0), 1.0F)
								.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0), 1.0F).build())
						.maxCount(1)));
		COLD_DRINK = registerItem("cold_drink", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).build()).maxCount(16)));

		APRICOT = registerItem("apricot", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		BANANA = registerItem("banana", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		BLUEBERRY = registerItem("blueberry", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		CHERRY = registerItem("cherry", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		CHINESE_DATE = registerItem("chinese_date", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		COCONUT = registerItem("coconut", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		GOLDEN_GRAPE = registerItem("golden_grape", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		GRAPE = registerItem("grape", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		GRAPEFRUIT = registerItem("grapefruit", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		HAWTHORN = registerItem("hawthorn", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		LEMON = registerItem("lemon", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		LONGAN = registerItem("longan", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		LOQUAT = registerItem("loquat", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		LYCHEE = registerItem("lychee", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		MANGO = registerItem("mango", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		ORANGE = registerItem("orange", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		PAYAPA = registerItem("payapa", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		PEACH = registerItem("peach", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		PEAR = registerItem("pear", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		PERSIMMON = registerItem("persimmon", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		PLUM = registerItem("plum", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		POMEGRANATE = registerItem("pomegranate", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		STRAWBERRY = registerItem("strawberry", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		TOMATO = registerItem("tomato", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
		CITRUS = registerItem("citrus", new Item(new Item.Settings().group(CPP_GROUP_FOOD)
				.food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));

		AGENTIA_OF_LIGHTNESS = registerItem("agentia_of_lightness",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.SLOW_FALLING, 4800, 0)));
		AGENTIA_OF_EYESIGHT = registerItem("agentia_of_eyesight",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.NIGHT_VISION, 9600, 0)));
		AGENTIA_OF_FIRE_SHIELD = registerItem("agentia_of_fire_shield",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 9600, 0)));
		AGENTIA_OF_WATERLESS = registerItem("agentia_of_waterless",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.WATER_BREATHING, 9600, 0)));
		AGENTIA_OF_TRANSPARENTNESS = registerItem("agentia_of_transparentness",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.INVISIBILITY, 9600, 0)));
		AGENTIA_OF_BOUNCE = registerItem("agentia_of_bounce",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.JUMP_BOOST, 4800, 1)));
		AGENTIA_OF_AGILENESS = registerItem("agentia_of_agileness",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.SPEED, 4800, 1)));
		AGENTIA_OF_SHARPNESS = registerItem("agentia_of_sharpness",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.STRENGTH, 4800, 1)));
		AGENTIA_OF_REJUVENESS = registerItem("agentia_of_rejuveness",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.REGENERATION, 2400, 1)));
		AGENTIA_OF_BLOOD = registerItem("agentia_of_blood",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 2)));
		AGENTIA_OF_EXTREMENESS = registerItem("agentia_of_extremeness",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.HASTE, 4800, 1)));
		AGENTIA_OF_SHIELD = registerItem("agentia_of_shield",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.RESISTANCE, 4800, 1)));
		AGENTIA_OF_TIDE = registerItem("agentia_of_tide",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 4800, 0)));
		AGENTIA_OF_CHAIN = registerItem("agentia_of_chain",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(CppEffects.CHAIN, 9600, 0)));
		AGENTIA_OF_SKY = registerItem("agentia_of_sky",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.SPEED, 7200, 1),
						new StatusEffectInstance(StatusEffects.SLOW_FALLING, 7200, 0)));
		AGENTIA_OF_OCEAN = registerItem("agentia_of_ocean",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.WATER_BREATHING, 14400, 0),
						new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 7200, 0)));
		AGENTIA_OF_RIDGE = registerItem("agentia_of_ridge",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.STRENGTH, 7200, 1),
						new StatusEffectInstance(StatusEffects.NIGHT_VISION, 14400, 0)));
		AGENTIA_OF_DIRT = registerItem("agentia_of_dirt",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.INVISIBILITY, 14400, 0),
						new StatusEffectInstance(CppEffects.CHAIN, 14400, 0)));
		AGENTIA_OF_EARTH = registerItem("agentia_of_earth",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.JUMP_BOOST, 7200, 1),
						new StatusEffectInstance(StatusEffects.HASTE, 7200, 1)));
		AGENTIA_OF_FIRE = registerItem("agentia_of_fire",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 14400, 0),
						new StatusEffectInstance(StatusEffects.RESISTANCE, 7200, 1)));
		AGENTIA_OF_LIFE = registerItem("agentia_of_life",
				new CppPotion(new Item.Settings().group(CPP_GROUP_MISC).maxCount(1),
						new StatusEffectInstance(StatusEffects.REGENERATION, 3600, 1),
						new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 3)));

		LYCORIS_RADIATA_SEED = registerItem("lycoris_radiata_seed",
				new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		TRIFOLIUM_SEED = registerItem("trifolium_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		BLACKTHORN_SEED = registerItem("blackthorn_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		CATTAIL_SEED = registerItem("cattail_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		MARIGOLD_SEED = registerItem("marigold_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		HIBISCUS_SEED = registerItem("hibiscus_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		HYACINTH_SEED = registerItem("hyacinth_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		CALAMUS_SEED = registerItem("calamus_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		WILD_LILIUM_SEED = registerItem("wild_lilium_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		BAUHINIA_SEED = registerItem("bauhinia_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		FLUFFY_GRASS_SEED = registerItem("fluffy_grass_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		GERBERA_SEED = registerItem("gerbera_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		ESPARTO_SEED = registerItem("esparto_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		GLOW_FORSYTHIA_SEED = registerItem("glow_forsythia_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		GLAZED_SHADE_SEED = registerItem("glazed_shade_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		STELERA_SEED = registerItem("stelera_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		FORAGE_CRYSTAL_SEED = registerItem("forage_crystal_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		ISORCHID_SEED = registerItem("isorchid_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		BURNING_CHRYSANTHE_SEED = registerItem("burning_chrysanthe_seed",
				new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		OXALIS_SEED = registerItem("oxalis_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		LYCORIS_RADIATA = registerItem("lycoris_radiata", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		TRIFOLIUM = registerItem("trifolium", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		BLACKTHORN = registerItem("blackthorn", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		CATTAIL = registerItem("cattail", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		MARIGOLD = registerItem("marigold", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		HIBISCUS = registerItem("hibiscus", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		HYACINTH = registerItem("hyacinth", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		CALAMUS = registerItem("calamus", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		WILD_LILIUM = registerItem("wild_lilium", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		BAUHINIA = registerItem("bauhinia", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		FLUFFY_GRASS = registerItem("fluffy_grass", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		GERBERA = registerItem("gerbera", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		ESPARTO = registerItem("esparto", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		GLOW_FORSYTHIA = registerItem("glow_forsythia", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		GLAZED_SHADE = registerItem("glazed_shade", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		STELERA = registerItem("stelera", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		FORAGE_CRYSTAL = registerItem("forage_crystal", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		ISORCHID = registerItem("isorchid", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		BURNING_CHRYSANTHE = registerItem("burning_chrysanthe", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		OXALIS = registerItem("oxalis", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		CALLIOPSIS = registerItem("calliopsis", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		CYCLAMEN = registerItem("cyclamen", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		IRIS = registerItem("iris", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		LILIUM_PUMILUM = registerItem("lilium_pumilum", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		SNOWDROP = registerItem("snowdrop", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		NARCISSUS = registerItem("narcissus", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		COLE_FLOWER = registerItem("cole_flower", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		LUPINE = registerItem("lupine", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		CROCU = registerItem("crocu", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		PANSY = registerItem("pansy", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		ARABIA_SPEEDWELL = registerItem("arabia_speedwell", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		SILENE_PENDULA = registerItem("silene_pendula", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		ARTEMISIA_ARGYI = registerItem("artemisia_argyi", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		BLUE_ROSE = registerItem("blue_rose", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		POINSETTIA = registerItem("poinsettia", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		CHRISTMAS_TREE = registerItem("christmas_tree", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		RICE_SEED = registerItem("rice_seed", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		RICE = registerItem("rice", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		FRUIT_SAPLING = registerItem("fruit_sapling", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		ORE_SAPLING = registerItem("ore_sapling", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		WOOL_SAPLING = registerItem("wool_sapling", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		SAKURA_SAPLING = registerItem("sakura_sapling", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		FRUIT_LEAVES = registerItem("fruit_leaves", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		ORE_LEAVES = registerItem("ore_leaves", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		WOOL_LEAVES = registerItem("wool_leaves", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));
		SAKURA_LEAVES = registerItem("sakura_leaves", new Item(new Item.Settings().group(CPP_GROUP_PLANT)));

		SEALING_WAND = registerItem("sealing_wand", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		STAR_WAND = registerItem("star_wand", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		DREAM_WAND = registerItem("dream_wand", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		TEMPERANCER = registerItem("temperancer", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		WAND_OF_THE_DARKNESS = registerItem("wand_of_the_darkness",
				new Item(new Item.Settings().group(CPP_GROUP_TOOL)));

		RED_LIP = registerItem("red_lip", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		PURPLE_EYE = registerItem("purple_eye", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		LASH = registerItem("lash", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		CAT_BREED = registerItem("cat_breed", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		GARLAND = registerItem("garland", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		BLACK_FRAMED_GLASSES = registerItem("black_framed_glasses",
				new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		ORANGE_FRAMED_GLASSES = registerItem("orange_framed_glasses",
				new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		JOKING_GLASSES = registerItem("joking_glasses", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		MINION_GOGGLES = registerItem("minion_goggles", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		PANTS = registerItem("pants", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		EMPIRE_HAT = registerItem("empire_hat", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		GLASS_HELMET = registerItem("glass_helmet", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		GLOW_HAT = registerItem("glow_hat", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		GREEN_HAT = registerItem("green_hat", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		BLACK_HAT = registerItem("black_hat", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		NURSE_HAT = registerItem("nurse_hat", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		JACKET = registerItem("jacket", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		RED_COAT = registerItem("red_coat", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		TIGHT_LEATHER_LEGGINGS = registerItem("tight_leather_leggings",
				new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		BLUE_JEANS = registerItem("blue_jeans", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
		SNOW_BOOTS = registerItem("snow_boots", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));

		CLASSICAL_PAINTING = registerItem("classical_painting", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SPRING_FESTIVAL_DECORATIONS = registerItem("spring_festival_decorations",
				new Item(new Item.Settings().group(CPP_GROUP_MISC)));

		CHARACTER = registerItem("character", new Item(new Item.Settings().group(CPP_GROUP_MISC)));

		CHINA_FLAG = registerItem("china_flag", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		USA_FLAG = registerItem("usa_flag", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		RUSSIA_FLAG = registerItem("russia_flag", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		UK_FLAG = registerItem("uk_flag", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		FRANCE_FLAG = registerItem("france_flag", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		BILIBILI_LOGO = registerItem("bilibili_logo", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		GITHUB_LOGO = registerItem("github_logo", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		MCMOD_LOGO = registerItem("mcmod_logo", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		TCP_LOGO = registerItem("tcp_logo", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		CBL_LOGO = registerItem("cbl_logo", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		NF_LOGO = registerItem("nf_logo", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SHIELD_CHINA_FLAG = registerItem("shield_china_flag", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SHIELD_USA_FLAG = registerItem("shield_usa_flag", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SHIELD_RUSSIA_FLAG = registerItem("shield_russia_flag", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SHIELD_UK_FLAG = registerItem("shield_uk_flag", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SHIELD_FRANCE_FLAG = registerItem("shield_france_flag", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SHIELD_BILIBILI = registerItem("shield_bilibili", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SHIELD_GITHUB = registerItem("shield_github", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SHIELD_MCMOD = registerItem("shield_mcmod", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SHIELD_TCP = registerItem("shield_tcp", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SHIELD_CBL = registerItem("shield_cbl", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		SHIELD_NF = registerItem("shield_nf", new Item(new Item.Settings().group(CPP_GROUP_MISC)));

		WHITE_SIGN = registerItem("white_sign", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		ORANGE_SIGN = registerItem("orange_sign", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		MAGENTA_SIGN = registerItem("magenta_sign", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		LIGHT_BLUE_SIGN = registerItem("light_blue_sign", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		YELLOW_SIGN = registerItem("yellow_sign", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		LIME_SIGN = registerItem("lime_sign", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		PINK_SIGN = registerItem("pink_sign", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		GRAY_SIGN = registerItem("gray_sign", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		LIGHT_GRAY_SIGN = registerItem("light_gray_sign", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		CYAN_SIGN = registerItem("cyan_sign", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		PURPLE_SIGN = registerItem("purple_sign", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		BLUE_SIGN = registerItem("blue_sign", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		BROWN_SIGN = registerItem("brown_sign", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		GREEN_SIGN = registerItem("green_sign", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		RED_SIGN = registerItem("red_sign", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		BLACK_SIGN = registerItem("black_sign", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		GLASS_SIGN = registerItem("glass_sign", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
		{
			Item[] flowers = { LYCORIS_RADIATA, TRIFOLIUM, BLACKTHORN, CATTAIL, MARIGOLD, HIBISCUS, HYACINTH, CALAMUS,
					WILD_LILIUM, BAUHINIA, FLUFFY_GRASS, GERBERA, ESPARTO, GLOW_FORSYTHIA, GLAZED_SHADE, STELERA,
					FORAGE_CRYSTAL, ISORCHID, BURNING_CHRYSANTHE, OXALIS },
					seeds = { LYCORIS_RADIATA_SEED, TRIFOLIUM_SEED, BLACKTHORN_SEED, CATTAIL_SEED, MARIGOLD_SEED,
							HIBISCUS_SEED, HYACINTH_SEED, CALAMUS_SEED, WILD_LILIUM_SEED, BAUHINIA_SEED,
							FLUFFY_GRASS_SEED, GERBERA_SEED, ESPARTO_SEED, GLOW_FORSYTHIA_SEED, GLAZED_SHADE_SEED,
							STELERA_SEED, FORAGE_CRYSTAL_SEED, ISORCHID_SEED, BURNING_CHRYSANTHE_SEED, OXALIS_SEED };
			SEEDS_TO_FLOWERS = new HashMap<Item, Item>();
			for (int i = 0; i < flowers.length; i++)
				SEEDS_TO_FLOWERS.put(seeds[i], flowers[i]);
		}

		FRUITS = new HashSet<>(Arrays.asList(APRICOT, BANANA, BLUEBERRY, CHERRY, CHINESE_DATE, COCONUT, GOLDEN_GRAPE,
				GRAPE, GRAPEFRUIT, HAWTHORN, LEMON, LONGAN, LOQUAT, LYCHEE, MANGO, ORANGE, PAYAPA, PEACH, PEAR,
				PERSIMMON, PLUM, POMEGRANATE, STRAWBERRY, TOMATO));
	}

	private static Item registerItem(String id, Item item) {
		return Registry.register(Registry.ITEM, new Identifier(MODID, id), item);
	}

	public static void register() {
	}

}
