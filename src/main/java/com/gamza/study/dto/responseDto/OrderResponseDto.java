package com.gamza.study.dto.responseDto;

import com.gamza.study.entity.enums.OrderStatus;
import com.gamza.study.entity.enums.OrderType;

import java.math.BigDecimal;

public record OrderResponseDto(
        Long orderId,
        BigDecimal amount,
        BigDecimal price,
        OrderType orderType,
        OrderStatus orderStatus
) {}
