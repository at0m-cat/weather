package matveyodintsov.weather.service;

import matveyodintsov.weather.exeption.LocationNotFoundDataBase;
import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.model.Location;
import matveyodintsov.weather.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

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

    public Location findCityLocationInDataBase(String city, Account user) throws LocationNotFoundDataBase {
        if (existsByName(city)) {
            return findByName(city);
        }
        if (existsByUserAndName(user, city)) {
            return findByUserAndName(user, city);
        }
        throw new LocationNotFoundDataBase("Location not found");
    }

    public List<Location> findAllLocationsFromUser(Account user) {
        return locationRepository.findAllByUser(user);
    }

    private boolean existsByUserAndName(Account user, String name) {
        return locationRepository.existsByUserAndName(user, name.toUpperCase());
    }

    private boolean existsByName(String name) {
        return locationRepository.existsByName(name.toUpperCase());
    }

    private Location findByUserAndName(Account user, String name) {
        return locationRepository.findByUserAndName(user, name.toUpperCase())
                .orElseThrow(() -> new LocationNotFoundDataBase("City not found in DB: " + name));
    }

    private Location findByName(String name) {
        return locationRepository.findByName(name.toUpperCase())
                .orElseThrow(() -> new LocationNotFoundDataBase("City not found in DB: " + name));
    }

}
