package matveyodintsov.weather.service;

import matveyodintsov.weather.dto.UsersDto;
import matveyodintsov.weather.exeption.AuthNotFoundException;
import matveyodintsov.weather.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private SessionService sessionService;
    private UserService userService;

    @Autowired
    public AuthService(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    public void login(UsersDto userDto) throws AuthNotFoundException {
        Users findUser = userService.findByLoginAndPassword(mapToUsers(userDto));
    }

    public void logout(UsersDto user) {
    }

    public void register(UsersDto userDto) throws AuthNotFoundException {
        Users user = mapToUsers(userDto);
        if (!userService.existsByLogin(user)) {
            userService.save(user);
        }
    }

    private void createCookie() {
    }

    private void deleteCookie() {
    }

    private Users mapToUsers(UsersDto usersDto) {
        Users users = new Users();
        users.setLogin(usersDto.getLogin().toLowerCase());
        users.setPassword(usersDto.getPassword());
        return users;
    }

}
