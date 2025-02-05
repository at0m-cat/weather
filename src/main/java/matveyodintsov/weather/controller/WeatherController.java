package matveyodintsov.weather.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/weather")
public class WeatherController {


    @GetMapping("/find")
    public String find(Model model) {
        return "weather/add-weather";
    }

    @PostMapping("/find")
    public String getWeather(Model model) {
        return "redirect:/";
    }

}
