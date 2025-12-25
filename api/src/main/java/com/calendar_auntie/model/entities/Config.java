package com.calendar_auntie.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;
import org.hibernate.annotations.UpdateTimestamp;

@Table(name = "config")
@Entity
public class Config {

  @Id
  private String config_key;

  @Column(name = "tax")
  private double tax;

  @Column(name = "shipping")
  private double shipping;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private Instant updatedAt;

  public String getConfig_key() {
    return config_key;
  }

  public Config setConfig_key(String config_key) {
    this.config_key = config_key;
    return this;
  }

  public double getTax() {
    return tax;
  }

  public Config setTax(double tax) {
    this.tax = tax;
    return this;
  }

  public double getShipping() {
    return shipping;
  }

  public Config setShipping(double shipping) {
    this.shipping = shipping;
    return this;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

}
