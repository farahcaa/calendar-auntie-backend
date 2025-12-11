package org.jaga.model.entities;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.jaga.model.enums.ShipmentStatus;

@Entity
@Table(name = "shipments")
public class Shipment {

  @Id
  @GeneratedValue(generator = "uuid")

  @Column(name = "id", nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @Column(name = "tracking_number", length = 255)
  private String trackingNumber;

  @Column(name = "carrier", length = 100)
  private String carrier;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 50)
  private ShipmentStatus status; // created, in_transit, delivered, returned

  @Column(name = "shipped_at")
  private Instant shippedAt;

  @Column(name = "delivered_at")
  private Instant deliveredAt;

  // Use DB default for created_at; keep Hibernate from writing/updating it
  @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
  private Instant createdAt;

  // getters/setters
  public UUID getId() { return id; }
  public Shipment setId(UUID id) { this.id = id; return this; }

  public Order getOrder() { return order; }
  public Shipment setOrder(Order order) { this.order = order; return this; }

  public String getTrackingNumber() { return trackingNumber; }
  public Shipment setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; return this; }

  public String getCarrier() { return carrier; }
  public Shipment setCarrier(String carrier) { this.carrier = carrier; return this; }

  public ShipmentStatus getStatus() { return status; }
  public Shipment setStatus(ShipmentStatus status) { this.status = status; return this; }

  public Instant getShippedAt() { return shippedAt; }
  public Shipment setShippedAt(Instant shippedAt) { this.shippedAt = shippedAt; return this; }

  public Instant getDeliveredAt() { return deliveredAt; }
  public Shipment setDeliveredAt(Instant deliveredAt) { this.deliveredAt = deliveredAt; return this; }

  public Instant getCreatedAt() { return createdAt; }
}
