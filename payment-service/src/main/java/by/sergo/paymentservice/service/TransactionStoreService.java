package by.sergo.paymentservice.service;

import by.sergo.paymentservice.domain.dto.response.AccountResponseDto;
import by.sergo.paymentservice.domain.dto.response.CreditCardResponseDto;
import by.sergo.paymentservice.domain.dto.response.ListTransactionStoreResponseDto;
import by.sergo.paymentservice.domain.dto.response.TransactionStoreResponseDto;
import by.sergo.paymentservice.domain.entity.TransactionStore;
import by.sergo.paymentservice.repository.AccountRepository;
import by.sergo.paymentservice.repository.CreditCardRepository;
import by.sergo.paymentservice.repository.TransactionStoreRepository;
import by.sergo.paymentservice.service.exception.BadRequestException;
import by.sergo.paymentservice.service.exception.ExceptionMessageUtil;
import by.sergo.paymentservice.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static by.sergo.paymentservice.domain.enums.UserType.PASSENGER;

@Service
@RequiredArgsConstructor
public class TransactionStoreService {
    private final ModelMapper modelMapper;
    private final TransactionStoreRepository transactionStoreRepository;
    private final AccountRepository accountRepository;
    private final CreditCardRepository creditCardRepository;

    public ListTransactionStoreResponseDto getDriverTransactionByDriverId(Long driverId, Integer page, Integer size, String orderBy) {
        var accountByDriverId = accountRepository.findByDriverId(driverId)
                .orElseThrow(() -> new NotFoundException(
                        ExceptionMessageUtil.getNotFoundMessage("Account", "driverId", driverId)));
        var pageRequest = getPageRequestForDriver(page, size, orderBy);
        var responsePage = transactionStoreRepository.findAllByAccountNumber(accountByDriverId.getAccountNumber(), pageRequest)
                .map(this::mapToDto);

        return ListTransactionStoreResponseDto.builder()
                .transactions(responsePage.getContent())
                .page(responsePage.getPageable().getPageNumber() + 1)
                .totalPages(responsePage.getTotalPages())
                .size(responsePage.getContent().size())
                .total((int) responsePage.getTotalElements())
                .sortedByField(orderBy)
                .build();
    }

    public ListTransactionStoreResponseDto getPassengerTransactionByPassengerId(Long passengerId, Integer page, Integer size, String orderBy) {
        var creditCard = creditCardRepository.findByUserIdAndAndUserType(passengerId, PASSENGER)
                .orElseThrow(() -> new NotFoundException(
                        ExceptionMessageUtil.getNotFoundMessage("Credit card", "passengerId", passengerId)));
        var pageRequest = getPageRequestForPassenger(page, size, orderBy);
        var responsePage = transactionStoreRepository.findAllByCreditCardNumber(creditCard.getCreditCardNumber(), pageRequest)
                .map(this::mapToDto);

        return ListTransactionStoreResponseDto.builder()
                .transactions(responsePage.getContent())
                .page(responsePage.getPageable().getPageNumber() + 1)
                .totalPages(responsePage.getTotalPages())
                .size(responsePage.getContent().size())
                .total((int) responsePage.getTotalElements())
                .sortedByField(orderBy)
                .build();
    }

    private TransactionStoreResponseDto mapToDto(TransactionStore transactionStore) {
        return modelMapper.map(transactionStore, TransactionStoreResponseDto.class);
    }

    private PageRequest getPageRequestForDriver(Integer page, Integer size, String field) {
        if (page < 1 || size < 1) {
            throw new BadRequestException(ExceptionMessageUtil.getInvalidRequestMessage(page, size));
        }

        if (field != null) {
            List<String> declaredFields = Arrays.stream(AccountResponseDto.class.getDeclaredFields())
                    .map(Field::getName)
                    .toList();
            if (!declaredFields.contains(field.toLowerCase())) {
                throw new BadRequestException(ExceptionMessageUtil.getInvalidSortingParamRequestMessage(field));
            }
            return PageRequest.of(page - 1, size).withSort(Sort.by(Sort.Order.asc(field.toLowerCase())));
        }
        if (field == null) {
            return PageRequest.of(page - 1, size);
        } else return PageRequest.of(0, 10);
    }

    private PageRequest getPageRequestForPassenger(Integer page, Integer size, String field) {
        if (page < 1 || size < 1) {
            throw new BadRequestException(ExceptionMessageUtil.getInvalidRequestMessage(page, size));
        }

        if (field != null) {
            List<String> declaredFields = Arrays.stream(CreditCardResponseDto.class.getDeclaredFields())
                    .map(Field::getName)
                    .toList();
            if (!declaredFields.contains(field.toLowerCase())) {
                throw new BadRequestException(ExceptionMessageUtil.getInvalidSortingParamRequestMessage(field));
            }
            return PageRequest.of(page - 1, size).withSort(Sort.by(Sort.Order.asc(field.toLowerCase())));
        }
        if (field == null) {
            return PageRequest.of(page - 1, size);
        } else return PageRequest.of(0, 10);
    }
}
