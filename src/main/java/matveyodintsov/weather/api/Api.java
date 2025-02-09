package matveyodintsov.weather.api;

public interface Api<T> {

    T getWeatherByLocation(String lat, String lon);
    T getWeatherByCity(String city);

}
