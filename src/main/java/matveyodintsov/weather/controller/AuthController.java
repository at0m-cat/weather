package matveyodintsov.weather.controller;

import lombok.AllArgsConstructor;
import matveyodintsov.weather.dto.UsersDto;
import matveyodintsov.weather.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping()
public class AuthController {

    AuthService authService;

    @GetMapping("/login")
    public String auth(Model model) {
        model.addAttribute("user", new UsersDto());
        return "auth/auth";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new UsersDto());
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String doRegistration(@ModelAttribute("user") UsersDto user, Model model) {
        // TODO: передавать в сервис для сохранения в бд

        model.addAttribute("message", "Registration successful!");
        return "auth/registration-successfully";
    }
}