package matveyodintsov.weather.dto;

import java.math.BigDecimal;

public class LocationDto {
    private Long id;
    private UsersDto userId;
    private BigDecimal latitude;
    private BigDecimal longitude;


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

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "LocationDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
