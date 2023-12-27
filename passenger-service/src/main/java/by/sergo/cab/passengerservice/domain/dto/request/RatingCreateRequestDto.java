package by.sergo.cab.passengerservice.domain.dto.request;

import lombok.Value;

@Value
public class RatingCreateRequestDto {
    Integer grade;
    Long driverId;
}
