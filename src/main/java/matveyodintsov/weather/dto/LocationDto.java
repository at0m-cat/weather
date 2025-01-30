package matveyodintsov.weather.dto;

import lombok.Builder;
import lombok.Data;
import matveyodintsov.weather.model.Users;

import java.math.BigDecimal;

@Data
@Builder
public class LocationDto {
    private Long id;
    private UsersDto userId;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
