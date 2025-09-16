package com.gamza.study.repository;

import com.gamza.study.entity.TradeOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeOrderRepository extends JpaRepository<TradeOrderEntity, Long> {
    List<TradeOrderEntity> findByUserId(Long userId);
}
