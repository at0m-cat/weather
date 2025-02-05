package matveyodintsov.weather.service;

import matveyodintsov.weather.dto.UsersDto;
import matveyodintsov.weather.exeption.AuthNotFoundException;
import matveyodintsov.weather.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public Users login(UsersDto userDto) throws AuthNotFoundException {
        return userService.findByLoginAndPassword(userService.mapToUsers(userDto));
    }

    public void register(UsersDto userDto) throws AuthNotFoundException {
        Users user = userService.mapToUsers(userDto);
        if (!userService.existsByLogin(user)) {
            userService.save(user);
        }
    }
}
