package com.gamza.study.mapper;

import com.gamza.study.dto.ResponseDTO.AssetResponseDTO;
import com.gamza.study.entity.AssetEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssetMapper {
    AssetResponseDTO toDTO(AssetEntity assetEntity);
}
