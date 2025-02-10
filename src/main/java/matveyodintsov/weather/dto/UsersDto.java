package matveyodintsov.weather.dto;

import matveyodintsov.weather.model.Account;

public class UsersDto implements Account {
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
