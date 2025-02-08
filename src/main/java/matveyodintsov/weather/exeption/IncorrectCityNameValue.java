package matveyodintsov.weather.exeption;

public class IncorrectCityNameValue extends RuntimeException {
    public IncorrectCityNameValue(String message) {
        super(message);
    }
}
