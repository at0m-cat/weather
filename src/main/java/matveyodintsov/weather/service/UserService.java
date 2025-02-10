package matveyodintsov.weather.service;

import matveyodintsov.weather.exeption.AuthNotFoundException;
import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.model.Users;

public interface UserService <T> {

     void save(T user);

    T findByLoginAndPassword(T user);

    boolean existsByLogin(T user);

}
