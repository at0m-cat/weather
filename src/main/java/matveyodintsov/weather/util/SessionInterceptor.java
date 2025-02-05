package matveyodintsov.weather.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import matveyodintsov.weather.model.Sessions;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);
    private final SessionService sessionService;

    public SessionInterceptor(SessionService sessionService) {
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
                request.setAttribute(AppConst.Constants.loggerUser, session.getUserId());
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

    public void createSession(Users user, HttpServletResponse response) {
        Sessions session = sessionService.createSession(user);
        addSessionCookie(response, session);
        logger.info("Создана новая сессия для пользователя ID: {}", user.getId());
    }

    private void extendSession(Sessions session, HttpServletResponse response) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        session.setExpiresat(calendar.getTime());
        sessionService.updateSession(session);
        updateSessionCookie(response, session);
        logger.info("Сессия продлена. Пользователь ID: {}", session.getUserId().getId());
    }

    public void deleteSession(UUID sessionUuid, HttpServletResponse response) {
        sessionService.deleteSession(sessionUuid);
        removeSessionCookie(response);
        logger.info("Сессия истекла и удалена: {}", sessionUuid);
    }

    public Users getUserFromSession(String sessionId) {
        try {
            UUID sessionUuid = UUID.fromString(sessionId);
            Sessions session = sessionService.find(sessionUuid);
            if (session.getExpiresat().after(new Date())) {
                return session.getUserId();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private String getSessionIdFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (AppConst.Constants.sessionID.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void addSessionCookie(HttpServletResponse response, Sessions session) {
        Cookie cookie = new Cookie(AppConst.Constants.sessionID, session.getId().toString());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }

    private void updateSessionCookie(HttpServletResponse response, Sessions session) {
        removeSessionCookie(response);
        addSessionCookie(response, session);
    }

    private void removeSessionCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(AppConst.Constants.sessionID, "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}