package matveyodintsov.weather.controller;

import matveyodintsov.weather.exeption.LocationLimitExceeded;
import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.model.Location;
import matveyodintsov.weather.model.Weather;
import matveyodintsov.weather.exeption.IncorrectCityNameValue;
import matveyodintsov.weather.service.LocationService;
import matveyodintsov.weather.service.WeatherService;
import matveyodintsov.weather.util.AppConfig;
import matveyodintsov.weather.util.SessionInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    private final SessionInterceptor sessionInterceptor;
    private final WeatherService<Weather, Account> weatherService;
    private final LocationService<Location, Account> locationService;
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    public WeatherController(SessionInterceptor sessionInterceptor, WeatherService<Weather, Account> weatherService, LocationService<Location, Account> locationService) {
        this.sessionInterceptor = sessionInterceptor;
        this.weatherService = weatherService;
        this.locationService = locationService;
    }

    @GetMapping("/find")
    public String find(@CookieValue(value = AppConfig.Constants.SESSION_ID, required = false) String sessionId, Model model) {
        if (!sessionInterceptor.isUserAuthenticated(sessionId, model)) {
            return "redirect:/login";
        }
        Account userFromSession = sessionInterceptor.getUserFromSession(sessionId);

        int weatherCountByUser = weatherService.getUserWeatherCount(userFromSession);
        if (weatherCountByUser >= AppConfig.Constants.LOCATION_LIMIT) {
            throw new LocationLimitExceeded("Location limit exceeded");
        }

        model.addAttribute("weather", new Weather());
        return "weather/add-weather";
    }

    @PostMapping("/find")
    public String createLocation(@ModelAttribute("weather") Weather weather,
                                 @CookieValue(value = AppConfig.Constants.SESSION_ID, required = false) String sessionId) {

        Account userFromSession = sessionInterceptor.getUserFromSession(sessionId);
        String userInputCityName = weather.getCityName();

        if (!userInputCityName.matches(AppConfig.Validate.CITY_NAME_REGEX)) {
            throw new IncorrectCityNameValue("Incorrect city name: " + userInputCityName);
        }

        weatherService.insertUserLocation(userInputCityName, userFromSession);
        return "redirect:/";
    }

    @PostMapping("/delete/{user}/{city}")
    public String delete(@CookieValue(value = AppConfig.Constants.SESSION_ID, required = false) String sessionId, @PathVariable String city, @PathVariable String user) {
        Account userFromSession = sessionInterceptor.getUserFromSession(sessionId);
        if (userFromSession.getLogin().equalsIgnoreCase(user)) {
            locationService.deleteUserLocation(city.toUpperCase(), userFromSession);
            return "redirect:/";
        }
        throw new RuntimeException("Invalid user");
    }
}
