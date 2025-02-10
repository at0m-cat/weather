package matveyodintsov.weather.service;
import java.util.UUID;

public interface SessionService<T, K> {

    public T find(UUID sessionId);

    public void deleteIfExistSession(UUID sessionId);

    public void updateOrSaveSession(T session);

    public int deleteOldSession();

    public T insertUserSession(K user);

}
