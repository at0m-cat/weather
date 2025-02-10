package matveyodintsov.weather.service;

import matveyodintsov.weather.dto.UsersDto;
import matveyodintsov.weather.exeption.SessionNotFoundException;
import matveyodintsov.weather.model.Account;
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

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Sessions find(UUID sessionId) throws SessionNotFoundException {
        return Optional.ofNullable(sessionRepository.findBySessionId(sessionId))
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));
    }

    public void deleteIfExistSession(UUID sessionId) throws SessionNotFoundException {
        if (sessionRepository.existsById(sessionId)) {
            sessionRepository.deleteById(sessionId);
        } else {
            throw new SessionNotFoundException("Session not found");
        }
    }

    public void updateOrSaveSession(Sessions session) {
        sessionRepository.save(session);
    }

    public int deleteOldSession() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -1);
        return sessionRepository.deleteByCreatedAtBefore(calendar.getTime());
    }

    //todo: убрать проверку на null
    public Sessions insertUserSession(Account user) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        Sessions session = sessionRepository.findByUserId(user);
        if (session == null) {
            session = new Sessions();
            session.setUserId((Users) user);
            session.getUserId().setPassword("");
            session.setExpiresat(calendar.getTime());
        }
        session.setExpiresat(calendar.getTime());
        return sessionRepository.save(session);
    }

}
