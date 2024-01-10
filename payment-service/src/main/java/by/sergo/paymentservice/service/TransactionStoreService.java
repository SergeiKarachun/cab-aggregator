package by.sergo.paymentservice.service;

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


import static by.sergo.paymentservice.domain.enums.UserType.PASSENGER;

@Service
@RequiredArgsConstructor
public class TransactionStoreService {
    private final ModelMapper modelMapper;
    private final TransactionStoreRepository transactionStoreRepository;
    private final AccountRepository accountRepository;
    private final CreditCardRepository creditCardRepository;

    public ListTransactionStoreResponseDto getDriverTransactionByDriverId(Long driverId, Integer page, Integer size) {
        var accountByDriverId = accountRepository.findByDriverId(driverId)
                .orElseThrow(() -> new NotFoundException(
                        ExceptionMessageUtil.getNotFoundMessage("Account", "driverId", driverId)));
        var pageRequest = getPageRequest(page, size);
        var responsePage = transactionStoreRepository.findAllByAccountNumber(accountByDriverId.getAccountNumber(), pageRequest)
                .map(this::mapToDto);

        return ListTransactionStoreResponseDto.builder()
                .transactions(responsePage.getContent())
                .page(responsePage.getPageable().getPageNumber() + 1)
                .totalPages(responsePage.getTotalPages())
                .size(responsePage.getContent().size())
                .total((int) responsePage.getTotalElements())
                .sortedByField("operationDate")
                .build();
    }

    public ListTransactionStoreResponseDto getPassengerTransactionByPassengerId(Long passengerId, Integer page, Integer size) {
        var creditCard = creditCardRepository.findByUserIdAndUserType(passengerId, PASSENGER)
                .orElseThrow(() -> new NotFoundException(
                        ExceptionMessageUtil.getNotFoundMessage("Credit card", "passengerId", passengerId)));
        var pageRequest = getPageRequest(page, size);
        var responsePage = transactionStoreRepository.findAllByCreditCardNumber(creditCard.getCreditCardNumber(), pageRequest)
                .map(this::mapToDto);

        return ListTransactionStoreResponseDto.builder()
                .transactions(responsePage.getContent())
                .page(responsePage.getPageable().getPageNumber() + 1)
                .totalPages(responsePage.getTotalPages())
                .size(responsePage.getContent().size())
                .total((int) responsePage.getTotalElements())
                .sortedByField("operationDate")
                .build();
    }

    private TransactionStoreResponseDto mapToDto(TransactionStore transactionStore) {
        return modelMapper.map(transactionStore, TransactionStoreResponseDto.class);
    }

    private PageRequest getPageRequest(Integer page, Integer size) {
        if (page < 1 || size < 1) {
            throw new BadRequestException(ExceptionMessageUtil.getInvalidRequestMessage(page, size));
        }

        return PageRequest.of(page - 1, size).withSort(Sort.by(Sort.Order.asc("operationDate")));
    }
}