package matveyodintsov.weather.service;

import matveyodintsov.weather.dto.UsersDto;
import matveyodintsov.weather.exeption.AuthNotFoundException;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.repository.SessionRepository;
import matveyodintsov.weather.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private SessionRepository sessionRepository;
    private UserRepository userRepository;

    @Autowired
    public AuthService(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public void login(UsersDto userDto) throws AuthNotFoundException {
        Users userFind = null;
        userFind = userRepository.findByLoginAndPassword(mapToUsers(userDto));
        if (userFind == null) {
            throw new AuthNotFoundException("Invalid username or password");
        }
    }

    public void logout(UsersDto user) {

    }

    public void register(UsersDto userDto) {
        Users user = mapToUsers(userDto);
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new AuthNotFoundException("Login already exists");
        } else {
            userRepository.save(user);
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
