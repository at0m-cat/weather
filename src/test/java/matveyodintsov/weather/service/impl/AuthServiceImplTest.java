package matveyodintsov.weather.service.impl;

import matveyodintsov.weather.exeption.AuthNotFoundException;
import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.service.AuthService;
import matveyodintsov.weather.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthServiceImplTest {

    private final UserService<Account> userService = Mockito.mock(UserServiceImp.class);
    private final AuthService<Account> authService = new AuthServiceImp(userService);

    @Test
    @DisplayName("Test Login")
    public void testLogin() {
        Account user = new Users();
        String userLogin = "testUserLogin1122";
        String password = "testPassword";
        user.setLogin(userLogin);
        user.setPassword(password);

        when(userService.findByLoginAndPassword(user)).thenReturn(user);

        Account loggedUser = authService.login(user);

        assertEquals(user.getLogin(), loggedUser.getLogin());
        assertEquals(user.getPassword(), loggedUser.getPassword());
        verify(userService, times(1)).findByLoginAndPassword(user);
    }

    @Test
    @DisplayName("Test register when exist user then throw")
    public void testRegisterWhenExistUserThenReturnThrow() {
        Account user = new Users();
        String userLogin = "testUserLogin1122";
        String password = "testPassword";
        user.setLogin(userLogin);
        user.setPassword(password);

        when(userService.existsByLogin(user)).thenReturn(true);

        assertThrows(AuthNotFoundException.class, () -> authService.register(user));
        verify(userService, times(1)).existsByLogin(user);
    }

    @Test
    @DisplayName("Test register when not exist user then save")
    public void testRegisterWhenNotExistUserThenSave() {
        Account user = new Users();
        String userLogin = "testUserLogin1122";
        String password = "testPassword";
        user.setLogin(userLogin);
        user.setPassword(password);

        when(userService.existsByLogin(user)).thenReturn(false);

        authService.register(user);

        verify(userService, times(1)).save(user);
        verify(userService, times(1)).existsByLogin(user);
    }

}
