package matveyodintsov.weather.service;

import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.repository.SessionRepository;
import matveyodintsov.weather.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    SessionRepository sessionRepository;
    UserRepository userRepository;

    @Autowired
    public AuthService(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    
    public void login(Users user) {

    }

    public void logout(Users user) {

    }

    public void register(Users user) {

    }

    private void createCookie() {

    }

    private void deleteCookie() {

    }

}
