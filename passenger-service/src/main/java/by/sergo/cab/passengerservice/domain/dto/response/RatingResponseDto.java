package by.sergo.cab.passengerservice.domain.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;



@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingResponseDto {
    private Long id;
    private Integer grade;
    private Long passengerId;
    private Long driverId;
}
