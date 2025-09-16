package com.gamza.study.dto.ResponseDTO;

import java.math.BigDecimal;

public record AssetResponseDTO(
        Long userId,
        BigDecimal balance,
        BigDecimal coinBalance
) {
}
