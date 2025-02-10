package matveyodintsov.weather.service;

public interface AuthService<T> {

    public T login(T user);

    public void register(T user);

}
