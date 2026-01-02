package com.calendar_auntie.controllers;

import com.calendar_auntie.model.dtos.CheckoutOrderDTO;
import com.calendar_auntie.model.dtos.CheckoutPricingDTO;
import com.calendar_auntie.model.dtos.CreateOrderRequest;
import com.calendar_auntie.model.dtos.OrderCreationResponse;
import com.calendar_auntie.model.dtos.PaymentIntentResult;
import com.calendar_auntie.model.entities.Order;
import com.calendar_auntie.services.CheckoutService;
import com.calendar_auntie.services.ConfigService;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/checkout")
@RestController
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
  public ResponseEntity<Object> createOrder(@RequestBody CreateOrderRequest createOrderRequest){
PaymentIntentResult paymentIntentResult = checkoutService.createOrder(createOrderRequest);

if(paymentIntentResult == null){
  return ResponseEntity.status(HttpStatus.CONFLICT).body("product has too few items left");
}
    return ResponseEntity.ok(new OrderCreationResponse(paymentIntentResult.orderId().toString(),
      paymentIntentResult.clientSecret()));
  }
}
