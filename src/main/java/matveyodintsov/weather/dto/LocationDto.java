package matveyodintsov.weather.dto;

import java.math.BigDecimal;

public class LocationDto {
    private Long id;
    private UsersDto user;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsersDto getUser() {
        return user;
    }

    public void setUser(UsersDto user) {
        this.user = user;
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
                ", userId=" + user +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
