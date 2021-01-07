package net.cpp.api;

import com.google.gson.JsonObject;
import net.cpp.Craftingpp;

public interface ICppConfig {

    String getConfigName();

    JsonObject defaultConfig(JsonObject json);

    default JsonObject getConfig() {
        if (Craftingpp.CONFIG.initConfig(this.getConfigName(), this.defaultConfig(new JsonObject()))) {
            return Craftingpp.CONFIG.getConfig(this.getConfigName());
        }
        return null;
    }
}
