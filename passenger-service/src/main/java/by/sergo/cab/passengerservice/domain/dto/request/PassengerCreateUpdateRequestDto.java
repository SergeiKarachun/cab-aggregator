package by.sergo.cab.passengerservice.domain.dto.request;

import lombok.Value;

@Value
public class PassengerCreateUpdateRequestDto {
    String name;
    String surname;
    String email;
    String phone;
}
