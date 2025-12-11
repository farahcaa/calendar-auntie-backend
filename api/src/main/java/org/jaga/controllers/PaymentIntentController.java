package org.jaga.controllers;

import com.stripe.model.PaymentIntent;
import jakarta.validation.Valid;
import org.jaga.model.dtos.payment.CreatePaymentRequest;
import org.jaga.model.dtos.payment.CreatePaymentResponse;
import org.jaga.services.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// PaymentIntentController.java
@RestController
@RequestMapping("/api/payments")
public class PaymentIntentController {

  private final Logger log = LoggerFactory.getLogger(PaymentIntentController.class);
  private final PaymentService paymentService;

  public PaymentIntentController(@Value("${stripe.secret.key}") String secretKey, PaymentService paymentService) {
    com.stripe.Stripe.apiKey = secretKey;
    this.paymentService = paymentService;
  }

  @PostMapping("/create-intent")
  public ResponseEntity<Object> createIntent(@Valid @RequestBody
                                                            CreatePaymentRequest request) throws Exception {
log.info("Creating payment intent");
if(!paymentService.validateCart(request.getItems())) return ResponseEntity.badRequest().build();

long price = paymentService.calculatePrice(request.getItems());

PaymentIntent paymentIntent = paymentService.createIntent(price, request.getItems().getFirst().getCurrency());

CreatePaymentResponse paymentResponse = new CreatePaymentResponse(paymentIntent.getClientSecret(), price, request.getItems().getFirst().getCurrency());

paymentService.createEntities(request, price);
return ResponseEntity.ok().body(paymentResponse);
  }
}
