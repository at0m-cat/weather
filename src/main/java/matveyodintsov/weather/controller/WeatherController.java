package matveyodintsov.weather.controller;

import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.model.Weather;
import matveyodintsov.weather.exeption.IncorrectCityNameValue;
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

    //todo:
    // создать список погоды пользователя /weather/{user}
    // создать /weather/{user}/{city} - подробная информация о погоде на отдельной странице
    // создать лимит добавления погоды (5 городов)
    // создать удаление погоды

    private final SessionInterceptor sessionInterceptor;
    private final WeatherService<Weather, Account> weatherService;
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    public WeatherController(SessionInterceptor sessionInterceptor, WeatherService<Weather, Account> weatherService) {
        this.sessionInterceptor = sessionInterceptor;
        this.weatherService = weatherService;
    }

    @GetMapping("/find")
    public String find(@CookieValue(value = AppConfig.Constants.SESSION_ID, required = false) String sessionId, Model model) {
        if (!sessionInterceptor.isUserAuthenticated(sessionId, model)) {
            return "redirect:/login";
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

}
