package com.gamza.study.dto.requestDto;

import com.gamza.study.entity.enums.OrderType;

import java.math.BigDecimal;

public record OrderRequestDto(
        BigDecimal amount,
        BigDecimal price,
        OrderType orderType
) {}
