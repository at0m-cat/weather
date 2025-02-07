package matveyodintsov.weather.repository;

import matveyodintsov.weather.model.Location;
import matveyodintsov.weather.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findAllByUser(Users user);

    Location findByUserAndName(Users user, String name);

    Location findByName(String name);

    // todo: пересмотреть методы exist by !!

    boolean existsByUserAndName(Users user, String name);

    boolean existsByName(String name);
}
