package cpp.init;

import cpp.Craftingpp;
import cpp.item.CppFoodOrPotion;
import cpp.block.FlowerGrass1Block;
import cpp.block.entity.AllInOneMachineBlockEntity.Degree;
import cpp.item.*;
import cpp.vaccine.Vaccines;
import net.minecraft.block.Block;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.UseAction;
import net.minecraft.util.registry.Registry;

import static cpp.Craftingpp.*;
import static cpp.init.CppBlocks.*;

@SuppressWarnings("unused")
public final class CppItems {
	// 稀有物品
	public static final Item SHARD_OF_THE_DARKNESS = registerItem("shard_of_the_darkness", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item WING_OF_SKY = registerItem("wing_of_sky", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item HEART_OF_CRYSTAL = registerItem("heart_of_crystal", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item LIMB_OF_RIDGE = registerItem("limb_of_ridge", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item SOUL_OF_DIRT = registerItem("soul_of_dirt", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item CERTIFICATION_OF_EARTH = registerItem("certification_of_earth", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item NOVA_OF_FIRE = registerItem("nova_of_fire", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item SPIRIT_OF_LIFE = registerItem("spirit_of_life", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	// 插件
	public static final Item EMPTY_PLUGIN = registerItem("empty_plugin", new Item(new Item.Settings().group(CPP_GROUP_MACHINE).maxCount(16)));
	public static final Item LOW_TEMPERATURE_PLUGIN = registerItem("low_temperature_plugin", new AllInOneMachinePlugin(Degree.LOW, true, new Item.Settings().group(CPP_GROUP_MACHINE).maxCount(1)));
	public static final Item LOW_PRESSURE_PLUGIN = registerItem("low_pressure_plugin", new AllInOneMachinePlugin(Degree.LOW, false, new Item.Settings().group(CPP_GROUP_MACHINE).maxCount(1)));
	public static final Item HIGH_TEMPERATURE_PLUGIN = registerItem("high_temperature_plugin", new AllInOneMachinePlugin(Degree.HIGH, true, new Item.Settings().group(CPP_GROUP_MACHINE).maxCount(1)));
	public static final Item HIGH_PRESSURE_PLUGIN = registerItem("high_pressure_plugin", new AllInOneMachinePlugin(Degree.HIGH, false, new Item.Settings().group(CPP_GROUP_MACHINE).maxCount(1)));
	public static final Item COBBLESTONE_PLUGIN = registerItem("cobblestone_plugin", new Item(new Item.Settings().group(CPP_GROUP_MACHINE).maxCount(1)));
	public static final Item STONE_PLUGIN = registerItem("stone_plugin", new Item(new Item.Settings().group(CPP_GROUP_MACHINE).maxCount(1)));
	public static final Item DEEPSLATE_PLUGIN = registerItem("deepslate_plugin", new Item(new Item.Settings().group(CPP_GROUP_MACHINE).maxCount(1)));
	public static final Item BLACKSTONE_PLUGIN = registerItem("blackstone_plugin", new Item(new Item.Settings().group(CPP_GROUP_MACHINE).maxCount(1)));
	public static final Item NETHERRACK_PLUGIN = registerItem("netherrack_plugin", new Item(new Item.Settings().group(CPP_GROUP_MACHINE).maxCount(1)));
	public static final Item END_STONE_PLUGIN = registerItem("end_stone_plugin", new Item(new Item.Settings().group(CPP_GROUP_MACHINE).maxCount(1)));
	public static final Item BASALT_PLUGIN = registerItem("basalt_plugin", new Item(new Item.Settings().group(CPP_GROUP_MACHINE).maxCount(1)));
	public static final Item MENDING_PLUGIN = registerItem("mending_plugin", new Item(new Item.Settings().group(CPP_GROUP_MACHINE).maxCount(1)));
	public static final Item WIFI_PLUGIN = registerItem("wifi_plugin", new Item(new Item.Settings().group(CPP_GROUP_MACHINE).maxCount(1)));
	public static final Item EMERALD_TRADE_PLUGIN = registerItem("emerald_trade_plugin", new TradePlugin(new Item.Settings().group(CPP_GROUP_MACHINE).rarity(Rarity.UNCOMMON).maxCount(1)));
	public static final Item GOLD_TRADE_PLUGIN = registerItem("gold_trade_plugin", new TradePlugin(new Item.Settings().group(CPP_GROUP_MACHINE).rarity(Rarity.UNCOMMON).maxCount(1)));
	public static final Item MOON_TRADE_PLUGIN = registerItem("moon_trade_plugin", new TradePlugin(new Item.Settings().group(CPP_GROUP_MACHINE).rarity(Rarity.UNCOMMON).maxCount(1)));
	// 上古卷轴
	public static final Item ANCIENT_SCROLL = registerItem("ancient_scroll", new AncientScroll(new Item.Settings().group(CPP_GROUP_MISC).rarity(Rarity.UNCOMMON)));
	// 附魔金属
	public static final Item ENCHANTED_IRON = registerItem("enchanted_iron", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item ENCHANTED_GOLD = registerItem("enchanted_gold", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item ENCHANTED_DIAMOND = registerItem("enchanted_diamond", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	// 材料
	public static final Item COAL_NUGGET = registerItem("coal_nugget", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item CINDER = registerItem("cinder", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item SPLINT = registerItem("splint", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item SILICON_PLATE = registerItem("silicon_plate", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item MOON_SHARD = registerItem("moon_shard", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item SUN_SHARD = registerItem("sun_shard", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item CLAY_BUCKET = registerItem("clay_bucket", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	// 粉末和瓶装物
	public static final Item COPPER_DUST = registerItem("copper_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item IRON_DUST = registerItem("iron_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item GOLD_DUST = registerItem("gold_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item CARBON_DUST = registerItem("carbon_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item EMERALD_DUST = registerItem("emerald_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item DIAMOND_DUST = registerItem("diamond_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item QUARTZ_DUST = registerItem("quartz_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item RARE_EARTH_SALT = registerItem("rare_earth_salt", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item ALKALOID_RARE_EARTH = registerItem("alkaloid_rare_earth", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item RARE_EARTH_DUST = registerItem("rare_earth_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item COARSE_SILICON = registerItem("coarse_silicon", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item SILICON_DUST = registerItem("silicon_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item STEEL_DUST = registerItem("steel_dust", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item FERTILIZER = registerItem("fertilizer", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item BOTTLE_OF_SALT = registerItem("bottle_of_salt", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item ACID = registerItem("acid", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item SODA_WATER = registerItem("soda_water", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item BIONIC_ACID = registerItem("bionic_acid", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item ALKALOID = registerItem("alkaloid", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item BOTTLE_OF_AIR = registerItem("bottle_of_air", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	public static final Item AMMONIA_REFRIGERANT = registerItem("ammonia_refrigerant", new Item(new Item.Settings().group(CPP_GROUP_MISC)));
	// 工具
	public static final Item BLUE_FORCE_OF_SKY = registerItem("blue_force_of_sky", new BlueForceOfSky(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item GREEN_FORCE_OF_WATER = registerItem("green_force_of_water", new GreenForceOfWater(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item CYAN_FORCE_OF_MOUNTAIN = registerItem("cyan_force_of_mountain", new CyanForceOfMountain(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item ORANGE_FORCE_OF_DIRT = registerItem("orange_force_of_dirt", new OrangeForceOfDirt(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item YELLOW_FORCE_OF_EARTH = registerItem("yellow_force_of_earth", new YellowForceOfEarth(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item RED_FORCE_OF_FIRE = registerItem("red_force_of_fire", new RedForceOfFire(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item PURPLE_FORCE_OF_LIFE = registerItem("purple_force_of_life", new PurpleForceOfLife(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item WHITE_FORCE_OF_LIGHTNING = registerItem("white_force_of_lightning", new WhiteForceOfLightning(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item BLACK_FORCE_OF_MOON = registerItem("black_force_of_moon", new BlackForceOfMoon(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item PORTABLE_CRAFTING_TABLE = registerItem("portable_crafting_table", new PortableCraftingTable(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item PORTABLE_CRAFTING_MACHINE = registerItem("portable_crafting_machine", new PortableCraftingMachine(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item MAGNET = registerItem("magnet", new Magnet(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item TIME_CHECKER = registerItem("time_checker", new TimeChecker(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item TIME_CONDITIONER = registerItem("time_conditioner", new TimeConditioner(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item ORIGIN_OF_THE_WORLD = registerItem("origin_of_the_world", new OriginOfTheWorld(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item COMPRESSOR = registerItem("compressor", new Compressor(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item ELDER_S_WORDS = registerItem("elder_s_words", new Item(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item COORDINATE_RECORDER = registerItem("coordinate_recorder", new CoordinateRecorder(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item MUFFLER = registerItem("muffler", new Muffler(new Item.Settings().group(CPP_GROUP_TOOL)));
	public static final Item SACHET = registerItem("sachet", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
	public static final Item GRAFTER = registerItem("grafter", new Grafter(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1).maxDamage(66)));
	public static final Item GLASS_PICKAXE = registerItem("glass_pickaxe", new GlassPickaxe(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item CHEST_TRANSPORTER = registerItem("chest_transporter", new ChestTransporter(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1).maxDamage(26)));
	public static final Item FIRECRACKERS = registerItem("firecrackers", new Firecrackers(new Item.Settings().group(CPP_GROUP_TOOL)));
	public static final Item BROOM = registerItem("broom", new Item(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item SHOOTING_STAR = registerItem("shooting_star", new Item(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item INDUSTRIOUS_HAND = registerItem("industrious_hand", new IndustriousHand(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item BREAK_HAND = registerItem("break_hand", new BreakHand(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item SMART_HAND = registerItem("smart_hand", new SmartHand(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item ANGRY_HAND = registerItem("angry_hand", new AngryHand(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item TOUGHEN_HAND = registerItem("toughen_hand", new ToughenHand(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item GOLEM = registerItem("golem", new Item(new Item.Settings().group(CPP_GROUP_TOOL)));
	public static final Item GOLEM_FARMER = registerItem("golem_farmer", new Golem(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1), CppEntities.GOLEM_FARMER));
	public static final Item GOLEM_MINER = registerItem("golem_miner", new Golem(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1), CppEntities.GOLEM_MINER));
	public static final Item GOLEM_FISHER = registerItem("golem_fisher", new Golem(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1), CppEntities.GOLEM_FISHER));
	public static final Item GOLEM_WARRIOR = registerItem("golem_warrior", new Golem(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1), CppEntities.GOLEM_WARRIOR));
	public static final Item GOLEM_HERDER = registerItem("golem_herder", new Golem(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1), CppEntities.GOLEM_HERDER));
	public static final Item SANTA_GIFT = registerItem("santa_gift", new SantaGift(new Item.Settings().group(CPP_GROUP_TOOL)));
	public static final Item COLOR_PALETTE = registerItem("color_palette", new ColorPalette(new Item.Settings().group(CPP_GROUP_TOOL)));
	public static final Item DYE_STICK = registerItem("dye_stick", new DyeStick(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	// 食物
	public static final Item KETCHUP = registerItem("ketchup", new CppFoodOrPotion(UseAction.EAT, 0, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
	public static final Item STRAWBERRY_JAM = registerItem("strawberry_jam", new CppFoodOrPotion(UseAction.EAT, -2, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
	public static final Item BLUE_BERRY_JAM = registerItem("blue_berry_jam", new CppFoodOrPotion(UseAction.EAT, -2, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
	public static final Item ORANGE_JAM = registerItem("orange_jam", new CppFoodOrPotion(UseAction.EAT, -2, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
	public static final Item CHERRY_JAM = registerItem("cherry_jam", new CppFoodOrPotion(UseAction.EAT, -2, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
	public static final Item APPLE_JAM = registerItem("apple_jam", new CppFoodOrPotion(UseAction.EAT, -2, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
	public static final Item YEAST_POWDER = registerItem("yeast_powder", new CppFoodOrPotion(UseAction.EAT, 0, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
	public static final Item BUTTER = registerItem("butter", new CppFoodOrPotion(UseAction.EAT, 2, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
	public static final Item CHEESE = registerItem("cheese", new CppFoodOrPotion(UseAction.EAT, 2, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
	public static final Item RAISIN = registerItem("raisin", new CppFoodOrPotion(UseAction.EAT, -4, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 0), 1.0F).build())));
	public static final Item TOFFEE_APPLE = registerItem("toffee_apple", new CppFoodOrPotion(UseAction.EAT, 0, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())));
	public static final Item CANDIED_HAW = registerItem("candied_haw", new CppFoodOrPotion(UseAction.EAT, -8, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build()).maxCount(16)));
	public static final Item CUPCAKE = registerItem("cupcake", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).build())));
	public static final Item CAKE_ROLL = registerItem("cake_roll", new CppFoodOrPotion(UseAction.EAT, -3, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).build())));
	public static final Item CROISSANT = registerItem("croissant", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).build())));
	public static final Item CARAMEL_PUDDING = registerItem("caramel_pudding", new CppFoodOrPotion(UseAction.EAT, 3, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build()).maxCount(16)));
	public static final Item DOUGHNUT = registerItem("doughnut", new CppFoodOrPotion(UseAction.EAT, 3, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(7).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())));
	public static final Item PURPLE_DOUGHNUT = registerItem("purple_doughnut", new CppFoodOrPotion(UseAction.EAT, 3, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(7).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())));
	public static final Item PINK_DOUGHNUT = registerItem("pink_doughnut", new CppFoodOrPotion(UseAction.EAT, 3, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(7).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())));
	public static final Item WHITE_DOUGHNUT = registerItem("white_doughnut", new CppFoodOrPotion(UseAction.EAT, 3, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(7).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())));
	public static final Item BLUE_DOUGHNUT = registerItem("blue_doughnut", new CppFoodOrPotion(UseAction.EAT, 3, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(7).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())));
	public static final Item GREEN_DOUGHNUT = registerItem("green_doughnut", new CppFoodOrPotion(UseAction.EAT, 3, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(7).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build())));
	public static final Item HONEY_PANCAKE = registerItem("honey_pancake", new CppFoodOrPotion(UseAction.EAT, 3, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build()).maxCount(16)));
	public static final Item BOILED_EGG = registerItem("boiled_egg", new CppFoodOrPotion(UseAction.EAT, 2, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.3F).build())));
	public static final Item BAKED_CARROT = registerItem("baked_carrot", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).build())));
	public static final Item BACON = registerItem("bacon", new CppFoodOrPotion(UseAction.EAT, 8, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F).build())));
	public static final Item VEGETABLE_KEBAB = registerItem("vegetable_kebab", new CppFoodOrPotion(UseAction.EAT, -8, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 600, 0), 1.0F).build()).maxCount(16)));
	public static final Item BEEF_KEBAB = registerItem("beef_kebab", new CppFoodOrPotion(UseAction.EAT, 8, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(16).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build()).maxCount(16)));
	public static final Item PORK_KEBAB = registerItem("pork_kebab", new CppFoodOrPotion(UseAction.EAT, 8, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(16).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build()).maxCount(16)));
	public static final Item LAMB_KEBAB = registerItem("lamb_kebab", new CppFoodOrPotion(UseAction.EAT, 6, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(14).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build()).maxCount(16)));
	public static final Item CHICKEN_KEBAB = registerItem("chicken_kebab", new CppFoodOrPotion(UseAction.EAT, 6, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(14).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build()).maxCount(16)));
	public static final Item RAW_COLORFUL_VEGETABLE = registerItem("raw_colorful_vegetable", new CppFoodOrPotion(UseAction.EAT, -8, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F).build()).maxCount(1)));
	public static final Item RAW_BRAISED_PORK_WITH_POTATOES = registerItem("raw_braised_pork_with_potatoes", new CppFoodOrPotion(UseAction.EAT, 0, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F).build()).maxCount(1)));
	public static final Item RAW_BRAISED_BEEF_WITH_POTATOES = registerItem("raw_braised_beef_with_potatoes", new CppFoodOrPotion(UseAction.EAT, 0, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F).build()).maxCount(1)));
	public static final Item RAW_RABBIT_STEW = registerItem("raw_rabbit_stew", new CppFoodOrPotion(UseAction.EAT, 0, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F).build()).maxCount(1)));
	public static final Item COLORFUL_VEGETABLE = registerItem("colorful_vegetable", new CppFoodOrPotion(UseAction.EAT, -12, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(14).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 600, 0), 1.0F).build()).maxCount(1)));
	public static final Item BRAISED_PORK_WITH_POTATOES = registerItem("braised_pork_with_potatoes", new CppFoodOrPotion(UseAction.EAT, 0, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(16).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build()).maxCount(1)));
	public static final Item BRAISED_BEEF_WITH_POTATOES = registerItem("braised_beef_with_potatoes", new CppFoodOrPotion(UseAction.EAT, 0, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(16).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build()).maxCount(1)));
	public static final Item RABBIT_STEW = registerItem("rabbit_stew", new CppFoodOrPotion(UseAction.EAT, 0, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(16).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build()).maxCount(1)));
	public static final Item DUMPLING = registerItem("dumpling", new CppFoodOrPotion(UseAction.EAT, -1, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(8).saturationModifier(0.3F).build())));
	public static final Item MEAT_FLOSS_BREAD = registerItem("meat_floss_bread", new CppFoodOrPotion(UseAction.EAT, 3, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build()).maxCount(16)));
	public static final Item CHRISTMAS_ROAST_CHICKEN = registerItem("christmas_roast_chicken", new CppFoodOrPotion(UseAction.EAT, 6, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(16).saturationModifier(0.3F).build()).maxCount(8)));
	public static final Item FISH_AND_CHIPS = registerItem("fish_and_chips", new CppFoodOrPotion(UseAction.EAT, 0, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(16).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build()).maxCount(8)));
	public static final Item BURGER = registerItem("burger", new CppFoodOrPotion(UseAction.EAT, 0, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(20).saturationModifier(0.3F).build()).maxCount(8)));
	public static final Item PIZZA = registerItem("pizza", new CppFoodOrPotion(UseAction.EAT, 0, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).build())));
	public static final Item SUSHI = registerItem("sushi", new CppFoodOrPotion(UseAction.EAT, 0, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).build())));
	public static final Item QINGTUAN = registerItem("qingtuan", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).build())));
	public static final Item SAKURA_MOCHI = registerItem("sakura_mochi", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).build())));
	public static final Item ZONGZI = registerItem("zongzi", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(9).saturationModifier(0.3F).build()).maxCount(16)));
	public static final Item ZONGZI_WITH_EGG_YOLK = registerItem("zongzi_with_egg_yolk", new CppFoodOrPotion(UseAction.EAT, -3, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(9).saturationModifier(0.3F).build()).maxCount(16)));
	public static final Item ZONGZI_WITH_HONEY_DATES = registerItem("zongzi_with_honey_dates", new CppFoodOrPotion(UseAction.EAT, -8, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(9).saturationModifier(0.3F).build()).maxCount(16)));
	public static final Item ZONGZI_WITH_PORK = registerItem("zongzi_with_pork", new CppFoodOrPotion(UseAction.EAT, 3, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(9).saturationModifier(0.3F).build()).maxCount(16)));
	public static final Item ZONGZI_WITH_SALT = registerItem("zongzi_with_salt", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(9).saturationModifier(0.3F).build()).maxCount(16)));
	public static final Item ZONGZI_WITH_SODA = registerItem("zongzi_with_soda", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(9).saturationModifier(0.3F).build()).maxCount(16)));
	public static final Item ZONGZI_WITH_HONEY = registerItem("zongzi_with_honey", new CppFoodOrPotion(UseAction.EAT, -2, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(9).saturationModifier(0.3F).build()).maxCount(16)));
	public static final Item SANDWICH = registerItem("sandwich", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build()).maxCount(16)));
	public static final Item STARGAZY_PIE = registerItem("stargazy_pie", new CppFoodOrPotion(UseAction.EAT, 0, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(16).saturationModifier(0.3F).build()).maxCount(8)));
	public static final Item BAKED_SHROOMLIGHT = registerItem("baked_shroomlight", new CppFoodOrPotion(UseAction.EAT, -2, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 600, 0), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600, 0), 1.0F).build())));
	public static final Item SOUL_FRIED_CHICKEN = registerItem("soul_fried_chicken", new CppFoodOrPotion(UseAction.EAT, 5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(10).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 600, 0), 1.0F).build()).maxCount(16)));
	public static final Item LUXURIOUS_NETHERITE_BARBECUE = registerItem("luxurious_netherite_barbecue", new CppFoodOrPotion(UseAction.EAT, 15, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(20).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0F).build()).maxCount(1)));
	public static final Item ASSORTED_ROOT_CANDY = registerItem("assorted_root_candy", new CppFoodOrPotion(UseAction.EAT, -2, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(5).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), 1.0F).build()).maxCount(16)));
	public static final Item NETHERITE_MUSHROOM_STEW = registerItem("netherite_mushroom_stew", new CppFoodOrPotion(UseAction.EAT, -6, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(12).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0), 1.0F).build()).maxCount(1)));
	public static final Item DOUBLE_COLOR_VINE_SALAD = registerItem("double_color_vine_salad", new CppFoodOrPotion(UseAction.EAT, -6, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 600, 0), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0), 1.0F).build()).maxCount(1)));
	public static final Item COLD_DRINK = registerItem("cold_drink", new CppFoodOrPotion(UseAction.EAT, 0, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).build()).maxCount(16)));
	// 水果
	public static final Item APRICOT = registerItem("apricot", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item BANANA = registerItem("banana", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item BLUEBERRY = registerItem("blueberry", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item CHERRY = registerItem("cherry", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item CHINESE_DATE = registerItem("chinese_date", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item COCONUT = registerItem("coconut", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item GOLDEN_GRAPE = registerItem("golden_grape", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item GRAPE = registerItem("grape", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item GRAPEFRUIT = registerItem("grapefruit", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item HAWTHORN = registerItem("hawthorn", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item LEMON = registerItem("lemon", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item LONGAN = registerItem("longan", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item LOQUAT = registerItem("loquat", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item LYCHEE = registerItem("lychee", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item MANGO = registerItem("mango", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item ORANGE = registerItem("orange", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item PAYAPA = registerItem("payapa", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item PEACH = registerItem("peach", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item PEAR = registerItem("pear", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item PERSIMMON = registerItem("persimmon", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item PLUM = registerItem("plum", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item POMEGRANATE = registerItem("pomegranate", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item STRAWBERRY = registerItem("strawberry", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item TOMATO = registerItem("tomato", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	public static final Item CITRUS = registerItem("citrus", new CppFoodOrPotion(UseAction.EAT, -5, new Item.Settings().group(CPP_GROUP_FOOD).food((new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build())));
	// 药剂
	public static final Item AGENTIA_OF_LIGHTNESS = registerItem("agentia_of_lightness", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 4800, 0), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_EYESIGHT = registerItem("agentia_of_eyesight", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 9600, 0), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_FIRE_SHIELD = registerItem("agentia_of_fire_shield", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 9600, 0), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_WATERLESS = registerItem("agentia_of_waterless", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 9600, 0), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_TRANSPARENTNESS = registerItem("agentia_of_transparentness", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 9600, 0), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_BOUNCE = registerItem("agentia_of_bounce", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 4800, 1), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_AGILENESS = registerItem("agentia_of_agileness", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 4800, 1), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_SHARPNESS = registerItem("agentia_of_sharpness", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 4800, 1), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_REJUVENESS = registerItem("agentia_of_rejuveness", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 2400, 1), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_BLOOD = registerItem("agentia_of_blood", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 2), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_EXTREMENESS = registerItem("agentia_of_extremeness", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 4800, 1), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_SHIELD = registerItem("agentia_of_shield", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 4800, 1), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_TIDE = registerItem("agentia_of_tide", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 4800, 0), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_CHAIN = registerItem("agentia_of_chain", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(CppEffects.CHAIN, 9600, 0), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_SKY = registerItem("agentia_of_sky", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 7200, 1), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 7200, 0), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_OCEAN = registerItem("agentia_of_ocean", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 14400, 0), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 7200, 0), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_RIDGE = registerItem("agentia_of_ridge", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 7200, 1), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 14400, 0), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_DIRT = registerItem("agentia_of_dirt", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 14400, 0), 1.0F).statusEffect(new StatusEffectInstance(CppEffects.CHAIN, 14400, 0), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_EARTH = registerItem("agentia_of_earth", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 7200, 1), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 7200, 1), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_FIRE = registerItem("agentia_of_fire", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 14400, 0), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 7200, 1), 1.0F).build()).maxCount(16)));
	public static final Item AGENTIA_OF_LIFE = registerItem("agentia_of_life", new CppFoodOrPotion(UseAction.DRINK, 0, new Item.Settings().group(CPP_GROUP_MISC).food((new FoodComponent.Builder()).hunger(6).saturationModifier(0.3F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 3600, 1), 1.0F).statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 3), 1.0F).build()).maxCount(16)));
	// 疫苗
	public static final Item VACCINE_OF_POISON = registerItem("vaccine_of_poison", new VaccineItem(Vaccines.POISON, new Item.Settings().group(CPP_GROUP_MISC).maxCount(16)));
	public static final Item VACCINE_OF_BLINDNESS = registerItem("vaccine_of_blindness", new VaccineItem(Vaccines.BLINDNESS, new Item.Settings().group(CPP_GROUP_MISC).maxCount(16)));
	public static final Item VACCINE_OF_MINING_FATIGUE = registerItem("vaccine_of_mining_fatigue", new VaccineItem(Vaccines.MINING_FATIGUE, new Item.Settings().group(CPP_GROUP_MISC).maxCount(16)));
	public static final Item VACCINE_OF_WITHER = registerItem("vaccine_of_wither", new VaccineItem(Vaccines.WITHER, new Item.Settings().group(CPP_GROUP_MISC).maxCount(16)));
	public static final Item VACCINE_OF_DARKNESS = registerItem("vaccine_of_darkness", new VaccineItem(Vaccines.DARKNESS, new Item.Settings().group(CPP_GROUP_MISC).maxCount(16)));
	// 植物
	public static final Item LYCORIS_RADIATA_SEEDS = registerSeeds(LYCORIS_RADIATA);
	public static final Item TRIFOLIUM_SEEDS = registerSeeds(TRIFOLIUM);
	public static final Item BLACKTHORN_SEEDS = registerSeeds(BLACKTHORN);
	public static final Item CATTAIL_SEEDS = registerSeeds(CATTAIL);
	public static final Item MARIGOLD_SEEDS = registerSeeds(MARIGOLD);
	public static final Item HIBISCUS_SEEDS = registerSeeds(HIBISCUS);
	public static final Item HYACINTH_SEEDS = registerSeeds(HYACINTH);
	public static final Item CALAMUS_SEEDS = registerSeeds(CALAMUS);
	public static final Item WILD_LILIUM_SEEDS = registerSeeds(WILD_LILIUM);
	public static final Item BAUHINIA_SEEDS = registerSeeds(BAUHINIA);
	public static final Item FLUFFY_GRASS_SEEDS = registerSeeds(FLUFFY_GRASS);
	public static final Item GERBERA_SEEDS = registerSeeds(GERBERA);
	public static final Item ESPARTO_SEEDS = registerSeeds(ESPARTO);
	public static final Item GLOW_FORSYTHIA_SEEDS = registerSeeds(GLOW_FORSYTHIA);
	public static final Item GLAZED_SHADE_SEEDS = registerSeeds(GLAZED_SHADE);
	public static final Item STELERA_SEEDS = registerSeeds(STELERA);
	public static final Item FORAGE_CRYSTAL_SEEDS = registerSeeds(FORAGE_CRYSTAL);
	public static final Item ISORCHID_SEEDS = registerSeeds(ISORCHID);
	public static final Item BURNING_CHRYSANTHE_SEEDS = registerSeeds(BURNING_CHRYSANTHE);
	public static final Item OXALIS_SEEDS = registerSeeds(OXALIS);
	public static final Item RICE_SEEDS = registerItem("rice_seed", new BlockItem(CppBlocks.RICE, new Item.Settings().group(CPP_GROUP_PLANT)));
	// 仪式和魔法
	public static final Item SEALING_WAND = registerItem("sealing_wand", new Wand(1, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item STAR_WAND = registerItem("star_wand", new Wand(2, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item DREAM_WAND = registerItem("dream_wand", new Wand(3, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item TEMPERANCER = registerItem("temperancer", new Temperancer(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item WAND_OF_THE_DARKNESS = registerItem("wand_of_the_darkness", new Wand(16, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	// 装饰-装备
	public static final Item RED_LIP = registerItem("red_lip", new WearableItem(EquipmentSlot.HEAD, false, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item PURPLE_EYE = registerItem("purple_eye", new WearableItem(EquipmentSlot.HEAD, false, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));//TODO 佩戴后，高亮附近的野生花草作物
	public static final Item LASH = registerItem("lash", new WearableItem(EquipmentSlot.HEAD, false, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item CAT_BREED = registerItem("cat_breed", new WearableItem(EquipmentSlot.HEAD, false, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item GARLAND = registerItem("garland", new WearableItem(EquipmentSlot.HEAD, true, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item BLACK_FRAMED_GLASSES = registerItem("black_framed_glasses", new WearableItem(EquipmentSlot.HEAD, true, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item ORANGE_FRAMED_GLASSES = registerItem("orange_framed_glasses", new WearableItem(EquipmentSlot.HEAD, true, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item JOKING_GLASSES = registerItem("joking_glasses", new WearableItem(EquipmentSlot.HEAD, true, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item MINION_GOGGLES = registerItem("minion_goggles", new WearableItem(EquipmentSlot.HEAD, true, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item PANTS = registerItem("pants", new WearableItem(EquipmentSlot.HEAD, true, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item EMPIRE_HAT = registerItem("empire_hat", new WearableItem(EquipmentSlot.HEAD, false, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item GLASS_HELMET = registerItem("glass_helmet", new WearableItem(EquipmentSlot.HEAD, true, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));//TODO 当玩家不在水中时，提供5秒的水下呼吸
	public static final Item GLOW_HAT = registerItem("glow_hat", new WearableItem(EquipmentSlot.HEAD, true, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));//TODO 戴上后可使玩家发光
	public static final Item GREEN_HAT = registerItem("green_hat", new WearableItem(EquipmentSlot.HEAD, true, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	public static final Item BLACK_HAT = registerItem("black_hat", new WearableItem(EquipmentSlot.HEAD, true, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));//TODO -1幸运
	public static final Item NURSE_HAT = registerItem("nurse_hat", new WearableItem(EquipmentSlot.HEAD, true, new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));//TODO +1幸运
	public static final Item SNOW_BOOTS = registerItem("snow_boots", new SnowBoots(new Item.Settings().group(CPP_GROUP_TOOL).maxCount(1)));
	// 装饰-杂
	public static final Item CLASSICAL_PAINTING = registerItem("classical_painting", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item SPRING_FESTIVAL_DECORATIONS = registerItem("spring_festival_decorations", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	// 纸片人
	public static final Item CHARACTER = registerItem("character", new CharacterItem(new Item.Settings().group(CPP_GROUP_DECORATE)));
	// 标志和图案
	public static final Item CHINA_FLAG = registerItem("china_flag", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item USA_FLAG = registerItem("usa_flag", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item RUSSIA_FLAG = registerItem("russia_flag", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item UK_FLAG = registerItem("uk_flag", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item FRANCE_FLAG = registerItem("france_flag", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item BILIBILI_LOGO = registerItem("bilibili_logo", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item GITHUB_LOGO = registerItem("github_logo", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item MCMOD_LOGO = registerItem("mcmod_logo", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item TCP_LOGO = registerItem("tcp_logo", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item CBL_LOGO = registerItem("cbl_logo", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item NF_LOGO = registerItem("nf_logo", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	// 告示牌
	public static final Item WHITE_SIGN = registerItem("white_sign", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item ORANGE_SIGN = registerItem("orange_sign", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item MAGENTA_SIGN = registerItem("magenta_sign", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item LIGHT_BLUE_SIGN = registerItem("light_blue_sign", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item YELLOW_SIGN = registerItem("yellow_sign", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item LIME_SIGN = registerItem("lime_sign", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item PINK_SIGN = registerItem("pink_sign", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item GRAY_SIGN = registerItem("gray_sign", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item LIGHT_GRAY_SIGN = registerItem("light_gray_sign", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item CYAN_SIGN = registerItem("cyan_sign", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item PURPLE_SIGN = registerItem("purple_sign", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item BLUE_SIGN = registerItem("blue_sign", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item BROWN_SIGN = registerItem("brown_sign", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item GREEN_SIGN = registerItem("green_sign", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item RED_SIGN = registerItem("red_sign", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item BLACK_SIGN = registerItem("black_sign", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));
	public static final Item GLASS_SIGN = registerItem("glass_sign", new Item(new Item.Settings().group(CPP_GROUP_DECORATE)));

	public static final Item RECIPE_CREATOR = registerItem("recipe_creator", new RecipeCreator());

	public static final Item COMPRESSED_ITEM = registerItem("compressed_item", new CompressedItem(new Item.Settings()));

	static <T extends BlockItem> T register(T blockItem) {
		return Registry.register(Registry.ITEM, Registry.BLOCK.getId(blockItem.getBlock()), blockItem);
	}
	private static Item registerItem(String id, Item item) {
		return Registry.register(Registry.ITEM, new Identifier(Craftingpp.MOD_ID3, id), item);
	}

	private static Item registerSeeds(Block block) {
		Item item = Registry.register(Registry.ITEM, new Identifier(Craftingpp.MOD_ID3, Registry.BLOCK.getId(block).getPath() + "_seed"), new BlockPlacerItem(block, new Item.Settings().group(CPP_GROUP_PLANT)));
		((FlowerGrass1Block) block).setSeed(item);
		return item;
	}

	public static void init() {}

}
