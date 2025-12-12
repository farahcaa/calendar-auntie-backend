package com.calendar_auntie.model.dtos.payment;

public class CreatePaymentResponse {

  private String clientSecret;

  private long amount;

  private String currency;


  public CreatePaymentResponse(String clientSecret, long amount, String currency) {
    this.clientSecret = clientSecret;
    this.amount = amount;
    this.currency = currency;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public CreatePaymentResponse setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
    return this;
  }
  public long getAmount() {
    return amount;
  }
  public CreatePaymentResponse setAmount(long amount) {
    this.amount = amount;
    return this;
  }
  public String getCurrency() {
    return currency;
  }
  public CreatePaymentResponse setCurrency(String currency) {
    this.currency = currency;
    return this;
  }

}
