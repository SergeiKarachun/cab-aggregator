package by.sergo.cab.passengerservice.controller;

import by.sergo.cab.passengerservice.domain.dto.response.PassengerRatingResponseDto;
import by.sergo.cab.passengerservice.domain.dto.request.RatingCreateRequestDto;
import by.sergo.cab.passengerservice.domain.dto.response.RatingResponseDto;
import by.sergo.cab.passengerservice.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/passengers/{id}/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RatingResponseDto> createRateOfPassenger(@RequestBody @Valid RatingCreateRequestDto dto,
                                                                   @PathVariable("id") Long passengerId) {
        RatingResponseDto responseDto = ratingService.createRateOfPassenger(dto, passengerId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping()
    public ResponseEntity<PassengerRatingResponseDto> getPassengerRating(@PathVariable("id") Long passengerId) {
        return ResponseEntity.ok(ratingService.getPassengerRating(passengerId));
    }

}
