package org.jaga.model.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.jaga.model.enums.TransactionStatus;

@Entity
@Table(name = "transactions")
public class Transaction {

  @Id
  @GeneratedValue(generator = "uuid2")
  @Column(name = "id", nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @Column(name = "payment_provider", length = 50)
  private String paymentProvider; // e.g., "stripe", "paypal"

  @Column(name = "provider_transaction_id", length = 255)
  private String providerTransactionId;

  @Column(name = "amount", nullable = false, precision = 10, scale = 2)
  private BigDecimal amount;

  @Column(name = "currency", length = 10)
  private String currency = "USD";

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 50)
  private TransactionStatus status; // success, failed, pending, refunded

  // Use DB default
  @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
  private Instant createdAt;

  // getters/setters
  public UUID getId() { return id; }
  public Transaction setId(UUID id) { this.id = id; return this; }

  public Order getOrder() { return order; }
  public Transaction setOrder(Order order) { this.order = order; return this; }

  public String getPaymentProvider() { return paymentProvider; }
  public Transaction setPaymentProvider(String paymentProvider) { this.paymentProvider = paymentProvider; return this; }

  public String getProviderTransactionId() { return providerTransactionId; }
  public Transaction setProviderTransactionId(String providerTransactionId) { this.providerTransactionId = providerTransactionId; return this; }

  public BigDecimal getAmount() { return amount; }
  public Transaction setAmount(BigDecimal amount) { this.amount = amount; return this; }

  public String getCurrency() { return currency; }
  public Transaction setCurrency(String currency) { this.currency = currency; return this; }

  public TransactionStatus getStatus() { return status; }
  public Transaction setStatus(TransactionStatus status) { this.status = status; return this; }

  public Instant getCreatedAt() { return createdAt; }
}
