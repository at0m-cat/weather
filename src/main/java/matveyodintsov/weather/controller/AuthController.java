package matveyodintsov.weather.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import matveyodintsov.weather.dto.UsersDto;
import matveyodintsov.weather.exeption.AuthNotFoundException;
import matveyodintsov.weather.exeption.SessionNotFoundException;
import matveyodintsov.weather.model.Sessions;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.service.AuthService;
import matveyodintsov.weather.service.SessionService;
import matveyodintsov.weather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping()
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final SessionService sessionService;

    @Autowired
    public AuthController(AuthService authService, UserService userService, SessionService sessionService) {
        this.authService = authService;
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("/login")
    public String auth(@CookieValue(value = "weather_app_SessionID", required = false) String sessionId, Model model) {
        if (sessionId != null) {
            try {
                UUID uuid = UUID.fromString(sessionId);
                Sessions session = sessionService.find(uuid);
                if (session.getExpiresAt().after(new Date())) {
                    model.addAttribute("user", session.getUserId());
                    return "index";
                }
            } catch (Exception ignored) {
            }
        }
        model.addAttribute("user", new UsersDto());
        return "auth/auth";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new UsersDto());
        return "auth/registration";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") UsersDto usersDto, Model model, HttpServletResponse response) {
        try {
            Users user = authService.login(usersDto);
            Sessions session = sessionService.createSession(user);
            Cookie cookie = sessionService.getSessionCookie(user);
            response.addCookie(cookie);
        } catch (AuthNotFoundException e) {
            return "auth/login-failed";
        }
        return "index";
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

        model.addAttribute("message", "Registration successful!");
        return "auth/registration-successfully";
    }

    @PostMapping("/logout")
    public String logout(@CookieValue(value = "weather_app_SessionID", required = false) String sessionId, HttpServletResponse response) {
        if (sessionId != null) {
            try {
                sessionService.deleteSession(UUID.fromString(sessionId));
            } catch (Exception ignored) {

            }
        }

        Cookie sessionCookie = new Cookie("weather_app_SessionID", "");
        sessionCookie.setMaxAge(0);
        sessionCookie.setHttpOnly(true);
        response.addCookie(sessionCookie);

        return "redirect:/login";
    }

}