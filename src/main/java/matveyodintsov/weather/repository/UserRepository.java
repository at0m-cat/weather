package matveyodintsov.weather.repository;

import matveyodintsov.weather.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByLogin(String login);

    Users findById(long id);

    void insertUser(Users user);

    void deleteByLogin(String login);

}
