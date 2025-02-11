package matveyodintsov.weather.exeption;

public class LocationLimitExceeded extends RuntimeException {
    public LocationLimitExceeded(String message) {
        super(message);
    }
}
