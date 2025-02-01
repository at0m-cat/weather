package matveyodintsov.weather.data;

import java.math.BigDecimal;

public class WeatherData {

    private String cityName;
    private String iconUrl;
    private BigDecimal temperature;
    private String description;
    private BigDecimal feelsLike;
    private BigDecimal windSpeed;
    private BigDecimal humidity;
    private BigDecimal pressure;

    public WeatherData() {
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String icon) {
        this.iconUrl = "https://openweathermap.org/img/wn/{icon}.png"
                .replace("{icon}", icon);
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(BigDecimal feelsLike) {
        this.feelsLike = feelsLike;
    }

    public BigDecimal getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(BigDecimal windSpeed) {
        this.windSpeed = windSpeed;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    public BigDecimal getPressure() {
        return pressure;
    }

    public void setPressure(BigDecimal pressure) {
        this.pressure = pressure;
    }
}
