package com.calendar_auntie.services;

import com.calendar_auntie.model.dtos.PaymentIntentResult;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  // Optional: keep apiKey if you want to use it elsewhere
  private final String stripeApiKey;

  public PaymentService(@Value("${stripe.secret-key}") String stripeApiKey) {
    this.stripeApiKey = stripeApiKey;
    Stripe.apiKey = stripeApiKey; // initialize Stripe globally
  }

  /**
   * Create a PaymentIntent for the given amount and currency.
   *
   * @param amountInCents total amount in the smallest currency unit (e.g. cents for USD)
   * @param currency      ISO currency code, e.g. "usd"
   * @return PaymentIntentResult containing both the PaymentIntent ID (for DB)
   *         and the client secret (for frontend)
   */
  public PaymentIntentResult createPaymentIntent(long amountInCents, String currency) {
    try {
      PaymentIntentCreateParams params =
        PaymentIntentCreateParams.builder()
          .setAmount(amountInCents)
          .setCurrency(currency)
          .setAutomaticPaymentMethods(
            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
              .setEnabled(true)
              .build()
          )
          .build();

      PaymentIntent paymentIntent = PaymentIntent.create(params);

      return new PaymentIntentResult(
        paymentIntent.getId(),          // e.g. "pi_3Xyz..."
        paymentIntent.getClientSecret() // e.g. "pi_3Xyz..._secret_Abc..."
        ,
        UUID.randomUUID() //placeholder
      );

    } catch (StripeException e) {
      // you can wrap this in a custom runtime exception or handle however you want
      throw new RuntimeException("Failed to create Stripe PaymentIntent", e);
    }
  }
}
