package by.sergo.driverservice.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverCreateUpdateRequestDto {
    @NotBlank(message = "Name is required")
    String name;
    @NotBlank(message = "Surname is required")
    String surname;
    @NotBlank(message = "Email is required")
    @Schema(example = "example@gmail.com")
    @Email(message = "Email pattern is example@gmail.com")
    String email;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+375(9|33|44|25)(\\d{7})$", message = "Phone pattern is +375331234567. Valid operator codes 25,29,33,44")
    String phone;

}
