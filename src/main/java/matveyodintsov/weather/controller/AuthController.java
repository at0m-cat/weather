package matveyodintsov.weather.controller;

import jakarta.servlet.http.HttpServletResponse;
import matveyodintsov.weather.dto.UsersDto;
import matveyodintsov.weather.exeption.AuthNotFoundException;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.service.AuthService;
import matveyodintsov.weather.util.AppConst;
import matveyodintsov.weather.util.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping()
public class AuthController {

    private final AuthService authService;
    private final SessionInterceptor sessionInterceptor;

    @Autowired
    public AuthController(AuthService authService, SessionInterceptor sessionInterceptor) {
        this.authService = authService;
        this.sessionInterceptor = sessionInterceptor;
    }

    @GetMapping("/login")
    public String auth(@CookieValue(value = AppConst.Constants.sessionID, required = false) String sessionId, Model model) {
        return sessionInterceptor.isUserAuthenticated(sessionId, model) ? "index" : "auth/auth";
    }

    @GetMapping("/registration")
    public String registration(@CookieValue(value = AppConst.Constants.sessionID, required = false) String sessionId, Model model) {
        return sessionInterceptor.isUserAuthenticated(sessionId, model) ? "index" : "auth/registration";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") UsersDto usersDto, HttpServletResponse response) {
        try {
            Users user = authService.login(usersDto);
            sessionInterceptor.createSession(user, response);
            return "index";
        } catch (AuthNotFoundException e) {
            return "auth/login-failed";
        }
    }

    @PostMapping("/registration")
    public String doRegistration(@ModelAttribute("user") UsersDto user, Model model) {
        if (!user.getPassword().equals(user.getRepeatPassword())) {
            return "auth/registration-failed";
        }

        try {
            authService.register(user);
        } catch (Exception e) {
            return "auth/registration-failed";
        }

        model.addAttribute("message", "Registration successful!");
        return "auth/registration-successfully";
    }

    @PostMapping("/logout")
    public String logout(@CookieValue(value = AppConst.Constants.sessionID, required = false) String sessionId, HttpServletResponse response) {
        if (sessionId != null) {
            sessionInterceptor.deleteSession(UUID.fromString(sessionId), response);
        }
        return "redirect:/login";
    }
}