package matveyodintsov.weather.dto;

import java.util.Date;

public class SessionsDto {
    private Long id;
    private UsersDto userId;
    private Date ExpiresAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsersDto getUserId() {
        return userId;
    }

    public void setUserId(UsersDto userId) {
        this.userId = userId;
    }

    public Date getExpiresAt() {
        return ExpiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        ExpiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return "SessionsDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", ExpiresAt=" + ExpiresAt +
                '}';
    }
}
