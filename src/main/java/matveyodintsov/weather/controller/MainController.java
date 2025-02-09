package matveyodintsov.weather.controller;

import matveyodintsov.weather.model.Weather;
import matveyodintsov.weather.exeption.SessionNotFoundException;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.service.LocationService;
import matveyodintsov.weather.service.UserService;
import matveyodintsov.weather.service.WeatherService;
import matveyodintsov.weather.util.AppConst;
import matveyodintsov.weather.util.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class MainController {

    UserService userService;
    WeatherService weatherService;
    SessionInterceptor sessionInterceptor;
    LocationService locationService;

    @Autowired
    public MainController(UserService userService, WeatherService weatherService, SessionInterceptor sessionInterceptor, LocationService locationService) {
        this.userService = userService;
        this.weatherService = weatherService;
        this.sessionInterceptor = sessionInterceptor;
        this.locationService = locationService;
    }

    @GetMapping("/")
    public String getMainPage(@CookieValue(value = AppConst.Constants.sessionID, required = false) String sessionId, Model model) {

        try {
            Users user = sessionInterceptor.getUserFromSession(sessionId);
            List<Weather> weather = weatherService.getWeatherFromUser(user);
            model.addAttribute("user", user);
            model.addAttribute("weather", weather);
            return "index";
        } catch (SessionNotFoundException e){
            return "redirect:/login";
        }
    }
}
