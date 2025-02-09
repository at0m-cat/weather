package matveyodintsov.weather.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import matveyodintsov.weather.dto.UserRegistrationDto;
import matveyodintsov.weather.dto.UsersDto;
import matveyodintsov.weather.exeption.CityNotFoundException;
import matveyodintsov.weather.model.Users;
import matveyodintsov.weather.model.Weather;

public class Mapper {

    public static class WeatherMapper {

        public static Weather mapJsonToWeather(JsonNode node) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.treeToValue(node, Weather.class);
            } catch (Exception e) {
                throw new CityNotFoundException("Weather data could not be parsed");
            }
        }
    }

    public static class UserMapper {

        public static UsersDto mapUsersToUsersDto(Users node) {
            UsersDto dto = new UsersDto();
            dto.setLogin(node.getLogin());
            return dto;
        }

        public static Users mapUsersDtoToUser(UsersDto dto) {
            Users user = new Users();
            user.setLogin(dto.getLogin());
            return user;
        }

        public static Users mapUsersRegistrationDtoToUser(UserRegistrationDto dto) {
            Users user = new Users();
            user.setLogin(dto.getLogin().toLowerCase());
            user.setPassword(dto.getPassword());
            return user;
        }
    }

}
