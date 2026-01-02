package com.calendar_auntie.controllers;

import com.calendar_auntie.model.dtos.OrderCreationResponse;
import com.calendar_auntie.model.entities.Order;
import com.calendar_auntie.model.enums.OrderStatus;
import com.calendar_auntie.model.repositories.OrderRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class StripeWebhookController {

  @Value("${stripe.webhook.secret}")
  private String endpointSecret;

  private final Logger logger =  LoggerFactory.getLogger(StripeWebhookController.class);
  private final OrderRepository orderRepository;
  private final ObjectMapper om = new ObjectMapper();
  public StripeWebhookController(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @PostMapping("/webhook")
  public ResponseEntity<String> handle(
    @RequestHeader("Stripe-Signature") String sigHeader,
    @RequestBody String payload
  ) {
    com.stripe.model.Event event;
    try {
      event = com.stripe.net.Webhook.constructEvent(payload, sigHeader, endpointSecret);
    } catch (Exception e) {
      logger.error("Invalid signature", e);
      return ResponseEntity.status(400).body("Invalid signature");
    }

    logger.info("Stripe event id={}, type={}", event.getId(), event.getType());

    try {
      JsonNode root = om.readTree(payload);
      JsonNode obj = root.path("data").path("object");

      switch (event.getType()) {
        case "payment_intent.succeeded" -> {
          String paymentIntentId = obj.path("id").asText(null);
          logger.info("PI succeeded id={}", paymentIntentId);
          markOrder(paymentIntentId, OrderStatus.PAID);
        }
        case "payment_intent.payment_failed" -> {
          String paymentIntentId = obj.path("id").asText(null);
          logger.info("PI failed id={}", paymentIntentId);
          markOrder(paymentIntentId, OrderStatus.FAILED);
        }
        default -> {
          // no-op
        }
      }
    } catch (Exception e) {
      logger.error("Failed to parse webhook JSON", e);
      return ResponseEntity.status(400).body("Bad payload");
    }

    return ResponseEntity.ok("ok");
  }

  private void markOrder(String paymentIntentId, OrderStatus status) {
    if (paymentIntentId == null || paymentIntentId.isBlank()) {
      logger.warn("No paymentIntentId found in event");
      return;
    }

    Order order = orderRepository.findByStripePaymentIntentId(paymentIntentId);
    if (order == null) {
      logger.warn("No Order found for paymentIntentId={}", paymentIntentId);
      return;
    }

    order.setStatus(status);
    orderRepository.save(order);
    logger.info("Updated order {} -> {}", order.getId(), status);
  }

}
