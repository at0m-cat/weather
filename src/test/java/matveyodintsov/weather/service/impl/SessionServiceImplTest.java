package matveyodintsov.weather.service.impl;

import matveyodintsov.weather.exeption.SessionNotFoundException;
import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.model.Sessions;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.repository.SessionRepository;
import matveyodintsov.weather.service.SessionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SessionServiceImplTest {

    private final SessionRepository sessionRepository = Mockito.mock(SessionRepository.class);
    private final SessionService<Sessions, Account> sessionService = new SessionServiceImp(sessionRepository);

    @Test
    @DisplayName("test find when exist session")
    public void testFindWhenExistSession() {

        UUID sessionId = UUID.randomUUID();

        Sessions session = Mockito.mock(Sessions.class);
        session.setId(sessionId);

        when(sessionRepository.findBySessionId(sessionId)).thenReturn(session);

        assertEquals(session, sessionService.find(sessionId));
    }

    @Test
    @DisplayName("test find when not exist session")
    public void testFindWhenNotExistSession() {
        when(sessionRepository.findBySessionId(UUID.randomUUID())).thenReturn(null);
        assertThrows(SessionNotFoundException.class, () -> sessionService.find(UUID.randomUUID()));
    }

    @Test
    @DisplayName("test deleteIfExistSession when exist session")
    public void testDeleteIfExistSession() {

        UUID sessionId = UUID.randomUUID();

        Sessions sessions = Mockito.mock(Sessions.class);
        sessions.setId(sessionId);

        when(sessionRepository.existsById(sessionId)).thenReturn(true);

        sessionService.deleteIfExistSession(sessionId);

        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    @DisplayName("test deleteIfExistSession when not exist session")
    public void testDeleteIfNotExistSession() {
        UUID sessionId = UUID.randomUUID();
        Sessions sessions = Mockito.mock(Sessions.class);
        sessions.setId(sessionId);

        when(sessionRepository.existsById(sessionId)).thenReturn(false);

        assertThrows(SessionNotFoundException.class, () -> sessionService.deleteIfExistSession(sessionId));
    }

    @Test
    @DisplayName("test updateOrSaveSession")
    public void testUpdateOrSaveSession() {
        UUID sessionId = UUID.randomUUID();
        Sessions session = Mockito.mock(Sessions.class);
        session.setId(sessionId);

        when(sessionRepository.save(session)).thenReturn(session);

        sessionService.updateOrSaveSession(session);

        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    @DisplayName("test deleteOldSession")
    public void testDeleteOldSession() {
        sessionService.deleteOldSession();
        verify(sessionRepository, times(1)).deleteByCreatedAtBefore(any(Date.class));
    }

    @Test
    @DisplayName("test insertUserSession if exist session by user")
    public void testInsertUserSession() {
        UUID sessionId = UUID.randomUUID();
        Users user = Mockito.mock(Users.class);
        Sessions session = Mockito.mock(Sessions.class);
        session.setId(sessionId);
        when(sessionRepository.findByUserId(user)).thenReturn(session);
        sessionService.insertUserSession(user);
        verify(sessionRepository, times(1)).save(session);

    }

    @Test
    @DisplayName("test insertUserSession if not exist user session")
    public void testInsertUserSessionIfNotExistUserSession() {
        Users user = Mockito.mock(Users.class);
        when(sessionRepository.findByUserId(user)).thenReturn(null);
        sessionService.insertUserSession(user);
        verify(sessionRepository, times(1)).save(any(Sessions.class));
    }

}
