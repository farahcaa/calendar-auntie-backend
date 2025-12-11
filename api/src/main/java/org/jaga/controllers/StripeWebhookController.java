package org.jaga.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// StripeWebhookController.java
@RestController
@RequestMapping("/api/payments")
public class StripeWebhookController {

  @Value("${stripe.webhook.secret}")
  private String endpointSecret;

  @PostMapping("/webhook")
  public ResponseEntity<String> handle(@RequestHeader("Stripe-Signature") String sigHeader,
                                       @RequestBody String payload) {
    com.stripe.model.Event event;
    try {
      event = com.stripe.net.Webhook.constructEvent(payload, sigHeader, endpointSecret);
    } catch (Exception e) {
      return ResponseEntity.status(400).body("Invalid signature");
    }

    if ("payment_intent.succeeded".equals(event.getType())) {
      var deserializer = event.getDataObjectDeserializer();
      deserializer.getObject().ifPresent(obj -> {
        var pi = (com.stripe.model.PaymentIntent) obj;
        String paymentIntentId = pi.getId();
        String orderId = pi.getMetadata().get("orderId");
        // TODO: mark your order as PAID, store charge id(s), etc.
      });
    }

    if ("payment_intent.payment_failed".equals(event.getType())) {
      // Optionally mark order as failed, log reason
    }

    return ResponseEntity.ok("ok");
  }
}
