package net.cpp.init;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.cpp.Craftingpp.CPP_GROUP;

public class CppItems {

    public static final String modId = "cpp";
    // 插件
    public static Item EMPTY_PLUGIN = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item LOW_TEMPERATURE_PLUGIN = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item LOW_PRESSURE_PLUGIN = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item HIGH_TEMPERATURE_PLUGIN = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item HIGH_PRESSURE_PLUGIN = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item COBBLESTONE_PLUGIN = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item STONE_PLUGIN = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BLACK_STONE_PLUGIN = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item NETHERRACH_PLUGIN = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item END_STONE_PLUGIN = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BASALT_PLUGIN = new Item(new Item.Settings().group(CPP_GROUP));
    // 上古卷轴
    public static Item ANCIENT_SCROLL = new Item(new Item.Settings().group(CPP_GROUP));
    // 附魔金属
    public static Item ENCHANTED_IRON = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ENCHANTED_GOLD = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ENCHANTED_DIAMOND = new Item(new Item.Settings().group(CPP_GROUP));
    // 材料
    public static Item COAL_NUGGET = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item CINDER = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item SPLINT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item RARE_EARTH_GLASS = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item REINFORCED_GLASS = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item SILICON_PLATE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item MOON_SHARD = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item SUN_SHARD = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item MOON_STONE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item SUN_STONE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item CLAY_BUCKET = new Item(new Item.Settings().group(CPP_GROUP));
    // 粉末和瓶装物
    public static Item COPPER_DUST = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item IRON_DUST = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item GOLD_DUST = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item CARBON_DUST = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item EMERALD_DUST = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item DIAMOND_DUST = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item QUARTZ_DUST = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item RARE_EARTH_SALT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ALKALOID_RARE_EARTH = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item RARE_EARTH_DUST = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item COARSE_SILICON = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item SILICON_DUST = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item STEEL_DUST = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item FERTILIZER = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BOTTLE_OF_SALT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ACID = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item SODA_WATER = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BIONIC_ACID = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ALKALOID = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BOTTLE_OF_AIR = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item AMMONIA_REFRIGERANT = new Item(new Item.Settings().group(CPP_GROUP));
    // 工具
    public static Item BLUE_FORCE_OF_SKY = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item GREEN_FORCE_OF_WATER = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item CYAN_FORCE_OF_MOUNTAIN = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ORANGE_FORCE_OF_DIRT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item YELLOW_FORCE_OF_EARTH = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item RED_FORCE_OF_FIRE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item PURPLE_FORCE_OF_LIFE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item WHITE_FORCE_OF_LIGHTNING = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BLACK_FORCE_OF_MOON = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item PORTABLE_CRAFTING_TABLE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item PORTABLE_CRAFTING_MACHINE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item MAGNET = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item TIME_CHECKER = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item TIME_CONDITIONER = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ORIGIN_OF_THE_WORLD = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item COMPRESSOR = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ELDER_S_WORDS = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item MUFFLER = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item SACHET = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item GRAFTER = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item GLASS_PICKAXE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item CHEST_TRANSPORTER = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item FIRECRACKERS = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BROOM = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item SHOOTING_STAR = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item INDUSTRIOUS_HAND = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BREAK_HAND = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item SMART_HAND = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ANGRY_HAND = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item TOUGHEN_HAND = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item GOLEM = new Item(new Item.Settings().group(CPP_GROUP));;
    public static Item INDUSTRIOUS_GOLEM = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BREAK_GOLEM = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item SMART_GOLEM = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ANGRY_GOLEM = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item TOUGHEN_GOLEM = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item SANTA_GIFT = new Item(new Item.Settings().group(CPP_GROUP));
    // 食物
    public static Item KETCHUP = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item STRAWBERRY_JAM = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BLUE_BERRY_JAM = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ORANGE_JAM = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item CHERRY_JAM = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item APPLE_JAM = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item YEAST_POWDER = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BUTTER = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item CHEESE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item RAISIN = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item TOFFEE_APPLE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item CANDIED_HAW = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item CUPCAKE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item CAKE_ROLL = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item CROISSANT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item CARAMEL_PUDDING = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item DOUGHNUT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item PURPLE_DOUGHNUT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item PINK_DOUGHNUT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item WHITE_DOUGHNUT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BLUE_DOUGHNUT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item GREEN_DOUGHNUT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item HONEY_PANCAKE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BOILED_EGG = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BACKED_CARROT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BACON = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item VEGETABLE_KEBAB = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BEEF_KEBAB = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item PORK_KEBAB = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item LAMB_KEBAB = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item CHICKEN_KEBAB = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item RAW_COLORFUL_VEGETABLE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item RAW_BRAISED_PORK_WITH_POTATOES = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item RAW_BRAISED_BEEF_WITH_POTATOES = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item RAW_RABBIT_STEW = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item COLORFUL_VEGETABLE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BRAISED_PORK_WITH_POTATOES = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BRAISED_BEEF_WITH_POTATOES = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item RABBIT_STEW = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item DUMPLING = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item MEAT_FLOSS_BREAD = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item CHRISTMAS_ROAST_CHICKEN = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item FISH_AND_CHIPS = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BURGER = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item PIZZA = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item SUSHI = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item QINGTUAN = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item SAKURA_MOCHI = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ZONGZI = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ZONGZI_BOILED_EGG = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ZONGZI_CHINESE_DATA = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ZONGZI_COOKED_PORKCHOP = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ZONGZI_SALT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ZONGZI_SODA_WATER = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ZONGZI_HONEY_BOTTLE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item SANDWICH = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item STARGAZY_PIE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BAKED_SHROOMLIGHT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item SOUL_FIRED_CHICKEN = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item LUXURIOUS_NETHERITE_BARBECUE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ASSORTED_ROOT_CANDY = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item NETHERITE_MUSHROOM_STEW = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item DOUBLE_COLOR_VINE_SALAD = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item COLD_DRINK = new Item(new Item.Settings().group(CPP_GROUP));
    // 水果
    public static Item APRICOT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BANANA = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item BLUEBERRY = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item CHERRY = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item CHINESE_DATE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item COCONUT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item GOLDEN_GRAPE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item GRAPE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item GRAPEFRUIT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item HAWTHORN = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item LEMON = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item LONGAN = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item LOQUAT = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item LYCHEE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item MANGO = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item ORANGE = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item PAYAPA = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item PEACH = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item PEAR = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item PERSIMMON = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item PLUM = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));
    public static Item  = new Item(new Item.Settings().group(CPP_GROUP));







    public static void register() {
        EMPTY_PLUGIN = registerItem("empty_plugin", EMPTY_PLUGIN);
        LOW_TEMPERATURE_PLUGIN = registerItem("low_temperature_plugin", LOW_TEMPERATURE_PLUGIN);
        LOW_PRESSURE_PLUGIN = registerItem("low_pressure_plugin", LOW_PRESSURE_PLUGIN);
        HIGH_TEMPERATURE_PLUGIN = registerItem("high_temperature_plugin", HIGH_TEMPERATURE_PLUGIN);
        HIGH_PRESSURE_PLUGIN = registerItem("high_pressure_plugin", HIGH_PRESSURE_PLUGIN);
        COBBLESTONE_PLUGIN = registerItem("cobblestone_plugin", COBBLESTONE_PLUGIN);
        STONE_PLUGIN = registerItem("stone_plugin", STONE_PLUGIN);
        BLACK_STONE_PLUGIN = registerItem("blackstone_plugin", BLACK_STONE_PLUGIN);
        NETHERRACH_PLUGIN = registerItem("netherrack_plugin", NETHERRACH_PLUGIN);
        END_STONE_PLUGIN = registerItem("end_stone_plugin", END_STONE_PLUGIN);
        BASALT_PLUGIN = registerItem("basalt_plugin", BASALT_PLUGIN);

        ANCIENT_SCROLL = registerItem("ancient_scroll", ANCIENT_SCROLL);

        ENCHANTED_IRON = registerItem("enchanted_iron", ENCHANTED_IRON);
        ENCHANTED_GOLD = registerItem("enchanted_gold", ENCHANTED_GOLD);
        ENCHANTED_DIAMOND = registerItem("enchanted_diamond", ENCHANTED_DIAMOND);




    }

    private static Item registerItem(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(modId, id), item);
    }
}
