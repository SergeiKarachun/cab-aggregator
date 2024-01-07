package by.sergo.rideservice.controller;

import by.sergo.rideservice.domain.dto.request.RideCreateUpdateRequestDto;
import by.sergo.rideservice.domain.dto.response.RideListResponseDto;
import by.sergo.rideservice.domain.dto.response.RideResponseDto;
import by.sergo.rideservice.domain.dto.response.StringResponse;
import by.sergo.rideservice.domain.enums.Status;
import by.sergo.rideservice.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/riders")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RideResponseDto> create(@RequestBody @Valid RideCreateUpdateRequestDto dto) {
        return ResponseEntity.ok(rideService.create(dto));
    }

    @PutMapping(value = "/set-driver/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RideResponseDto> setDriver(@RequestBody @Valid RideResponseDto dto,
                                                     @PathVariable("id") Long driverId) {
        return ResponseEntity.ok(rideService.setDriver(dto, driverId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<RideResponseDto> changeStatus(@RequestBody Status status,
                                                        @PathVariable("id") String rideId) {
        return ResponseEntity.ok(rideService.changeStatus(rideId, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StringResponse> deleteById(@PathVariable("id") String id) {
        rideService.deleteById(id);
        return ResponseEntity.ok(StringResponse.builder()
                .message("Ride with id=" + id + " has been deleted.")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponseDto> getById(@PathVariable("id") String id) {
        return ResponseEntity.ok(rideService.getById(id));
    }

    @GetMapping("/passenger/{id}")
    public ResponseEntity<RideListResponseDto> getByPassengerId(@PathVariable("id") Long id,
                                                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                                @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                                                @RequestParam(value = "orderBy", required = false) String orderBy) {
        var rideListResponseDto = rideService.getByPassengerId(id, page, size, orderBy);
        return ResponseEntity.ok(rideListResponseDto);
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<RideListResponseDto> getByDriverId(@PathVariable("id") Long id,
                                                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                             @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                                             @RequestParam(value = "orderBy", required = false) String orderBy) {
        var rideListResponseDto = rideService.getByDriverId(id, page, size, orderBy);
        return ResponseEntity.ok(rideListResponseDto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RideResponseDto> update(@RequestBody @Valid RideCreateUpdateRequestDto dto,
                                                  @PathVariable("id") String id) {
        var rideResponseDto = rideService.update(dto, id);
        return ResponseEntity.ok(rideResponseDto);
    }
}
