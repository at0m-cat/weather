package matveyodintsov.weather.dto;

import matveyodintsov.weather.model.Account;

public class UserRegistrationDto implements Account {

    private String login;
    private String password;
    private String repeatPassword;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
