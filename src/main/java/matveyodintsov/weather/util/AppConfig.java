package matveyodintsov.weather.util;

import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    public static final class Constants {
        public static final String SESSION_ID = "weather_app_SessionID";
        public static final String KEY = "{key}";
        public static final String CITY = "{city}";
        public static final String LATITUDE = "{lat}";
        public static final String LONGITUDE = "{lon}";
    }

    public static final class Validate {
        public static final String CITY_NAME_REGEX = "^(?!\\s)[A-Za-zА-Яа-яЁё]+(?:[ -][A-Za-zА-Яа-яЁё]+)*$";
    }

    public static final class HttpCode {
        public static final int OK = 200;
    }

}
