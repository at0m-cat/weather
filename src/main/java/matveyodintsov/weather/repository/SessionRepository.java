package matveyodintsov.weather.repository;

import matveyodintsov.weather.model.Sessions;
import matveyodintsov.weather.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Sessions, Long> {

    Sessions findByUserId(Users userId);

    Sessions insertSession(Sessions session);

    void deleteSession(Sessions session);

}
