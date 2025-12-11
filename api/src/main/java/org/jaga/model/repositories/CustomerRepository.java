package org.jaga.model.repositories;

import java.util.UUID;
import org.jaga.model.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
  Customer findByEmail(String email);
}
