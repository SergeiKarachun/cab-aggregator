package by.sergo.cab.passengerservice.controller.handler;

import by.sergo.cab.passengerservice.service.exception.BadRequestException;
import by.sergo.cab.passengerservice.service.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(basePackages = {"by.sergo.cab.passengerservice.controller"})
public class RestControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<RestErrorResponse> handleNotFoundException(NotFoundException ex) {
        return createResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<RestErrorResponse> handleBadRequestException(BadRequestException ex) {
        return createResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var collect = ex.getBindingResult().getAllErrors().stream()
                .map(er -> er.getDefaultMessage()).collect(Collectors.toList());
        return new ResponseEntity<>(RestErrorResponse.builder()
                .messages(collect)
                .status(HttpStatus.BAD_REQUEST)
                .time(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<RestErrorResponse> handleServerException(ServerException ex) {
        return createResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<RestErrorResponse> createResponse(Exception ex, HttpStatus status) {
        return new ResponseEntity<>(RestErrorResponse.builder()
                .messages(Collections.singletonList(ex.getMessage()))
                .status(status)
                .time(LocalDateTime.now())
                .build(), status);
    }
}
