package matveyodintsov.weather.controller;

import jakarta.servlet.http.HttpServletResponse;
import matveyodintsov.weather.dto.UserRegistrationDto;
import matveyodintsov.weather.exeption.AuthNotFoundException;
import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.service.AuthService;
import matveyodintsov.weather.util.AppConfig;
import matveyodintsov.weather.util.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping()
public class AuthController {

    private final AuthService<Account> authService;
    private final SessionInterceptor sessionInterceptor;

    @Autowired
    public AuthController(AuthService<Account> authService, SessionInterceptor sessionInterceptor) {
        this.authService = authService;
        this.sessionInterceptor = sessionInterceptor;
    }

    @GetMapping("/login")
    public String auth(@CookieValue(value = AppConfig.Constants.SESSION_ID, required = false) String sessionId, Model model) {
        return sessionInterceptor.isUserAuthenticated(sessionId, model) ? "index" : "auth/auth";
    }

    @GetMapping("/registration")
    public String registration(@CookieValue(value = AppConfig.Constants.SESSION_ID, required = false) String sessionId, Model model) {
        return sessionInterceptor.isUserAuthenticated(sessionId, model) ? "index" : "auth/registration";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") UserRegistrationDto usersDto, HttpServletResponse response) {
        try {
            Account user = authService.login(usersDto);
            sessionInterceptor.createSession(user, response);
            return "redirect:/";
        } catch (AuthNotFoundException e) {
            return "auth/login-failed";
        }
    }

    @PostMapping("/registration")
    public String doRegistration(@ModelAttribute("user") UserRegistrationDto user, Model model) {
        if (!user.getPassword().equals(user.getRepeatPassword())) {
            return "auth/registration-failed";
        }
        try {
            authService.register(user);
            model.addAttribute("message", "Registration successful!");
            return "auth/registration-successfully";
        } catch (Exception e) {
            return "auth/registration-failed";
        }
    }

    @PostMapping("/logout")
    public String logout(@CookieValue(value = AppConfig.Constants.SESSION_ID, required = false) String sessionId, HttpServletResponse response) {
        if (sessionId != null) {
            sessionInterceptor.deleteSession(UUID.fromString(sessionId), response);
        }
        return "redirect:/login";
    }
}