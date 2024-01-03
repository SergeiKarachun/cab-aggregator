package by.sergo.cab.passengerservice.service;

import by.sergo.cab.passengerservice.domain.dto.request.PassengerCreateUpdateRequestDto;
import by.sergo.cab.passengerservice.domain.dto.response.PassengerListResponseDto;
import by.sergo.cab.passengerservice.domain.dto.response.PassengerResponseDto;
import by.sergo.cab.passengerservice.domain.entity.Passenger;
import by.sergo.cab.passengerservice.repository.PassengerRepository;
import by.sergo.cab.passengerservice.service.exception.BadRequestException;
import by.sergo.cab.passengerservice.service.exception.ExceptionMessageUtil;
import by.sergo.cab.passengerservice.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PassengerService {

    private final ModelMapper modelMapper;
    private final PassengerRepository passengerRepository;

    @Transactional
    public PassengerResponseDto create(PassengerCreateUpdateRequestDto dto) {
        checkIsPassengerUnique(dto);
        var savedPassenger = passengerRepository.save(mapToEntity(dto));
        return mapToDto(savedPassenger);
    }

    @Transactional
    public PassengerResponseDto update(Long id, PassengerCreateUpdateRequestDto dto) {
        var passengerToUpdate = getByIdOrElseThrow(id);

        checkIsPassengerForUpdateUnique(dto, passengerToUpdate);

        var passenger = mapToEntity(dto);
        passenger.setId(passengerToUpdate.getId());
        return mapToDto(passengerRepository.save(passenger));
    }

    @Transactional
    public void delete(Long id) {
        if (passengerRepository.existsById(id)) {
            passengerRepository.deleteById(id);
        } else throw new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Passenger", "id", id));
    }

    public PassengerResponseDto getById(Long id) {
        var passenger = getByIdOrElseThrow(id);
        return mapToDto(passenger);
    }

    public PassengerResponseDto getByPhone(String phone) {
        var passenger = passengerRepository.findByPhone(phone)
                .orElseThrow(() -> new NotFoundException(
                        ExceptionMessageUtil.getNotFoundMessage("Passenger", "phone", phone)
                ));
        return mapToDto(passenger);
    }

    public PassengerListResponseDto getAll() {
        List<PassengerResponseDto> passengers = passengerRepository.findAll().stream()
                .map(entity -> mapToDto(entity))
                .collect(Collectors.toList());

        return PassengerListResponseDto.builder()
                .passengers(passengers)
                .size(passengers.size())
                .total(passengers.size())
                .build();
    }

    public PassengerListResponseDto getAll(Integer page, Integer size, String field) {
        PageRequest pageRequest = getPageRequest(page, size, field);
        var responsePage = passengerRepository.findAll(pageRequest)
                .map(this::mapToDto);
        return PassengerListResponseDto.builder()
                .passengers(responsePage.getContent())
                .page(responsePage.getPageable().getPageNumber() + 1)
                .totalPages(responsePage.getTotalPages())
                .size(responsePage.getContent().size())
                .total((int) responsePage.getTotalElements())
                .sortedByField(field)
                .build();
    }

    private PageRequest getPageRequest(Integer page, Integer size, String field) {
        if  (page < 1 || size < 1) {
            throw new BadRequestException(ExceptionMessageUtil.getInvalidRequestMessage(page, size));
        }

        if (field != null) {
            List<String> declaredFields = Arrays.stream(PassengerResponseDto.class.getDeclaredFields())
                    .map(Field::getName)
                    .toList();
            if (!declaredFields.contains(field.toLowerCase())) {
                throw new BadRequestException(ExceptionMessageUtil.getInvalidSortingParamRequestMessage(field));
            }
            return PageRequest.of(page - 1, size).withSort(Sort.by(Sort.Order.asc(field.toLowerCase())));
        }
        if (field == null) {
            return PageRequest.of(page - 1, size);
        }
        else return PageRequest.of(0, 10);
    }

    private void checkIsPassengerForUpdateUnique(PassengerCreateUpdateRequestDto dto, Passenger entity) {
        if (!Objects.equals(dto.getEmail(), entity.getEmail())) {
            checkEmailIsUnique(dto);
        }

        if (!Objects.equals(dto.getPhone(), entity.getPhone())) {
            checkPhoneIsUnique(dto);
        }
    }

    private Passenger getByIdOrElseThrow(Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ExceptionMessageUtil.getNotFoundMessage("Passenger", "id", id)
                ));
    }

    private void checkIsPassengerUnique(PassengerCreateUpdateRequestDto dto) {
        checkEmailIsUnique(dto);
        checkPhoneIsUnique(dto);
    }

    private void checkPhoneIsUnique(PassengerCreateUpdateRequestDto dto) {
        if (passengerRepository.existsByPhone(dto.getPhone())) {
            throw new BadRequestException(
                    ExceptionMessageUtil.getAlreadyExistMessage("Passenger", "phone", dto.getPhone()));
        }
    }

    private void checkEmailIsUnique(PassengerCreateUpdateRequestDto dto) {
        if (passengerRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException(
                    ExceptionMessageUtil.getAlreadyExistMessage("Passenger", "email", dto.getEmail()));
        }
    }

    private Passenger mapToEntity(PassengerCreateUpdateRequestDto passengerResponseDto) {
        return modelMapper.map(passengerResponseDto, Passenger.class);
    }

    private PassengerResponseDto mapToDto(Passenger passenger) {
        return modelMapper.map(passenger, PassengerResponseDto.class);
    }

}
