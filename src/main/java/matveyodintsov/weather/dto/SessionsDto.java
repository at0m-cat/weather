package matveyodintsov.weather.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SessionsDto {
    private Long id;
    private UsersDto userId;
    private Date ExpiresAt;
}
