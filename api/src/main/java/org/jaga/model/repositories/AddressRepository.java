package org.jaga.model.repositories;

import java.util.List;
import java.util.UUID;
import org.jaga.model.entities.Address;
import org.jaga.model.entities.Customer;
import org.jaga.model.enums.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, UUID> {
  List<Address> findByAddressTypeAndCustomer(AddressType addressType, Customer customer);

}
