package by.sergo.cab.passengerservice.domain.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Value
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PassengerRatingResponseDto {
    Long passengerId;
    Double rating;
}
