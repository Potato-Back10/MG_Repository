package com.gamza.study.service;

import com.gamza.study.dto.RequestDTO.OrderRequestDTO;
import com.gamza.study.entity.AssetEntity;
import com.gamza.study.entity.TradeOrderEntity;
import com.gamza.study.entity.enums.OrderStatus;
import com.gamza.study.entity.enums.OrderType;
import com.gamza.study.error.ErrorCode;
import com.gamza.study.error.customExceptions.InsufficientException;
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
    @Transactional
    public TradeOrderEntity createOrder(Long userId, OrderRequestDTO orderRequestDTO) {
        AssetEntity userAsset = assetRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        if (orderRequestDTO.orderType() == OrderType.BUY) {
            BigDecimal requiredBalance = orderRequestDTO.amount().multiply(orderRequestDTO.price());
            if (userAsset.getBalance().compareTo(requiredBalance) < 0) {
                throw new InsufficientException(ErrorCode.INSUFFICIENT_BALANCE);
            }
            userAsset.deductBalance(requiredBalance);
        } else if (orderRequestDTO.orderType() == OrderType.SELL) {
            if (userAsset.getCoinBalance().compareTo(orderRequestDTO.amount()) < 0) {
                throw new InsufficientException(ErrorCode.INSUFFICIENT_COIN_BALANCE);
            }
            userAsset.deductCoin(orderRequestDTO.amount());
        }

        TradeOrderEntity tradeOrderEntity = TradeOrderEntity.createPending(
                userId,
                orderRequestDTO.orderType(),
                orderRequestDTO.amount(),
                orderRequestDTO.price()
        );

        assetRepository.save(userAsset);
        return tradeOrderRepository.save(tradeOrderEntity);
    }

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
    public void cancelOrder(Long orderId, Long userId) {
        TradeOrderEntity tradeOrderEntity = tradeOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!tradeOrderEntity.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Unauthorized action");
        }

        if (tradeOrderEntity.getStatus() == OrderStatus.PENDING) {
            tradeOrderEntity.markCanceled();

            AssetEntity userAsset = assetRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

            if (tradeOrderEntity.getOrderType() == OrderType.BUY) {
                userAsset.chargeBalance(tradeOrderEntity.getAmount().multiply(tradeOrderEntity.getPrice()));
            } else if (tradeOrderEntity.getOrderType() == OrderType.SELL) {
                userAsset.chargeCoin(tradeOrderEntity.getAmount());
            }
            assetRepository.save(userAsset);
            tradeOrderRepository.save(tradeOrderEntity);
        } else {
            throw new IllegalArgumentException("Cannot cancel a filled or already canceled order");
        }
    }
}
