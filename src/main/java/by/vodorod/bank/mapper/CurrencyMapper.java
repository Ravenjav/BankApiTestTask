package by.vodorod.bank.mapper;

import by.vodorod.bank.entity.Currency;
import by.vodorod.bank.entity.CurrencyDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CurrencyMapper {

    private static final ModelMapper modelMapper = new ModelMapper();


    static{
        modelMapper.addMappings(new PropertyMap<CurrencyDTO, Currency>(){

            @Override
            protected void configure() {
                map().setBankCurrencyId(source.getCurId());
                map().setDate(source.getDate());
                map().setAbbreviation(source.getCurAbbreviation());
                map().setScale(source.getCurScale());
                map().setName(source.getCurName());
                map().setRate(source.getCurOfficialRate());
                skip(destination.getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<Currency, CurrencyDTO>() {
            @Override
            protected void configure() {
                map().setCurId(source.getBankCurrencyId());
                map().setDate(source.getDate());
                map().setCurAbbreviation(source.getAbbreviation());
                map().setCurScale(source.getScale());
                map().setCurName(source.getName());
                map().setCurOfficialRate(source.getRate());
            }
        });
    }

    public static Currency mapDTOToEntity(CurrencyDTO currencyDTO){
        return modelMapper.map(currencyDTO, Currency.class);
    }

    public static CurrencyDTO mapEntityToDTO(Currency currency){
        return modelMapper.map(currency, CurrencyDTO.class);
    }

    public static List<Currency> mapDTOListToEntityList(List<CurrencyDTO> dtosList) {
        return dtosList.stream()
                .map(CurrencyMapper::mapDTOToEntity)
                .collect(Collectors.toList());
    }

    public static List<CurrencyDTO> mapEntityListToDTOList(List<Currency> list) {
        return list.stream()
                .map(CurrencyMapper::mapEntityToDTO)
                .collect(Collectors.toList());
    }
}
