package com.calendar_auntie.model.repositories;

import java.util.UUID;
import com.calendar_auntie.model.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
  Customer findByEmail(String email);
}
