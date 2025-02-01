package matveyodintsov.weather.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    private static String apiKey;

    @Value("${weather.api.key}")
    public void setApiKey(String apiKey) {
        AppConfig.apiKey = apiKey;
    }

    public static String getApiKey() {
        return apiKey;
    }
}