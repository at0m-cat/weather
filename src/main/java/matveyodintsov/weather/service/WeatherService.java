package matveyodintsov.weather.service;

import matveyodintsov.weather.api.Api;
import matveyodintsov.weather.exeption.LocationNotFoundDataBase;
import matveyodintsov.weather.model.Location;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.model.Weather;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private final LocationService locationService;
    private final Api<Weather> weatherApi;

    @Autowired
    public WeatherService(LocationService locationService, Api<Weather> weatherApi) {
        this.locationService = locationService;
        this.weatherApi = weatherApi;
    }

    public void insertUserLocation(String city, Users user) {
        try {
            findLocationAndSaveUser(city, user);
        } catch (LocationNotFoundDataBase ignored) {
            findWeatherByCityNameAndSaveUserLocation(city, user);
        }
    }

    public List<Weather> getUserWeathers(Users user) {
        List<Weather> weatherList = new ArrayList<>();
        List<Location> locations = locationService.findAllLocationsFromUser(user);
        for (Location location : locations) {
            Weather weather = findWeatherByLocation(location);
            weatherList.add(weather);
        }
        return weatherList;
    }

    private void findLocationAndSaveUser(String city, Users user) {
        Location location = locationService.findCityLocationInDataBase(city, user);
        saveUserToLocation(user, location);
    }

    private void findWeatherByCityNameAndSaveUserLocation(String city, Users user) {
        Weather weather = weatherApi.getWeatherByCity(city);
        Location location = weather.getLocation();
        saveUserToLocation(user, location);
    }

    private void saveUserToLocation(Users user, Location location) {
        location.setUser(user);
        locationService.save(location);
    }

    private Weather findWeatherByLocation(Location location) {
        String latitude = location.getLatitude().toString();
        String longitude = location.getLongitude().toString();

        return weatherApi.getWeatherByLocation(latitude, longitude);
    }
}

