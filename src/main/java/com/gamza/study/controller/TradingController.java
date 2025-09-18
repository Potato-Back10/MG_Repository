package com.gamza.study.controller;

import com.gamza.study.dto.responseDto.*;
import com.gamza.study.dto.requestDto.OrderRequestDto;
import com.gamza.study.entity.TradeOrderEntity;
import com.gamza.study.mapper.AssetMapper;
import com.gamza.study.mapper.OrderMapper;
import com.gamza.study.service.TradingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class TradingController {
    private final TradingService tradingService;
    private final OrderMapper orderMapper;
    private final AssetMapper assetMapper;

    @GetMapping("/{userId}/assets")
    public ResponseEntity<AssetResponseDto> getMyAssets(@PathVariable Long userId) {
        return tradingService.getMyAssets(userId)
                .map(assetMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderResponseDto>> getMyOrders(@PathVariable Long userId) {
        List<TradeOrderEntity> orders = tradingService.getMyOrders(userId);
        return ResponseEntity.ok(orderMapper.OrderToDtoList(orders));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<OrderResponseDto> createOrder(@PathVariable Long userId,
                                                        @Valid @RequestBody OrderRequestDto requestDto) {
        TradeOrderEntity created = tradingService.createOrder(userId, requestDto);
        return ResponseEntity.ok(orderMapper.OrderToDto(created));
    }

    @DeleteMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long userId, @PathVariable Long orderId) {
        tradingService.cancelOrder(orderId, userId);
        return ResponseEntity.noContent().build();
    }
}
