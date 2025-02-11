package matveyodintsov.weather.service;

import java.util.List;

public interface LocationService<T, K> {

    void save(T location);

    void deleteUserLocation(String city, K user);

    T findCityLocationInDataBase(String city, K user);

    List<T> findAllLocationsFromUser(K user);

}
