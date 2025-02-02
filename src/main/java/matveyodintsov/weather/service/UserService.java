package matveyodintsov.weather.service;

import matveyodintsov.weather.exeption.AuthNotFoundException;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public void save(Users user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Users findByLoginAndPassword(Users user) {
        Users foundUser = userRepository.findByLogin(user.getLogin());
        if (foundUser == null || !bCryptPasswordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            throw new AuthNotFoundException("Invalid username or password");
        }
        return foundUser;
    }

    public boolean existsByLogin(Users user) {
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new AuthNotFoundException("Login already exists");
        }
        return false;
    }

}
