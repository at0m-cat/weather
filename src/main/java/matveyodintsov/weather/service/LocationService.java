package matveyodintsov.weather.service;

import java.util.List;

public interface LocationService<T, K> {

    public void save(T location);

    public T findCityLocationInDataBase(String city, K user);

    public List<T> findAllLocationsFromUser(K user);

}
