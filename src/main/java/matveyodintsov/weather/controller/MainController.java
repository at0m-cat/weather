package matveyodintsov.weather.controller;

import matveyodintsov.weather.service.UserService;
import matveyodintsov.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class MainController {

    UserService userService;
    WeatherService weatherService;

    @Autowired
    public MainController(UserService userService, WeatherService weatherService) {
        this.userService = userService;
        this.weatherService = weatherService;
    }

    @GetMapping
    public String index(Model model) {
//        Map<String, List<BigDecimal>> map = weatherService.getCityCoordinateByCityName("moscow");
//        BigDecimal lon = map.get("moscow").get(0);
//        BigDecimal lat = map.get("moscow").get(1);
//        System.out.println(weatherService.getCityWeatherByCoordinate(lon, lat));
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("weatherData", weatherService.getDefaultWeatherData());
        return "index";
    }

}
