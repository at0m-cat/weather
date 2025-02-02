package matveyodintsov.weather.repository;

import matveyodintsov.weather.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE u.login = :#{#user.login} AND u.password = :#{#user.password}")
    Users findByLoginAndPassword(@Param("user") Users user);

    boolean existsByLogin(String login);
}
