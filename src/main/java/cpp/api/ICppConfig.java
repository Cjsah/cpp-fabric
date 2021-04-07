package cpp.api;

import com.google.gson.JsonObject;
import cpp.Craftingpp;

public interface ICppConfig {

    String getConfigName();

    JsonObject defaultConfig(JsonObject json);

    default JsonObject getDefaultConfig() {
        return defaultConfig(new JsonObject());
    }

    default JsonObject getConfig() {
        if (Craftingpp.CONFIG.initConfig(this.getConfigName(), this.getDefaultConfig())) {
            return Craftingpp.CONFIG.getConfig(this.getConfigName());
        }
        return null;
    }
}
