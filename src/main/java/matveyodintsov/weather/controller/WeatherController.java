package matveyodintsov.weather.controller;

import matveyodintsov.weather.dto.UsersDto;
import matveyodintsov.weather.model.Sessions;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.util.AppConst;
import matveyodintsov.weather.util.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    SessionInterceptor sessionInterceptor;

    @Autowired
    public WeatherController(SessionInterceptor sessionInterceptor) {
        this.sessionInterceptor = sessionInterceptor;
    }

//    private boolean isUserAuthenticated(String sessionId, Model model) {
//        if (sessionId != null) {
//            Users user = sessionInterceptor.getUserFromSession(sessionId);
//            if (user != null) {
//                model.addAttribute("user", user);
//                return true;
//            }
//        }
//        model.addAttribute("user", new UsersDto());
//        return false;
//    }

    @GetMapping("/find")
    public String find(@CookieValue(value = AppConst.Constants.sessionID, required = false) String sessionId, Model model) {
        if (!sessionInterceptor.isUserAuthenticated(sessionId, model)) {
            return "redirect:/login";
        }
        return "weather/add-weather";
    }

    @PostMapping("/find")
    public String getWeather(Model model) {
        return "redirect:/";
    }

}
