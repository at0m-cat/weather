package matveyodintsov.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {

    private String iconUrl;
    private BigDecimal temperature;
    private String description;
    private BigDecimal feelsLike;
    private BigDecimal windSpeed;
    private BigDecimal humidity;
    private BigDecimal pressure;
    private Location location;

    public Weather() {
    }

    @JsonProperty("name")
    private String cityName;

    @JsonProperty("weather")
    private void unpackWeatherIconUrlFromArray(JsonNode weatherArray) {
        this.iconUrl = "https://openweathermap.org/img/wn/" + weatherArray.get(0).get("icon").asText() + ".png";
        this.description = weatherArray.get(0).get("description").asText();
    }

    @JsonProperty("main")
    private void unpackMain(JsonNode main) {
        this.temperature = BigDecimal.valueOf(main.get("temp").asDouble());
        this.feelsLike = BigDecimal.valueOf(main.get("feels_like").asDouble());
        this.humidity = BigDecimal.valueOf(main.get("humidity").asDouble());
        this.pressure = BigDecimal.valueOf(main.get("pressure").asDouble());
    }

    @JsonProperty("wind")
    private void unpackWind(JsonNode wind) {
        this.windSpeed = BigDecimal.valueOf(wind.get("speed").asDouble());
    }

    @JsonProperty("coord")
    public void setLocation(JsonNode coord) {
        this.location = new Location();
        this.location.setLatitude(coord.get("lat").decimalValue());
        this.location.setLongitude(coord.get("lon").decimalValue());
    }

    public String getCityName() {
        return cityName;
    }


    public String getIconUrl() {
        return iconUrl;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getFeelsLike() {
        return feelsLike;
    }

    public BigDecimal getWindSpeed() {
        return windSpeed;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public BigDecimal getPressure() {
        return pressure;
    }

    public Location getLocation() {
        return location;
    }

}