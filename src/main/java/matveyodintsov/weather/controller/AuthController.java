package matveyodintsov.weather.controller;

import matveyodintsov.weather.dto.UsersDto;
import matveyodintsov.weather.exeption.AuthNotFoundException;
import matveyodintsov.weather.service.AuthService;
import matveyodintsov.weather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping()
public class AuthController {

    private final UserService userService;
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String auth(Model model) {
        model.addAttribute("user", new UsersDto());
        return "auth/auth";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") UsersDto usersDto, Model model) {


        // todo: проверить в базе, если есть - редирект home
        //  если в базе нет - ошибка входа
//
        try {
            authService.login(usersDto);
        } catch (AuthNotFoundException e) {
            return "auth/login-failed";
        }
        return "index";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new UsersDto());
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String doRegistration(@ModelAttribute("user") UsersDto user, Model model) {

        String password = user.getPassword();
        String repeatPassword = user.getRepeatPassword();

        if (!password.equals(repeatPassword)) {
            return "auth/registration-failed";
        }

        try {
            authService.register(user);
        } catch (Exception e) {
            return "auth/registration-failed";
        }


        // TODO: передавать в сервис для сохранения в бд

        // todo: проверить в базе, если нет - регистрация
        //  если в базе есть - ошибка регистрации

        model.addAttribute("message", "Registration successful!");
        return "auth/registration-successfully";
    }
}