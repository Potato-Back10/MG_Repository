package com.gamza.study.dto.responseDto;

import java.math.BigDecimal;

public record AssetResponseDto(
        Long userId,
        BigDecimal balance,
        BigDecimal coinBalance
) {}
