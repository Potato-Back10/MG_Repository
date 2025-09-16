package com.gamza.study.dto.RequestDTO;

import com.gamza.study.entity.enums.OrderType;

import java.math.BigDecimal;

public record OrderRequestDTO(
        BigDecimal amount,
        BigDecimal price,
        OrderType orderType
) {
}
