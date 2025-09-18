package com.gamza.study.service;

import com.gamza.study.dto.requestDto.OrderRequestDto;
import com.gamza.study.entity.AssetEntity;
import com.gamza.study.entity.TradeOrderEntity;

import java.util.List;
import java.util.Optional;

public interface TradingService {
    Optional<AssetEntity> getMyAssets(Long userId);
    List<TradeOrderEntity> getMyOrders(Long userId);
    TradeOrderEntity createOrder(Long userId, OrderRequestDto orderRequestDTO);
    void cancelOrder(Long orderId, Long userId);

}
