package by.vodorod.bank.service;

import by.vodorod.bank.entity.CurrencyDTO;
import by.vodorod.bank.exception.InvalidInputData;

import java.time.LocalDate;

public interface BankService {
    String loadAllCurrency(LocalDate date);

    CurrencyDTO getCurrency(Long id, LocalDate date) throws InvalidInputData;
}
