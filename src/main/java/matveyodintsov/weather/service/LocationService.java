package matveyodintsov.weather.service;

import matveyodintsov.weather.dto.LocationDto;
import matveyodintsov.weather.model.Location;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    // todo: если город уже есть в базе - брать его координаты
    //

    public void save(LocationDto locationDto, Users user) {
        Location location;
        if (!locationRepository.existsByUserAndName(user, locationDto.getName())) {
            location = new Location();
            location.setLatitude(locationDto.getLatitude());
            location.setLongitude(locationDto.getLongitude());
            location.setUser(user);
            location.setName(locationDto.getName());
            locationRepository.save(location);
        }

    }

    public List<Location> findByUser(Users user) {
        return locationRepository.findAllByUser(user);
    }

}
