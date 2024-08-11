package com.DigitalMoneyHouse.msvc_cards.dto;
import com.DigitalMoneyHouse.msvc_cards.entity.Card;
import com.DigitalMoneyHouse.msvc_cards.entity.CreditCard;
import com.DigitalMoneyHouse.msvc_cards.entity.DebitCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ICardMapper {

    ICardMapper INSTANCE = Mappers.getMapper(ICardMapper.class);

    @Mapping(target = "creditLimit", source = "creditLimit", ignore = true)
    @Mapping(target = "balance", source = "balance", ignore = true)
    Card toEntity(CardDTO cardDTO);

    @Mapping(target = "creditLimit", source = "creditLimit")
    CreditCard toCreditCard(CardDTO cardDTO);

    @Mapping(target = "balance", source = "balance")
    DebitCard toDebitCard(CardDTO cardDTO);
}
