package by.vodorod.bank.service;

import by.vodorod.bank.entity.Currency;
import by.vodorod.bank.entity.CurrencyDTO;
import by.vodorod.bank.exception.BankApiException;
import by.vodorod.bank.exception.InvalidInputData;
import by.vodorod.bank.exception.ProcessingException;
import by.vodorod.bank.mapper.CurrencyMapper;
import by.vodorod.bank.repository.BankRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Service
public class BankServiceImpl implements BankService{

    private final BankRepository bankRepository;

    private final ObjectMapper objectMapper;


    private static final String BASE_URL = "https://api.nbrb.by/exrates/rates";

    public BankServiceImpl(BankRepository bankRepository, ObjectMapper objectMapper, CurrencyMapper currencyMapper) {
        this.bankRepository = bankRepository;
        this.objectMapper = objectMapper;
    }

    public String loadAllCurrency(LocalDate date){
        try {
            System.out.println(date);
            loadCurrency(date, "0");
            loadCurrency(date, "1");
            return "Success";
        } catch (ProcessingException | BankApiException e) {
            return e.getMessage();
        }
    }

    public CurrencyDTO getCurrency(Long id, LocalDate date) throws InvalidInputData {
        try {
            System.out.println(bankRepository.findAll());
            Currency currency = bankRepository.findFirstByDateAndBankCurrencyId(date, id);
            System.out.println(currency);
            return CurrencyMapper.mapEntityToDTO(currency);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputData();
        }
    }

    private void loadCurrency(LocalDate date, String periodicity) throws ProcessingException, BankApiException {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("ondate", date);
        uriVariables.put("periodicity", periodicity);
        String url = BASE_URL + "?ondate={ondate}&periodicity={periodicity}";
        try {
            String jsonCurrency = restTemplate.getForObject(url, String.class, uriVariables);
            List<CurrencyDTO> currencyDTOList = objectMapper.readValue(jsonCurrency, new TypeReference<List<CurrencyDTO>>() {});
            List<Currency> currencyList = CurrencyMapper.mapDTOListToEntityList(currencyDTOList);
            bankRepository.saveAll(currencyList);
        } catch (JsonProcessingException e) {
            throw new ProcessingException();
        }catch (HttpClientErrorException e){
            System.out.println(e.getMessage());
            throw new BankApiException();
        }
    }
}
