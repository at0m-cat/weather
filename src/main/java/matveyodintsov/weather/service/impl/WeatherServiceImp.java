package matveyodintsov.weather.service.impl;

import matveyodintsov.weather.api.Api;
import matveyodintsov.weather.exeption.LocationNotFoundDataBase;
import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.model.Location;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.model.Weather;
import matveyodintsov.weather.service.LocationService;
import matveyodintsov.weather.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherServiceImp implements WeatherService<Weather, Account> {

    private final LocationService<Location, Account> locationService;
    private final Api<Weather> weatherApi;

    @Autowired
    public WeatherServiceImp(LocationService<Location, Account> locationService, Api<Weather> weatherApi) {
        this.locationService = locationService;
        this.weatherApi = weatherApi;
    }

    @Override
    public void insertUserLocation(String city, Account user) {
        try {
            findLocationAndSaveUser(city, user);
        } catch (LocationNotFoundDataBase ignored) {
            findWeatherByCityNameAndSaveUserLocation(city, user);
        }
    }

    @Override
    public List<Weather> getUserWeathers(Account user) {
        List<Weather> weatherList = new ArrayList<>();
        List<Location> locations = locationService.findAllLocationsFromUser(user);
        for (Location location : locations) {
            Weather weather = findWeatherByLocation(location);
            weatherList.add(weather);
        }
        return weatherList;
    }

    @Override
    public int getUserWeatherCount(Account user) {
        return locationService.findAllLocationsFromUser(user).size();
    }

    private void findLocationAndSaveUser(String city, Account user) {
        Location location = locationService.findCityLocationInDataBase(city, user);
        saveUserToLocation(user, location);
    }

    private void findWeatherByCityNameAndSaveUserLocation(String city, Account user) {
        Weather weather = weatherApi.getWeatherByCity(city);
        Location location = weather.getLocation();
        location.setName(weather.getCityName());
        saveUserToLocation(user, location);
    }

    private void saveUserToLocation(Account user, Location location) {
        location.setUser((Users) user);
        locationService.save(location);
    }

    private Weather findWeatherByLocation(Location location) {
        String latitude = location.getLatitude().toString();
        String longitude = location.getLongitude().toString();

        return weatherApi.getWeatherByLocation(latitude, longitude);
    }
}

