package by.sergo.cab.passengerservice.domain.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingCreateRequestDto {
    @NotBlank(message = "Garde is required")
    @Min(value = 1, message = "Min value is 1")
    @Max(value = 5, message = "Max value is 5")
    Integer grade;
    @NotBlank(message = "Driver is required")
    @Min(value = 1, message = "Min value is 1")
    Long driverId;
}
