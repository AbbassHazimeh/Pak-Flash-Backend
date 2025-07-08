package com.delivery.DeliveryTask.service;
import com.delivery.DeliveryTask.model.Customer;
import com.delivery.DeliveryTask.model.DeliveryMan;
import com.delivery.DeliveryTask.model.DeliveryTrip;
import com.delivery.DeliveryTask.model.PackageOrder;
import com.delivery.DeliveryTask.repo.CustomerRepository;
import com.delivery.DeliveryTask.repo.DeliveryManRepository;
import com.delivery.DeliveryTask.repo.DeliveryTripRepository;
import com.delivery.DeliveryTask.repo.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final CustomerRepository customerRepository;
    private final DeliveryManRepository deliveryManRepository;
    private final DeliveryTripRepository deliveryTripRepository;
    private final PackageRepository packageRepository;
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public DeliveryMan createDeliveryMan(DeliveryMan deliveryMan) {
        return deliveryManRepository.save(deliveryMan);
    }
    public PackageOrder createPackage(PackageOrder newpackage) {
        return packageRepository.save(newpackage);
    }

    public DeliveryTrip createTrip(DeliveryTrip deliveryTrip) {
        return deliveryTripRepository.save(deliveryTrip);
    }
}
