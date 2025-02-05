package matveyodintsov.weather.controller;

import jakarta.servlet.http.HttpServletResponse;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.service.UserService;
import matveyodintsov.weather.service.WeatherService;
import matveyodintsov.weather.util.AppConst;
import matveyodintsov.weather.util.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class MainController {

    UserService userService;
    WeatherService weatherService;
    SessionInterceptor sessionInterceptor;

    @Autowired
    public MainController(UserService userService, WeatherService weatherService, SessionInterceptor sessionInterceptor) {
        this.userService = userService;
        this.weatherService = weatherService;
        this.sessionInterceptor = sessionInterceptor;
    }

    @GetMapping
    public String main(@CookieValue(value = AppConst.Constants.sessionID, required = false) String sessionId, HttpServletResponse response, Model model) {
        model.addAttribute("weatherData", weatherService.getDefaultWeatherData());
        Users user = sessionInterceptor.getUserFromSession(sessionId);

        // todo получить юзера - отправить юзера в сервис погоды - получить его погоду по координатам из сервиса локаций
        //  если нет, голая страница с поиском погоды

        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/default")
    public String index(Model model) {
        model.addAttribute("weatherData", weatherService.getDefaultWeatherData());
        return "index";
    }

}
