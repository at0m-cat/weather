package matveyodintsov.weather.service.impl;

import matveyodintsov.weather.exeption.LocationNotFoundDataBase;
import matveyodintsov.weather.model.Account;
import matveyodintsov.weather.model.Location;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.repository.LocationRepository;
import matveyodintsov.weather.service.LocationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LocationServiceImplTest {

    private final LocationRepository locationRepository = Mockito.mock(LocationRepository.class);
    private final LocationService<Location, Account> locationService = new LocationServiceImp(locationRepository);

    @Test
    @DisplayName("test save when exist user location")
    public void testSaveWhenExistUserLocation() {
        Users user = new Users();
        user.setLogin("testLogin");
        user.setId(1L);
        user.setPassword("testPassword");

        Location location = new Location();
        location.setId(1L);
        location.setLatitude(BigDecimal.TEN);
        location.setLongitude(BigDecimal.TEN);
        location.setName("testLocation");
        location.setUser(user);

        when(locationRepository.existsByUserAndName(location.getUser(), location.getName().toUpperCase())).thenReturn(true);
        locationService.save(location);
        verify(locationRepository, times(1)).existsByUserAndName(location.getUser(), location.getName().toUpperCase());
        verify(locationRepository, times(0)).save(location);
    }

    @Test
    @DisplayName("test save when not exist user location")
    public void testSaveWhenNotExistUserLocation() {
        Users user = new Users();
        user.setLogin("testLogin");
        user.setId(1L);
        user.setPassword("testPassword");

        Location location = new Location();
        location.setId(1L);
        location.setLatitude(BigDecimal.TEN);
        location.setLongitude(BigDecimal.TEN);
        location.setName("testLocation");
        location.setUser(user);

        when(locationRepository.existsByUserAndName(location.getUser(), location.getName().toUpperCase())).thenReturn(false);
        locationService.save(location);
        verify(locationRepository, times(1)).existsByUserAndName(location.getUser(), location.getName().toUpperCase());
        verify(locationRepository, times(1)).save(location);
    }

    @Test
    @DisplayName("test delete user location by city name")
    public void testDeleteLocation() {
        Users user = new Users();
        user.setLogin("testLogin");
        user.setId(1L);
        user.setPassword("testPassword");
        Location location = new Location();
        location.setId(1L);
        location.setLatitude(BigDecimal.TEN);
        location.setLongitude(BigDecimal.TEN);
        location.setName("testLocation");
        location.setUser(user);

        locationService.deleteUserLocation(location.getName(), user);
        verify(locationRepository, times(1)).deleteLocationByUser(location.getName(), user);
    }

    @Test
    @DisplayName("test findCityLocationInDataBase when true exist by cityName")
    public void testFindCityLocationInDataBaseWhenExistCityName() {
        Users user = new Users();
        user.setLogin("testLogin");
        user.setId(1L);
        user.setPassword("testPassword");
        Location location = new Location();
        location.setId(1L);
        location.setLatitude(BigDecimal.TEN);
        location.setLongitude(BigDecimal.TEN);
        location.setName("testCity");
        location.setUser(user);

        when(locationRepository.existsByName(location.getName().toUpperCase())).thenReturn(true);
        when(locationRepository.findByName(location.getName().toUpperCase())).thenReturn(Optional.of(location));

        assertEquals(locationService.findCityLocationInDataBase(location.getName(), user), location);

        verify(locationRepository, times(1)).findByName(location.getName().toUpperCase());
        verify(locationRepository, times(1)).existsByName(location.getName().toUpperCase());
    }

    @Test
    @DisplayName("test findCityLocationInDataBase when true exist by user and cityName")
    public void testFindCityLocationInDataBaseWhenExistUserAndCityName() {
        Users user = new Users();
        user.setLogin("testLogin");
        user.setId(1L);
        user.setPassword("testPassword");
        Location location = new Location();
        location.setId(1L);
        location.setLatitude(BigDecimal.TEN);
        location.setLongitude(BigDecimal.TEN);
        String cityName = "testCity";
        location.setName(cityName);
        location.setUser(user);

        when(locationRepository.existsByName(cityName.toUpperCase())).thenReturn(false);
        when(locationRepository.existsByUserAndName(user, cityName.toUpperCase())).thenReturn(true);
        when(locationRepository.findByUserAndName(user, cityName.toUpperCase())).thenReturn(Optional.of(location));

        assertEquals(locationService.findCityLocationInDataBase(cityName, user), location);

        verify(locationRepository, times(1)).existsByUserAndName(user, cityName.toUpperCase());
        verify(locationRepository, times(1)).existsByName(cityName.toUpperCase());
        verify(locationRepository, times(1)).findByUserAndName(user, cityName.toUpperCase());

    }

    @Test
    @DisplayName("test findCityLocationInDataBase when not exist location in database")
    public void testFindCityLocationInDataBaseWhenNotExistLocation() {
        Users user = new Users();
        user.setLogin("testLogin");
        user.setId(1L);
        user.setPassword("testPassword");
        Location location = new Location();
        location.setId(1L);
        location.setLatitude(BigDecimal.TEN);
        location.setLongitude(BigDecimal.TEN);
        String cityName = "testCity";
        location.setName(cityName);
        location.setUser(user);

        when(locationRepository.existsByName(cityName.toUpperCase())).thenReturn(false);
        when(locationRepository.existsByUserAndName(user, cityName.toUpperCase())).thenReturn(true);

        assertThrows(LocationNotFoundDataBase.class, () -> locationService.findCityLocationInDataBase(cityName, user));

        verify(locationRepository, times(1)).existsByUserAndName(user, cityName.toUpperCase());
        verify(locationRepository, times(1)).existsByName(cityName.toUpperCase());

    }

    @Test
    @DisplayName("test findAllLocationsFromUser when null user locations then return empty list")
    public void testFindAllLocationsFromUserWhenNullUserLocations() {

        Users users = new Users();
        users.setLogin("testLogin");
        users.setId(1L);
        users.setPassword("testPassword");

        when(locationRepository.findAllByUser(users)).thenReturn(Collections.emptyList());

        assertEquals(Collections.emptyList(), locationService.findAllLocationsFromUser(users));

        verify(locationRepository, times(1)).findAllByUser(users);

    }

    @Test
    @DisplayName("test findAllLocationsFromUser when locations exist then return ArrayList")
    public void testFindAllLocationsFromUserWhenLocationsExist() {
        Users users = new Users();
        users.setLogin("testLogin");
        users.setId(1L);
        users.setPassword("testPassword");
        Location location = new Location();
        location.setId(1L);
        location.setLatitude(BigDecimal.TEN);
        location.setLongitude(BigDecimal.TEN);
        location.setName("testLocation");
        location.setUser(users);

        Location location2 = new Location();
        location2.setId(2L);
        location2.setLatitude(BigDecimal.TEN);
        location2.setLongitude(BigDecimal.TEN);
        location.setName("testLocation2");
        location.setUser(users);

        List<Location> locations = new ArrayList<>();
        locations.add(location);
        locations.add(location2);

        when(locationRepository.findAllByUser(users)).thenReturn(locations);
        assertEquals(locationService.findAllLocationsFromUser(users), locations);
    }
}
