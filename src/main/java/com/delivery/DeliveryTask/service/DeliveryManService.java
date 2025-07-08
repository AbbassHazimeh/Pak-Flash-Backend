package com.delivery.DeliveryTask.service;

import com.delivery.DeliveryTask.model.DeliveryMan;
import com.delivery.DeliveryTask.repo.DeliveryManRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryManService {
    @Autowired
    private DeliveryManRepository deliveryManRepository;

    public DeliveryMan createDeliveryMan(DeliveryMan deliveryMan) {
        return deliveryManRepository.save(deliveryMan);
    }
}
