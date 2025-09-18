package com.gamza.study.mapper;

import com.gamza.study.dto.responseDto.AssetResponseDto;
import com.gamza.study.entity.AssetEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssetMapper {
    AssetResponseDto toDTO(AssetEntity assetEntity);
}
