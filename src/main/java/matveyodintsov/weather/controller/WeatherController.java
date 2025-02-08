package matveyodintsov.weather.controller;

import matveyodintsov.weather.data.WeatherData;
import matveyodintsov.weather.exeption.IncorrectCityNameValue;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.service.WeatherService;
import matveyodintsov.weather.util.AppConst;
import matveyodintsov.weather.util.SessionInterceptor;
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

    SessionInterceptor sessionInterceptor;
    WeatherService weatherService;

    @Autowired
    public WeatherController(SessionInterceptor sessionInterceptor, WeatherService weatherService) {
        this.sessionInterceptor = sessionInterceptor;
        this.weatherService = weatherService;
    }

    @GetMapping("/find")
    public String find(@CookieValue(value = AppConst.Constants.sessionID, required = false) String sessionId, Model model) {
        if (!sessionInterceptor.isUserAuthenticated(sessionId, model)) {
            return "redirect:/login";
        }
        model.addAttribute("weather", new WeatherData());
        return "weather/add-weather";
    }

    @PostMapping("/find")
    public String createLocation(@ModelAttribute("weather") WeatherData weatherData,
                                 @CookieValue(value = AppConst.Constants.sessionID, required = false) String sessionId) {

        Users user = sessionInterceptor.getUserFromSession(sessionId);
        String city = weatherData.getCityName();

        String regex = "^(?!\\s)[A-Za-zА-Яа-яЁё]+(?:[ -][A-Za-zА-Яа-яЁё]+)*$";
        if (!city.matches(regex)) {
            throw new IncorrectCityNameValue("Incorrect city name: " + city);
        }

        weatherService.createLocation(city, user);
        return "redirect:/";
    }

}
