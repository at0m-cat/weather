package matveyodintsov.weather.repository;

import matveyodintsov.weather.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

}
