package com.gamza.study.entity;

import com.gamza.study.entity.enums.OrderStatus;
import com.gamza.study.entity.enums.OrderType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trade_order")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TradeOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType orderType;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @CreationTimestamp
    private LocalDateTime orderTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private TradeOrderEntity(Long userId, OrderType orderType, BigDecimal amount, BigDecimal price, OrderStatus status) {
        this.userId = userId;
        this.orderType = orderType;
        this.amount  = amount;
        this.price = price;
        this.status = status;
    }
    public static TradeOrderEntity createPending(Long userId, OrderType orderType, BigDecimal amount, BigDecimal price) {
        return new TradeOrderEntity(userId, orderType, amount, price, OrderStatus.PENDING);
    }

    public void markCanceled() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot cancel a filled or already canceled order");
        }
        this.status = OrderStatus.CANCELED;
    }
}
