package net.cpp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.fabricmc.loader.api.FabricLoader;


public class CppConfig {
    private static final File JSON_PATH = new File(FabricLoader.getInstance().getConfigDir().toFile(), "Craftingpp");

    private static final Logger logger = LogManager.getLogger("Craftingpp");

    public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

    public CppConfig() {
        if (!JSON_PATH.isDirectory() && !JSON_PATH.mkdirs()) {
            logger.error("Failed to create config dir");
        }
    }

    public JsonObject getConfig(String configName){
        File json_file = new File(JSON_PATH, configName + ".json");
        if (json_file.exists()) {
            try(BufferedReader reader = new BufferedReader(new FileReader(json_file))) {
                return GSON.fromJson(reader, JsonObject.class);
            } catch (IOException e) {
                logger.error("Failed to load config file");
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean initConfig (String configName, JsonObject json) {
        File json_file = new File(JSON_PATH, configName + ".json");
        if (json_file.exists()) {
            return true;
        }else {
            try(FileWriter fileWriter = new FileWriter(json_file)) {
                fileWriter.write(GSON.toJson(json));
                return true;
            }catch (IOException e) {
                logger.error("Failed to create config file!");
                e.printStackTrace();
                return false;
            }
        }
    }
}
