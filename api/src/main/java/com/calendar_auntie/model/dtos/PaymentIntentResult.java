package com.calendar_auntie.model.dtos;

import java.util.UUID;

public record PaymentIntentResult(
  String paymentIntentId,
  String clientSecret,
  UUID orderId
) {}