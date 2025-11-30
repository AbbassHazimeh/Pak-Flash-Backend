package com.delivery.DeliveryTask.service.mapper;

import com.delivery.DeliveryTask.model.PackageOrder;
import com.delivery.DeliveryTask.service.dto.PackageOrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PackageOrderMapper {

    PackageOrder toEntity(PackageOrderDTO dto);
    PackageOrderDTO toDto(PackageOrder packageOrder);
}
