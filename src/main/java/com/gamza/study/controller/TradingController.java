package com.gamza.study.controller;

import com.gamza.study.dto.RequestDTO.OrderRequestDTO;
import com.gamza.study.dto.ResponseDTO.OrderResponseDTO;
import com.gamza.study.entity.TradeOrderEntity;
import com.gamza.study.mapper.OrderMapper;
import com.gamza.study.service.TradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TradingController {
    private final TradingService tradingService;
    private final OrderMapper orderMapper;


    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable Long userId, @RequestBody OrderRequestDTO requestDto) {
        try {
            TradeOrderEntity created = tradingService.createOrder(userId, requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.OrderToDTO(created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/users/{userId}/assets")
    public ResponseEntity<?> getMyAssets(@PathVariable Long userId) {
        return tradingService.getMyAssets(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(@PathVariable Long userId) {
        List<TradeOrderEntity> orders = tradingService.getMyOrders(userId);
        return ResponseEntity.ok(orderMapper.OrderToDTOList(orders));
    }


    @DeleteMapping("/users/{userId}/orders/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long userId, @PathVariable Long orderId) {
        try {
            tradingService.cancelOrder(orderId, userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
