package matveyodintsov.weather.service;

import matveyodintsov.weather.dto.UserRegistrationDto;
import matveyodintsov.weather.exeption.AuthNotFoundException;
import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public Users login(UserRegistrationDto userDto) throws AuthNotFoundException {
        Users loggedUser = Mapper.UserMapper.mapUsersRegistrationDtoToUser(userDto);
        return userService.findByLoginAndPassword(loggedUser);
    }

    public void register(UserRegistrationDto userDto) throws AuthNotFoundException {
        Users newUser = Mapper.UserMapper.mapUsersRegistrationDtoToUser(userDto);
        if (!userService.existsByLogin(newUser)) {
            userService.save(newUser);
        }
    }
}
