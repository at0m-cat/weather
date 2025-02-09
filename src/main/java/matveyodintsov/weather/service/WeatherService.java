package matveyodintsov.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import matveyodintsov.weather.model.Weather;
import matveyodintsov.weather.util.WeatherApi;
import matveyodintsov.weather.exeption.LocationNotFoundDataBase;
import matveyodintsov.weather.model.Location;
import matveyodintsov.weather.model.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private final String key;
    private final String findLocationByCity;
    private final String findCityByLocation;
    private final LocationService locationService;

    public WeatherService(@Value("${weather.api.key}") String apiKey,
                          @Value("${weather.url.geo}") String findLocationByCity,
                          @Value("${weather.url.weather}") String findCityByLocation,
                          LocationService locationService) {
        this.key = apiKey;
        this.findLocationByCity = findLocationByCity;
        this.findCityByLocation = findCityByLocation;
        this.locationService = locationService;
    }

    public void createLocation(String city, Users user) {
        Location location;
        try {
            location = locationService.findCityLocationInDataBase(city, user);
            location.setUser(user);
        } catch (LocationNotFoundDataBase ignored) {
            JsonNode node = WeatherApi
                    .getNode(findLocationByCityName(city));
            location = locationService.createLocation(node, user);
        }
        locationService.save(location);
    }

    public List<Weather> getWeatherFromUser(Users user) {
        List<Weather> weatherList = new ArrayList<>();
        List<Location> locations = locationService.findAllLocationsFromUser(user);
        for (Location location : locations) {
            Weather weather = findWeatherByLocation(location);
            weatherList.add(weather);
        }
        return weatherList;
    }

    private Weather findWeatherByLocation(Location location) {
        JsonNode node = WeatherApi
                .getNode(findCityByLocation(location.getLatitude().toString(), location.getLongitude().toString()));
        Weather weather = mapJsonToWeather(node);
        weather.setLocation(location);
        return weather;
    }

    private Weather mapJsonToWeather(JsonNode node) {
        return WeatherApi.mapToWeather(node);
    }

    private String findLocationByCityName(String city) {
        return findLocationByCity
                .replace("{city}", city)
                .replace("{key}", key);
    }

    private String findCityByLocation(String lat, String lon) {
        return findCityByLocation
                .replace("{lat}", lat)
                .replace("{lon}", lon)
                .replace("{key}", key);
    }
}

