package matveyodintsov.weather.controller;

import matveyodintsov.weather.dto.UsersDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping()
public class AuthController {

    @GetMapping("/login")
    public String auth(Model model) {
        model.addAttribute("user", new UsersDto());
        return "auth";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new UsersDto());
        return "registration";
    }

    @PostMapping("/registration")
    public String doRegistration(@ModelAttribute("user") UsersDto user, Model model) {
        // TODO: передавать в сервис для сохранения в бд

        model.addAttribute("message", "Registration successful!");
        return "registration-successfully";
    }
}