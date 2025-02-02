package matveyodintsov.weather.repository;

import matveyodintsov.weather.model.Sessions;
import matveyodintsov.weather.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

@EnableJpaRepositories
public interface SessionRepository extends JpaRepository<Sessions, Long> {

    @Query("SELECT s FROM Sessions s WHERE s.userId = :user")
    Sessions findByUserId(@Param("user") Users user);

    @Query("SELECT s from Sessions s where s.id = :sessionId")
    Sessions findBySessionId(@Param("sessionId") UUID sessionId);
}
