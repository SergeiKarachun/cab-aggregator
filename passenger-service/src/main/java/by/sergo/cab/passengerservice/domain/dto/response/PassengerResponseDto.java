package by.sergo.cab.passengerservice.domain.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PassengerResponseDto {
    Long id;
    String name;
    String surname;
    String email;
    String phone;
    Double rating;
}