package com.gamza.study.controller;

import com.gamza.study.dto.responseDto.*;
import com.gamza.study.dto.requestDto.OrderRequestDto;
import com.gamza.study.entity.TradeOrderEntity;
import com.gamza.study.jwt.CustomUserDetails;
import com.gamza.study.mapper.AssetMapper;
import com.gamza.study.mapper.OrderMapper;
import com.gamza.study.service.TradingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class TradingController {
    private final TradingService tradingService;
    private final OrderMapper orderMapper;
    private final AssetMapper assetMapper;

    @GetMapping("/assets")
    public ResponseEntity<AssetResponseDto> getMyAssets(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getId();

        return tradingService.getMyAssets(userId)
                .map(assetMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> getMyOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getId();

        List<TradeOrderEntity> orders = tradingService.getMyOrders(userId);
        return ResponseEntity.ok(orderMapper.OrderToDtoList(orders));
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponseDto> createOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody OrderRequestDto requestDto) {

        Long userId = userDetails.getId();

        TradeOrderEntity created = tradingService.createOrder(userId, requestDto);
        return ResponseEntity.ok(orderMapper.OrderToDto(created));
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> cancelOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long orderId) {

        Long userId = userDetails.getId();

        tradingService.cancelOrder(orderId, userId);
        return ResponseEntity.noContent().build();
    }
}
