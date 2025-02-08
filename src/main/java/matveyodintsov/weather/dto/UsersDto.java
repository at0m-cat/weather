package matveyodintsov.weather.dto;

public class UsersDto {
    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "UsersDto{" +
                "login='" + login + '\'' +
                '}';
    }
}
