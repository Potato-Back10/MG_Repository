package com.gamza.study.mapper;

import com.gamza.study.dto.ResponseDTO.OrderResponseDTO;
import com.gamza.study.entity.TradeOrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderResponseDTO OrderToDTO(TradeOrderEntity tradeOrderEntity);
    List<OrderResponseDTO> OrderToDTOList(List<TradeOrderEntity> entityList);
}
