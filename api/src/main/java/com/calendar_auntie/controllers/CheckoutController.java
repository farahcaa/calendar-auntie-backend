package com.calendar_auntie.controllers;

import com.calendar_auntie.model.dtos.CheckoutPricingDTO;
import com.calendar_auntie.services.CheckoutService;
import com.calendar_auntie.services.ConfigService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/checkout")
public class CheckoutController {

  private final ConfigService configService;
  private final CheckoutService checkoutService;

  public CheckoutController(CheckoutService checkoutService, ConfigService configService) {
    this.checkoutService = checkoutService;
    this.configService = configService;
  }

  @GetMapping("/pricing")
  public ResponseEntity<Object> getPricing()
  {
    return ResponseEntity.ok(configService.getCheckoutPricing());
  }

  @PostMapping("/create")
  public ResponseEntity<Object> createPost(){
    //checkoutService.createOrder()
    return ResponseEntity.ok("success");
  }
}
