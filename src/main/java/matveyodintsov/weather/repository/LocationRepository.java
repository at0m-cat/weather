package matveyodintsov.weather.repository;

import matveyodintsov.weather.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    void insertLocation(Location location);

    void deleteLocation(Location location);

}
