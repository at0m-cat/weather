package matveyodintsov.weather.controller;

import matveyodintsov.weather.data.WeatherData;
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
    public String getWeather(@ModelAttribute("weather") WeatherData weatherData, Model model) {

        // todo: найти город - записать координаты пользователю
        //  ( у пользователя на главной странице вытаскивать погоду по координатам )

        model.addAttribute("weatherData", weatherService.getWeather(weatherData.getCityName()));
        return "index";
    }

}
