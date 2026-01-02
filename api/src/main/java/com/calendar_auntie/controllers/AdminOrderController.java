package com.calendar_auntie.controllers;

import com.calendar_auntie.model.enums.OrderStatus;
import com.calendar_auntie.model.repositories.OrderRepository;
import com.calendar_auntie.services.AdminOrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {


  private final AdminOrderService adminOrderService;

  public AdminOrderController(AdminOrderService adminOrderService) {
    this.adminOrderService = adminOrderService;
  }

  @GetMapping()
  public ResponseEntity<Object> getAllProducts(Pageable pageable) {

    return ResponseEntity.ok(adminOrderService.getAdminOrders(pageable));

  }

  @GetMapping("/paid")
  public ResponseEntity<Object> getAllPendingOrders(Pageable pageable) {
  return ResponseEntity.ok(adminOrderService.getAdminOrdersByStatus(pageable, OrderStatus.PAID));
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<Object> getOrderById(@PathVariable String orderId) {
    return ResponseEntity.ok(adminOrderService.getAdminOrderById(orderId));
  }

  @PostMapping("/{orderId}/fulfill")
  public ResponseEntity<Object> getOrderFulfil(@PathVariable String orderId) {
    adminOrderService.fulfilOrder(orderId);
    return ResponseEntity.ok().build();
  }
}
