package matveyodintsov.weather.service;

import matveyodintsov.weather.dto.UserRegistrationDto;
import matveyodintsov.weather.exeption.AuthNotFoundException;
import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public void save(Account user) {
        Users saveUser = new Users();
        saveUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        saveUser.setLogin(user.getLogin());
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(saveUser);
    }

    public Account findByLoginAndPassword(Account user) throws AuthNotFoundException {
        Account foundUser = userRepository.findByLogin(user.getLogin());
        if (foundUser == null || !bCryptPasswordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            throw new AuthNotFoundException("Invalid username or password");
        }
        return foundUser;
    }

    public boolean existsByLogin(Account user) throws AuthNotFoundException {
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new AuthNotFoundException("Login already exists");
        }
        return false;
    }
}
