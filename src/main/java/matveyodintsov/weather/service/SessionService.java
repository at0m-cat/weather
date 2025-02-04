package matveyodintsov.weather.service;

import jakarta.servlet.http.Cookie;
import matveyodintsov.weather.exeption.SessionNotFoundException;
import matveyodintsov.weather.model.Sessions;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;


@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserService userService;

    @Autowired
    public SessionService(SessionRepository sessionRepository, UserService userService) {
        this.sessionRepository = sessionRepository;
        this.userService = userService;
    }

    public Sessions find(UUID sessionId) throws SessionNotFoundException {
        return Optional.ofNullable(sessionRepository.findBySessionId(sessionId))
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));
    }

    public void deleteSession(UUID sessionId) throws SessionNotFoundException {
        if (sessionRepository.existsById(sessionId)) {
            sessionRepository.deleteById(sessionId);
        } else {
            throw new SessionNotFoundException("Session not found");
        }
    }

    public Cookie getSessionCookie(Users user) {
        Sessions session = sessionRepository.findByUserId(user);
        Cookie cookie = new Cookie("weather_app_SessionID", session.getId().toString());
        cookie.setHttpOnly(true);
        return cookie;
    }

    public Sessions createSession(Users user) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        Sessions session = sessionRepository.findByUserId(user);
        if (session == null) {
            session = new Sessions();
            session.setUserId(user);
            session.setExpiresAt(calendar.getTime());
        }
        session.setExpiresAt(calendar.getTime());
        return sessionRepository.save(session);
    }
}
