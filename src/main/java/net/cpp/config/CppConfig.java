package net.cpp.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.cpp.Craftingpp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.fabricmc.loader.api.FabricLoader;


public class CppConfig {
    private final File JSON_PATH = new File(FabricLoader.getInstance().getConfigDir().toFile(), Craftingpp.MOD_ID2);

    private final Logger logger = LogManager.getLogger(Craftingpp.MOD_ID1);

    private final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

    private final JsonObject JSON = new JsonObject();

    public CppConfig() {
        if (!JSON_PATH.isDirectory() && !JSON_PATH.mkdirs()) {
            logger.error("Failed to create config dir " + JSON_PATH);
        }
        this.init();
    }

    private void init() {
        File[] files = JSON_PATH.listFiles();
        if (files != null) {
            for (File json_file : files) {
                String file_name = json_file.getName();
                if (json_file.isFile() && file_name.endsWith(".json")) {
                    try(BufferedReader reader = new BufferedReader(new FileReader(json_file))) {
                        JSON.add(file_name.substring(0, file_name.length()-5), GSON.fromJson(reader, JsonObject.class));
                    } catch (IOException e) {
                        logger.error("Failed to load config file " + json_file);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public JsonObject getConfig(String configName){
        return this.JSON.get(configName).getAsJsonObject();
    }

    public boolean initConfig(String configName, JsonObject json) {
        File json_file = new File(JSON_PATH, configName + ".json");
        if (json_file.exists()) {
            return true;
        }else {
            try(FileWriter fileWriter = new FileWriter(json_file)) {
                fileWriter.write(GSON.toJson(json));
                JSON.add(configName, json);
                return true;
            }catch (IOException e) {
                logger.error("Failed to create config file " + json_file);
                e.printStackTrace();
                return false;
            }
        }
    }

    protected void changeConfig(String configName, JsonObject json) {
        File json_file = new File(JSON_PATH, configName + ".json");
        if (!json_file.exists()) {
            logger.error(configName + "is not exists");
        }else {
            try(FileWriter fileWriter = new FileWriter(json_file)) {
                fileWriter.write(formatJson(json));
                JSON.add(configName, json);
            }catch (IOException e) {
                logger.error("Failed to change config file " + json_file);
                e.printStackTrace();
            }
        }
    }

    protected String formatJson(JsonObject json) {
        return GSON.toJson(json);
    }

    protected JsonObject getJson() {
        return this.JSON;
    }

}
