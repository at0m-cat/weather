package matveyodintsov.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import matveyodintsov.weather.exeption.IncorrectCityNameValue;
import matveyodintsov.weather.exeption.LocationNotFoundDataBase;
import matveyodintsov.weather.model.Location;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public void save(Location location) {
        if (!existsByUserAndName(location.getUser(), location.getName().toUpperCase())) {
            location.setName(location.getName().toUpperCase());
            locationRepository.save(location);
        }
    }

    public Location findCityLocationInDataBase(String city, Users user)
            throws LocationNotFoundDataBase, IncorrectCityNameValue {

        if (existsByName(city)) {
            return findByName(city);
        }

        if (existsByUserAndName(user, city)) {
            return findByUserAndName(user, city);
        }

        throw new LocationNotFoundDataBase("Location not found");
    }

    public Location createLocation (JsonNode node, Users user) {
        Location location = new Location();
        location.setName(node.get("name").asText().toUpperCase());
        location.setLatitude(node.get("coord").get("lon").decimalValue());
        location.setLongitude(node.get("coord").get("lat").decimalValue());
        location.setUser(user);
        return location;
    }

    public List<Location> findAllLocationsFromUser(Users user) {
        return locationRepository.findAllByUser(user);
    }

    private boolean existsByUserAndName(Users user, String name) {
        return locationRepository.existsByUserAndName(user, name.toUpperCase());
    }

    private boolean existsByName(String name) {
        return locationRepository.existsByName(name.toUpperCase());
    }

    private Location findByUserAndName(Users user, String name) {
        return locationRepository.findByUserAndName(user, name.toUpperCase())
                .orElseThrow(() -> new LocationNotFoundDataBase("City not found in DB: " + name));
    }

    private Location findByName(String name) {
        return locationRepository.findByName(name.toUpperCase())
                .orElseThrow(() -> new LocationNotFoundDataBase("City not found in DB: " + name));
    }


}
