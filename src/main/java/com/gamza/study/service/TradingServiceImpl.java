package com.gamza.study.service;

import com.gamza.study.dto.requestDto.OrderRequestDto;
import com.gamza.study.entity.AssetEntity;
import com.gamza.study.entity.TradeOrderEntity;
import com.gamza.study.entity.enums.OrderStatus;
import com.gamza.study.entity.enums.OrderType;
import com.gamza.study.error.ErrorCode;
import com.gamza.study.error.customExceptions.InsufficientException;
import com.gamza.study.error.customExceptions.OrderException;
import com.gamza.study.error.customExceptions.UnauthorizedException;
import com.gamza.study.error.customExceptions.UserNotFoundException;
import com.gamza.study.repository.AssetRepository;
import com.gamza.study.repository.TradeOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TradingServiceImpl implements TradingService{
    private final AssetRepository assetRepository;
    private final TradeOrderRepository tradeOrderRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetEntity> getMyAssets(Long userId) {
        return assetRepository.findById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TradeOrderEntity> getMyOrders(Long userId) {
        return tradeOrderRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public TradeOrderEntity createOrder(Long userId, OrderRequestDto dto) {
        AssetEntity asset = assetRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        if (dto.orderType() == OrderType.BUY) {
            BigDecimal required = dto.amount().multiply(dto.price());
            if (asset.getBalance().compareTo(required) < 0) throw new InsufficientException(ErrorCode.INSUFFICIENT_BALANCE);
            asset.deductBalance(required);
        } else if (dto.orderType() == OrderType.SELL) {
            if (asset.getCoinBalance().compareTo(dto.amount()) < 0) throw new InsufficientException(ErrorCode.INSUFFICIENT_COIN_BALANCE);
            asset.deductCoin(dto.amount());
        }

        TradeOrderEntity order = TradeOrderEntity.createPending(
                userId, dto.orderType(), dto.amount(), dto.price());
        assetRepository.save(asset);
        return tradeOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        TradeOrderEntity order = tradeOrderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getUserId().equals(userId))
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_ACTION);
        if (order.getStatus() != OrderStatus.PENDING)
            throw new OrderException(ErrorCode.INVALID_ORDER_STATE);

        AssetEntity asset = assetRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        if (order.getOrderType() == OrderType.BUY) {
            asset.chargeBalance(order.getAmount().multiply(order.getPrice()));
        } else if (order.getOrderType() == OrderType.SELL) {
            asset.chargeCoin(order.getAmount());
        }

        order.markCanceled();
        assetRepository.save(asset);
        tradeOrderRepository.save(order);
    }
}
