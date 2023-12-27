package by.sergo.cab.passengerservice.domain.dto.response;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PassengerRatingResponseDto {
    Long passengerId;
    Double rating;
}
