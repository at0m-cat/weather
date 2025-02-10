package matveyodintsov.weather.service;

import matveyodintsov.weather.exeption.AuthNotFoundException;
import matveyodintsov.weather.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public Account login(Account userDto) throws AuthNotFoundException {
        return userService.findByLoginAndPassword(userDto);
    }

    public void register(Account userDto) throws AuthNotFoundException {
        if (!userService.existsByLogin(userDto)) {
            userService.save(userDto);
        }
    }
}
