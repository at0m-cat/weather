package matveyodintsov.weather.repository;

import jakarta.transaction.Transactional;
import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.model.Sessions;
import matveyodintsov.weather.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.UUID;

@EnableJpaRepositories
public interface SessionRepository extends JpaRepository<Sessions, Long> {

    @Query("SELECT s FROM Sessions s WHERE s.userId = :user")
    Sessions findByUserId(@Param("user") Account user);

    @Query("SELECT s from Sessions s where s.id = :sessionId")
    Sessions findBySessionId(@Param("sessionId") UUID sessionId);

    boolean existsById(UUID sessionId);

    @Modifying
    @Transactional
    void deleteById(UUID sessionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Sessions s WHERE s.expiresat < :timeLimit")
    int deleteByCreatedAtBefore(@Param("timeLimit") Date timeLimit);

}
