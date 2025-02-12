package matveyodintsov.weather.service.impl;

import matveyodintsov.weather.exeption.AuthNotFoundException;
import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.repository.UserRepository;
import matveyodintsov.weather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService<Account> {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public void save(Account user) {
        Users saveUser;
        saveUser = new Users();
        saveUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        saveUser.setLogin(user.getLogin());
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
        return userRepository.existsByLogin(user.getLogin());
    }
}
