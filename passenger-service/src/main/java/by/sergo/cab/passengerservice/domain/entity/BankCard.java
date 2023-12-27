package by.sergo.cab.passengerservice.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class BankCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long cardNumber;

    BigDecimal balance;


}
