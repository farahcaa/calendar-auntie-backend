package com.calendar_auntie.model.dtos;

import com.calendar_auntie.model.enums.OrderStatus;
import java.sql.Timestamp;
import java.time.Instant;

public record AdminOrderDTO(String id, OrderStatus orderStatus, Instant timestamp) {
}
