package matveyodintsov.weather.api;

import matveyodintsov.weather.model.Weather;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class WeatherApiImpTest {
    private MockWebServer mockWebServer;
    private WeatherApiImp weatherApi;

    @Value("${weather.api.key}")
    private String apiKey = "test_api_key";

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String mockGeoUrl = mockWebServer.url("/geo").toString();
        String mockWeatherUrl = mockWebServer.url("/weather").toString();

        weatherApi = new WeatherApiImp(apiKey, mockGeoUrl, mockWeatherUrl);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    /** ✅ Успешное получение погоды по городу */
    @Test
    void testGetWeatherByCity_Success() {
        String mockResponse = """
                {
                  "name": "Moscow",
                  "weather": [{ "description": "Clear sky", "icon": "01d" }],
                  "main": { "temp": 25.0, "feels_like": 23.0, "humidity": 60, "pressure": 1015 },
                  "wind": { "speed": 3.5 },
                  "coord": { "lat": 55.7558, "lon": 37.6173 }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponse)
                .setResponseCode(200));

        Weather weather = weatherApi.getWeatherByCity("Moscow");

        assertNotNull(weather);
        assertEquals("Moscow", weather.getCityName());
        assertEquals(25.0, weather.getTemperature().doubleValue());
        assertEquals("Clear sky", weather.getDescription());
    }

    @Test
    void testGetWeatherByLocation_Success() {
        String mockResponse = """
                {
                  "name": "Berlin",
                  "weather": [{ "description": "Cloudy", "icon": "03d" }],
                  "main": { "temp": 18.0, "feels_like": 16.0, "humidity": 70, "pressure": 1020 },
                  "wind": { "speed": 5.0 },
                  "coord": { "lat": 52.52, "lon": 13.405 }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponse)
                .setResponseCode(200));

        Weather weather = weatherApi.getWeatherByLocation("52.52", "13.405");

        assertNotNull(weather);
        assertEquals("Berlin", weather.getCityName());
        assertEquals(18.0, weather.getTemperature().doubleValue());
        assertEquals("Cloudy", weather.getDescription());
    }

    @Test
    void testGetWeatherByCity_NotFound() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("{ \"message\": \"city not found\" }"));

        assertThrows(RuntimeException.class, () -> weatherApi.getWeatherByCity("UnknownCity"));
    }

    @Test
    void testGetWeatherByCity_ServerError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("{ \"message\": \"internal server error\" }"));

        assertThrows(RuntimeException.class, () -> weatherApi.getWeatherByCity("Moscow"));
    }
}