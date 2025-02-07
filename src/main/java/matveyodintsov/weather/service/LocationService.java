package matveyodintsov.weather.service;

import matveyodintsov.weather.dto.LocationDto;
import matveyodintsov.weather.model.Location;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    // todo: если город уже есть в базе - брать его координаты
    //

    public void save(Location loc, Users user) {
        if (!locationRepository.existsByUserAndName(user, loc.getName())) {
            loc.setUser(user);
            loc.setName(loc.getName().toUpperCase());
            locationRepository.save(loc);
        }
    }

    public boolean existByName(String name) {
        return locationRepository.existsByName(name);
    }

    public List<Location> findByUser(Users user) {
        return locationRepository.findAllByUser(user);
    }

    public Location findByName(String name) {
        return Optional.ofNullable(locationRepository.findByName(name.toUpperCase()))
                .orElseThrow(() -> new NoSuchElementException("No location found with name: " + name));
    }

}
