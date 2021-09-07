package cpp.api;

import com.google.gson.JsonObject;
import cpp.config.CppConfig;

public interface ICppConfig {

    default JsonObject getConfig() {
        return CppConfig.getConfig(getConfigName());
    }

    String getConfigName();

}
