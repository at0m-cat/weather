package matveyodintsov.weather.exeption;

public class LocationNotFoundDataBase extends RuntimeException {
    public LocationNotFoundDataBase(String message) {
        super(message);
    }
}
