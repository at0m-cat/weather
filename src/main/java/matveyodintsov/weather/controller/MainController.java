package matveyodintsov.weather.controller;

import jakarta.servlet.http.HttpServletResponse;
import matveyodintsov.weather.data.WeatherData;
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
    public String getMainPage(@CookieValue(value = AppConst.Constants.sessionID, required = false) String sessionId,
                              HttpServletResponse response, Model model) {

        try {
            Users user = sessionInterceptor.getUserFromSession(sessionId);
//            model.addAttribute("weatherData", weatherService.getDefaultWeatherData());
            model.addAttribute("user", user);
//            model.addAttribute("weatherData", weatherData);


            // todo: вытащить координаты городов у пользователя
            //  сходить в сервис за списком погоды (поиск по координатам)


            return "index";
        } catch (SessionNotFoundException e){
            return "redirect:/login";
        }

//        model.addAttribute("user", user);
//        return "index";
    }

    @GetMapping("/default")
    public String test(Model model) {
        model.addAttribute("weatherData", weatherService.getDefaultWeatherData());
        return "index";
    }

}
