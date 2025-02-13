package matveyodintsov.weather.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import matveyodintsov.weather.dto.UserRegistrationDto;
import matveyodintsov.weather.exeption.SessionNotFoundException;
import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.model.Sessions;
import matveyodintsov.weather.service.SessionService;
import matveyodintsov.weather.service.impl.SessionServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);
    private final SessionService<Sessions, Account> sessionService;

    @Autowired
    public SessionInterceptor(SessionService<Sessions, Account> sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String sessionId = getSessionIdFromCookies(request);

        if (sessionId != null) {
            try {
                UUID sessionUuid = UUID.fromString(sessionId);
                Sessions session = sessionService.find(sessionUuid);
                extendSession(session, response);
            } catch (IllegalArgumentException e) {
                logger.error("Ошибка преобразования SessionID", e);
            } catch (Exception e) {
                logger.error("Ошибка при обработке сессии", e);
            }
        } else {
            try {
                int deletedCount = sessionService.deleteOldSession();
                if (deletedCount > 0) {
                    logger.info("Удалено {} просроченных сессий", deletedCount);
                }
            } catch (Exception e) {
                logger.warn("Ошибка при удалении просроченных сессий", e);
            }
        }

        return true;
    }


    public void createSession(Account user, HttpServletResponse response) {
        Sessions session = sessionService.insertUserSession(user);
        addSessionCookie(response, session);
        logger.info("Создана новая сессия для пользователя ID: {}", user.getLogin());
    }

    //todo: убрать проверку на null
    // разбить метод на несколько
    public boolean isUserAuthenticated(String sessionId, Model model) {
        if (sessionId != null) {
            Account user = getUserFromSession(sessionId);
            model.addAttribute("user", user);
            return true;
        }
        model.addAttribute("user", new UserRegistrationDto());
        return false;
    }

    public void deleteSession(UUID sessionUuid, HttpServletResponse response) {
        sessionService.deleteIfExistSession(sessionUuid);
        removeSessionCookie(response);
        logger.info("Сессия истекла и удалена: {}", sessionUuid);
    }

    public Account getUserFromSession(String sessionId) throws SessionNotFoundException {
        try {
            UUID sessionUuid = UUID.fromString(sessionId);
            Sessions session = sessionService.find(sessionUuid);
            if (session.getExpiresat().after(new Date())) {
                return session.getUserId();
            }
        } catch (Exception ignored) {
            throw new SessionNotFoundException("Session not found");
        }
        return null;
    }

    private void extendSession(Sessions session, HttpServletResponse response) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        session.setExpiresat(calendar.getTime());
        sessionService.updateOrSaveSession(session);
        updateSessionCookie(response, session);
        logger.info("Сессия продлена. Пользователь: {}", session.getUserId().getLogin());
    }

    private String getSessionIdFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (AppConfig.Constants.SESSION_ID.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void addSessionCookie(HttpServletResponse response, Sessions session) {
        Cookie cookie = new Cookie(AppConfig.Constants.SESSION_ID, session.getId().toString());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        logger.info("Set-Cookie: SESSION={}, Secure={}, SameSite=None", session.getId(), cookie.getSecure());
    }

    private void updateSessionCookie(HttpServletResponse response, Sessions session) {
        removeSessionCookie(response);
        addSessionCookie(response, session);
    }

    private void removeSessionCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(AppConfig.Constants.SESSION_ID, "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}