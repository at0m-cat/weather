package matveyodintsov.weather.repository;

import matveyodintsov.weather.model.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Sessions, Long> {

}
