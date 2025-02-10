package matveyodintsov.weather.service;

import java.util.List;

public interface WeatherService<T, K> {


    public void insertUserLocation(String city, K user);

    public List<T> getUserWeathers(K user);
}
