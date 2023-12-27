package by.sergo.cab.passengerservice.domain.dto;

import lombok.Value;

@Value
public class RatingCreateRequestDto {
    Integer grade;
    Long driverId;
}
