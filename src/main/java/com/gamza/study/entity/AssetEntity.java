package com.gamza.study.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "asset")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssetEntity {
    @Id
    private Long userId; // 사용자를 식별하는 ID
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance; // 보유 현금 잔액
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal coinBalance; // 보유 코인 수량

    private AssetEntity(Long userId, BigDecimal balance, BigDecimal coinBalance) {
        this.userId = userId;
        this.balance = balance;
        this.coinBalance = coinBalance;
    }
    public static AssetEntity of(Long userId, BigDecimal balance, BigDecimal coinBalance) {
        return new AssetEntity(userId, balance, coinBalance);
    }

    public void chargeBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void deductBalance(BigDecimal amount) {
        if (this.balance.compareTo(amount) < 0) throw new IllegalArgumentException("Insufficient balance");
        this.balance = this.balance.subtract(amount);
    }

    public void chargeCoin(BigDecimal amount) {
        this.coinBalance = this.coinBalance.add(amount);
    }

    public void deductCoin(BigDecimal amount) {
        if (this.coinBalance.compareTo(amount) < 0) throw new IllegalArgumentException("Insufficient coin balance");
        this.coinBalance = this.coinBalance.subtract(amount);
    }
}
