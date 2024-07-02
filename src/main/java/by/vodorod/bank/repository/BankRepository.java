package by.vodorod.bank.repository;

import by.vodorod.bank.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface BankRepository extends JpaRepository<Currency, Long> {
    public Currency findFirstByDateAndBankCurrencyId(LocalDate date, Long bankCurrencyId);
}
