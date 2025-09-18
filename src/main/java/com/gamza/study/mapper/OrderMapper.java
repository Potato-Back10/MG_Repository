package com.gamza.study.mapper;

import com.gamza.study.dto.responseDto.OrderResponseDto;
import com.gamza.study.entity.TradeOrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderResponseDto OrderToDto(TradeOrderEntity tradeOrderEntity);
    List<OrderResponseDto> OrderToDtoList(List<TradeOrderEntity> entityList);
}
