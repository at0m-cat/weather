package matveyodintsov.weather.repository;

import matveyodintsov.weather.model.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface SessionRepository extends JpaRepository<Sessions, Long> {


}
