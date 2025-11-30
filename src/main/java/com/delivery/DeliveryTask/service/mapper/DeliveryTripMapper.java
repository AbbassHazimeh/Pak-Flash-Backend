package com.delivery.DeliveryTask.service.mapper;

import com.delivery.DeliveryTask.model.DeliveryTrip;
import com.delivery.DeliveryTask.model.PackageOrder;
import com.delivery.DeliveryTask.service.dto.DeliveryTripDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeliveryTripMapper {

    @Mapping(source = "packages", target = "packageIds")
    DeliveryTripDTO toDto(DeliveryTrip trip);

    DeliveryTrip toEntity(DeliveryTripDTO dto);

    default List<String> map(List<PackageOrder> packages) {
        if (packages == null) return null;
        return packages.stream().map(PackageOrder::getId).collect(Collectors.toList());
    }
}