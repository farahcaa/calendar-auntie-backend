package com.calendar_auntie.controllers;

import com.calendar_auntie.model.dtos.CheckoutPricingDTO;
import com.calendar_auntie.services.ConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/config")
public class AdminConfigController {

  private final ConfigService configService;

  public AdminConfigController(ConfigService configService) {
    this.configService = configService;
  }


  @PostMapping
  public ResponseEntity<Object> postConfig(@RequestBody CheckoutPricingDTO dto) {
    configService.setConfig(dto);
    return ResponseEntity.ok(configService.getCheckoutPricing());
  }

}
