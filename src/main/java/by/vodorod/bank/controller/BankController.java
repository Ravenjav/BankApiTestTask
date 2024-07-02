package by.vodorod.bank.controller;

import by.vodorod.bank.exception.InvalidInputData;
import by.vodorod.bank.service.BankServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Tag(name = "Банковские операции", description = "API для работы с банковскими данными")
public class BankController {

    private final BankServiceImpl bankServiceImpl;


    public BankController(BankServiceImpl bankServiceImpl) {
        this.bankServiceImpl = bankServiceImpl;
    }

    @Operation(summary = "Загрузить курсы валют",
            description = "Загружает курсы валют для указанной даты. " +
                    "Дата должна быть в формате YYYY-MM-DD.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение курсов валют"),
            @ApiResponse(responseCode = "400", description = "Некорректный формат даты")
    })
    @RequestMapping(value = "/loadCurrency/{date}", method = RequestMethod.GET)
    public ResponseEntity<?> loadCurrency(@PathVariable String date){
        LocalDate parseDate;
        try {
            parseDate = LocalDate.parse(date);
        }catch (Exception e){
            String errorMessage = "Invalid data format";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(bankServiceImpl.loadAllCurrency(parseDate), HttpStatus.OK);
    }

    @Operation(summary = "Получить курс валюты",
            description = "Возвращает курс валюты по указанному ID и дате.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение курса валюты"),
            @ApiResponse(responseCode = "400", description = "Некорректный формат ID или даты, или дата в будущем"),
            @ApiResponse(responseCode = "404", description = "Курс валюты с указанным ID не найден")
    })
    @RequestMapping(value = "/getCurrency/{id}/{date}", method = RequestMethod.GET)
    public ResponseEntity<?> getCurrency(
            @Parameter(description = "ID валюты", required = true)
            @PathVariable String id,
            @Parameter(description = "Дата в формате YYYY-MM-DD", required = true)
            @PathVariable String date){

        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            String errorMessage = "Invalid id format";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        LocalDate parseDate;
        try {
            parseDate = LocalDate.parse(date);
        }catch (Exception e){
            String errorMessage = "Invalid data format";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        if (parseDate.isAfter(LocalDate.now())) {
            String errorMessage = "Date cannot be in the future";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        try {
            return new ResponseEntity<>(bankServiceImpl.getCurrency(parsedId, parseDate), HttpStatus.OK);
        } catch (InvalidInputData e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
