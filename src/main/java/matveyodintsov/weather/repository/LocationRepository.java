package matveyodintsov.weather.repository;

import matveyodintsov.weather.dto.UsersDto;
import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.model.Location;
import matveyodintsov.weather.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findAllByUser(Users user);

    @Query("SELECT (COUNT(l) > 0) FROM Location l WHERE l.user = :user AND l.name = :name")
    boolean existsByUserAndName(@Param("user") Users user, @Param("name") String name);

    Optional<Location> findByUserAndName(Users user, String name);

    Optional<Location> findByName(String name);

    @Query("select (count(l) > 0) from Location l where l.name =:name")
    boolean existsByName(@Param("name") String name);
}
