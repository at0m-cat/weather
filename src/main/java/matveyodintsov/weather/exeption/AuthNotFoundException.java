package matveyodintsov.weather.exeption;

public class AuthNotFoundException extends RuntimeException {
    public AuthNotFoundException(String message) {
        super(message);
    }
}