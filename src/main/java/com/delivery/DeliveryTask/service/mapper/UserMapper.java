package com.delivery.DeliveryTask.service.mapper;

import com.delivery.DeliveryTask.model.UserClass;
import com.delivery.DeliveryTask.service.dto.UserClassDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserClass toEntity(UserClassDTO userClassDTO);


    UserClassDTO toDto(UserClass userClass);
}
