package com.calendar_auntie.model.mappers;

import com.calendar_auntie.model.dtos.CheckoutPricingDTO;
import com.calendar_auntie.model.entities.Config;
import org.mapstruct.Mapper;

@Mapper
public interface ConfigMapper {

  CheckoutPricingDTO toCheckoutPricingDTO(Config config);
}
