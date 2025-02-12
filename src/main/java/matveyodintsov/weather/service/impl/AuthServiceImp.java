package matveyodintsov.weather.service.impl;

import matveyodintsov.weather.exeption.AuthNotFoundException;
import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.service.AuthService;
import matveyodintsov.weather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements AuthService<Account> {

    private final UserService<Account> userService;

    @Autowired
    public AuthServiceImp(UserService<Account> userService) {
        this.userService = userService;
    }

    public Account login(Account userDto) throws AuthNotFoundException {
        return userService.findByLoginAndPassword(userDto);
    }

    public void register(Account userDto) throws AuthNotFoundException {
        if (!userService.existsByLogin(userDto)) {
            userService.save(userDto);
        } else {
            throw new AuthNotFoundException("Login already exists");
        }
    }
}
