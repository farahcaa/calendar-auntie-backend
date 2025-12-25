package com.calendar_auntie.services;

import com.calendar_auntie.model.dtos.CheckoutPricingDTO;
import com.calendar_auntie.model.entities.Config;
import com.calendar_auntie.model.mappers.ConfigMapper;
import com.calendar_auntie.model.repositories.ConfigRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {
  private final ConfigRepository configRepository;
  private final ConfigMapper configMapper;

  public ConfigService(ConfigMapper configMapper,ConfigRepository configRepository)
  {
    this.configMapper=configMapper;
    this.configRepository = configRepository;
  }

  public CheckoutPricingDTO getCheckoutPricing()
  {
    Optional<Config> config = configRepository.findById("default");

    return config.map(configMapper::toCheckoutPricingDTO).orElse(null);
  }

  public void setConfig(CheckoutPricingDTO checkoutPricingDTO){
    Optional<Config> configOptional = configRepository.findById("default");
    Config config;
    if(configOptional.isPresent()){
      config = configOptional.get();
    }
    config.setShipping(checkoutPricingDTO.shipping());
    config.setTax(checkoutPricingDTO.tax());
    configRepository.save(config);
  }
}
