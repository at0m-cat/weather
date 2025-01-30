package matveyodintsov.weather.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersDto {
    private Long id;
    private String login;
    private String password;
}
