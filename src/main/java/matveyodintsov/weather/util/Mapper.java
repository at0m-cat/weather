package matveyodintsov.weather.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import matveyodintsov.weather.exeption.CityNotFoundException;
import matveyodintsov.weather.model.Weather;

public class Mapper {

    public static class WeatherMapper {

        public static Weather mapJsonToWeather(JsonNode node) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.treeToValue(node, Weather.class);
            } catch (Exception e) {
                throw new CityNotFoundException("Weather data could not be parsed");
            }
        }
    }
}
