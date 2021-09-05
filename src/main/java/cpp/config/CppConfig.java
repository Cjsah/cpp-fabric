package cpp.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import cpp.Craftingpp;
import org.apache.logging.log4j.Logger;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.fabricmc.loader.api.FabricLoader;

public class CppConfig {

    private static final File CONFIG_PATH = new File(FabricLoader.getInstance().getConfigDir().toFile(), Craftingpp.MOD_ID2 + ".json");

    private static final Logger logger = Craftingpp.logger;

    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

    private static final JsonObject CONFIG;

    static {
        if (!CONFIG_PATH.exists()) {
            try (FileWriter fileWriter = new FileWriter(CONFIG_PATH)) {
                CONFIG = new JsonObject();
                defaultConfigs();
                fileWriter.write(config2String());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create config file " + CONFIG_PATH, e);
            }
        }else {
            try(BufferedReader reader = new BufferedReader(new FileReader(CONFIG_PATH))) {
                CONFIG = GSON.fromJson(reader, JsonObject.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load config file " + CONFIG_PATH, e);
            }
        }
    }

    private static void addConfigs(String name, Consumer<JsonObject> json) {
        JsonObject jsonObject = new JsonObject();
        json.accept(jsonObject);
        CONFIG.add(name, jsonObject);
    }

    private static void defaultConfigs() {
        addConfigs("blue_force_of_sky", (json) -> json.addProperty("NeedXp", true));
        addConfigs("cyan_force_of_mountain", (json) -> {
            String[] defaultNames = new String[]{"minecraft:dirt", "minecraft:dirt_path", "minecraft:diorite",
                    "minecraft:farmland", "minecraft:podzol", "minecraft:grass_block", "minecraft:mycelium",
                    "minecraft:stone", "minecraft:granite", "minecraft:diorite", "minecraft:andesite", "minecraft:gravel",
                    "minecraft:sand", "minecraft:sandstone", "minecraft:netherrack", "minecraft:blackstone"};

            json.addProperty("StartLevel", 2);
            JsonArray jsonArray = new JsonArray();
            for (String name : defaultNames) {
                jsonArray.add(name);
            }
            json.add("CanBreak", jsonArray);
        });



    }

    public static JsonObject getConfig(String configName){
        return CONFIG.get(configName).getAsJsonObject();
    }

    public static void changeConfig(String name, Consumer<JsonObject> json) {
        json.accept(CONFIG.get(name).getAsJsonObject());
        try(FileWriter fileWriter = new FileWriter(CONFIG_PATH)) {
            fileWriter.write(config2String());
        }catch (IOException e) {
            logger.error("Failed to change config.");
            e.printStackTrace();
        }

    }

    protected static String config2String() {
        return GSON.toJson(CONFIG);
    }

    protected static JsonObject getConfig() {
        return CONFIG;
    }

    public static void load() {}
}
