package matveyodintsov.weather.service.impl;

import matveyodintsov.weather.exeption.AuthNotFoundException;
import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.repository.UserRepository;
import matveyodintsov.weather.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
public class UserServiceImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService<Account> userService;

    @Test
    @DisplayName("test save")
    public void testSave() {
        String login = "test";
        String password = "test";
        Account user = new Users();
        user.setLogin(login);
        user.setPassword(password);

        userService.save(user);

        Account findUser = userRepository.findByLogin(login);

        assertEquals(user.getLogin(), findUser.getLogin());

    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("test findByLoginAndPassword when exist user")
    public void testFindByLoginAndPasswordWhenExistUser() {
        String login = "test";
        String password = "test";
        Users user = new Users();
        user.setLogin(login);
        user.setPassword(password);

        userService.save(user);
        Account finduser = userService.findByLoginAndPassword((Account) user);

        assertEquals(user.getLogin(), finduser.getLogin());
    }

    @Test
    @DisplayName("test findByLoginAndPassword when not exist user")
    public void testFindByLoginAndPasswordWhenNotExistUser() {
        Account user = new Users();
        user.setLogin("test");
        user.setPassword("test");
        assertThrows(AuthNotFoundException.class, () -> userService.findByLoginAndPassword(user));

    }

    @Test
    @DisplayName("test existByLogin when exist user")
    public void testExistByLoginWhenExistUser() {
        String login = "test";
        String password = "test";
        Account user = new Users();
        user.setLogin(login);
        user.setPassword(password);
        userService.save(user);
        assertTrue(userService.existsByLogin(user));

    }

    @Test
    @DisplayName("test existByLogin when not exist user")
    public void testExistByLoginWhenNotExistUser() {
        Account user = new Users();
        user.setLogin("test");
        assertFalse(userService.existsByLogin(user));
    }

}
