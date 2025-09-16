package com.gamza.study.controller;

import com.gamza.study.dto.RequestDTO.OrderRequestDTO;
import com.gamza.study.dto.ResponseDTO.AssetResponseDTO;
import com.gamza.study.dto.ResponseDTO.OrderResponseDTO;
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
    public ResponseEntity<AssetResponseDTO> getMyAssets(@PathVariable Long userId) {
        return tradingService.getMyAssets(userId)
                .map(assetMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(@PathVariable Long userId) {
        List<TradeOrderEntity> orders = tradingService.getMyOrders(userId);
        return ResponseEntity.ok(orderMapper.OrderToDTOList(orders));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<OrderResponseDTO> createOrder(@PathVariable Long userId,
                                                        @Valid @RequestBody OrderRequestDTO requestDto) {
        TradeOrderEntity created = tradingService.createOrder(userId, requestDto);
        return ResponseEntity.ok(orderMapper.OrderToDTO(created));
    }

    @DeleteMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long userId, @PathVariable Long orderId) {
        tradingService.cancelOrder(orderId, userId);
        return ResponseEntity.noContent().build();
    }
}
