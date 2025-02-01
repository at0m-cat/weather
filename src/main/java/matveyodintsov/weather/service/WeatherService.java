package matveyodintsov.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import matveyodintsov.weather.data.WeatherData;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private final String key;
    private final String requestByCityUrl;
    private final String requestByCoordinateUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LocationService locationService;

    public WeatherService(@Value("${weather.api.key}") String apiKey,
                          @Value("${weather.url.geo}") String requestByCityUrl,
                          @Value("${weather.url.weather}") String requestByCoordinateUrl,
                          LocationService locationService) {
        this.key = apiKey;
        this.requestByCityUrl = requestByCityUrl;
        this.requestByCoordinateUrl = requestByCoordinateUrl;
        this.locationService = locationService;
    }

    public List<WeatherData> getDefaultWeatherData() {
        List<WeatherData> weatherDataList = new ArrayList<>();
        weatherDataList.add(getWeather("krasnoyarsk"));
        weatherDataList.add(getWeather("zelenogorsk"));
        weatherDataList.add(getWeather("moscow"));
        weatherDataList.add(getWeather("yakutsk"));

        return weatherDataList;
    }

    public WeatherData getWeather(String city) {
        String url = requestByCityUrl
                .replace("{city}", city)
                .replace("{key}", key);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(new HttpGet(url))) {

            JsonNode node = objectMapper.readTree(response.getEntity().getContent());
//            BigDecimal lon = node.get("coord").get("lon").decimalValue();
//            BigDecimal lat = node.get("coord").get("lat").decimalValue();

            return mapJsonToWeatherData(node, city);

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private WeatherData mapJsonToWeatherData(JsonNode node, String city) {
        WeatherData weatherData = new WeatherData();
        weatherData.setCityName(city);
        weatherData.setIconUrl(node.get("weather").get(0).get("icon").asText());
        weatherData.setTemperature(node.get("main").get("temp").decimalValue().subtract(BigDecimal.valueOf(273.15)));
        weatherData.setDescription(node.get("weather").get(0).get("description").asText());
        weatherData.setFeelsLike(node.get("main").get("feels_like").decimalValue().subtract(BigDecimal.valueOf(273.15)));
        weatherData.setWindSpeed(node.get("wind").get("speed").decimalValue());
        weatherData.setHumidity(node.get("main").get("humidity").decimalValue());
        weatherData.setPressure(node.get("main").get("pressure").decimalValue());
        return weatherData;
    }
}


//
//
//    public Map<String, List<BigDecimal>> getCityCoordinateByCityName(String city) {
//        String url = requestByCityUrl.replace("{city}", city).replace("{key}", key);
//        try (CloseableHttpClient httpClient = HttpClients.createDefault();
//             CloseableHttpResponse response = httpClient.execute(new HttpGet(url))) {
//
//            JsonNode node = objectMapper.readTree(response.getEntity().getContent());
//            BigDecimal lon = node.get("coord").get("lon").decimalValue();
//            BigDecimal lat = node.get("coord").get("lat").decimalValue();
//
//            return Map.of(city, List.of(lon, lat));
//
//        } catch (IOException e) {
//            throw new RuntimeException(e.getMessage(), e);
//        }
//    }
//
//    public String getCityWeatherByCoordinate(BigDecimal longitude, BigDecimal latitude) {
//        String url = requestByCoordinateUrl
//                .replace("{lon}", longitude.toString())
//                .replace("{lat}", latitude.toString())
//                .replace("{key}", key);
//
//        try (CloseableHttpClient httpClient = HttpClients.createDefault();
//             CloseableHttpResponse response = httpClient.execute(new HttpGet(url))) {
//            JsonNode node = objectMapper.readTree(response.getEntity().getContent());
//
//            return node.toPrettyString();
//
//        } catch (IOException e) {
//            throw new RuntimeException(e.getMessage(), e);
//        }
//    }
//
//}


//{
//        "coord" : {
//        "lon" : 37.6156,
//        "lat" : 55.7522
//        },
//        "weather" : [ {
//        "id" : 804,
//        "main" : "Clouds",
//        "description" : "overcast clouds",
//        "icon" : "04d"
//        } ],
//        "base" : "stations",
//        "main" : {
//        "temp" : 277.48,
//        "feels_like" : 273.72,
//        "temp_min" : 277.39,
//        "temp_max" : 278.36,
//        "pressure" : 1016,
//        "humidity" : 65,
//        "sea_level" : 1016,
//        "grnd_level" : 996
//        },
//        "visibility" : 10000,
//        "wind" : {
//        "speed" : 4.86,
//        "deg" : 191,
//        "gust" : 10.85
//        },
//        "clouds" : {
//        "all" : 100
//        },
//        "dt" : 1738413790,
//        "sys" : {
//        "type" : 2,
//        "id" : 2095214,
//        "country" : "RU",
//        "sunrise" : 1738387409,
//        "sunset" : 1738418559
//        },
//        "timezone" : 10800,
//        "id" : 524901,
//        "name" : "Moscow",
//        "cod" : 200
//        }

//https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={key}

