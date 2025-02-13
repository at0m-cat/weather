package matveyodintsov.weather.repository;

import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<Users, Long> {

    Account findByLogin(String login);

    boolean existsByLogin(String login);
}
