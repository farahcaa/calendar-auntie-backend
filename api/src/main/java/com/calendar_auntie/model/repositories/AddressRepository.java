package com.calendar_auntie.model.repositories;

import java.util.List;
import java.util.UUID;
import com.calendar_auntie.model.entities.Address;
import com.calendar_auntie.model.entities.Customer;
import com.calendar_auntie.model.enums.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, UUID> {
  List<Address> findByAddressTypeAndCustomer(AddressType addressType, Customer customer);


}
