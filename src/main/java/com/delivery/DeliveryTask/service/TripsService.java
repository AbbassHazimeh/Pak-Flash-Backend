package com.delivery.DeliveryTask.service;

import com.delivery.DeliveryTask.enums.DeliveryManStatus;
import com.delivery.DeliveryTask.enums.DeliveryTripStatus;
import com.delivery.DeliveryTask.enums.PackageStatus;
import com.delivery.DeliveryTask.model.Customer;
import com.delivery.DeliveryTask.model.DeliveryMan;
import com.delivery.DeliveryTask.model.DeliveryTrip;
import com.delivery.DeliveryTask.model.PackageOrder;
import com.delivery.DeliveryTask.repo.CustomerRepository;
import com.delivery.DeliveryTask.repo.DeliveryManRepository;
import com.delivery.DeliveryTask.repo.DeliveryTripRepository;
import com.delivery.DeliveryTask.repo.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripsService {
    private final DeliveryTripRepository deliveryTripRepository;
    private final PackageRepository packageRepository;
    private final DeliveryManRepository deliveryManRepository;
    private final CustomerRepository customerRepository;

    //ADMIN
    @Transactional
    public DeliveryTrip createDeliveryTrip(DeliveryTrip deliveryTrip) {
        deliveryTrip.setStatus(DeliveryTripStatus.CREATED);

        List<PackageOrder> packages = deliveryTrip.getPackages();
        if (packages == null || packages.isEmpty()) {
            throw new IllegalArgumentException("At least one package must be assigned to the trip.");
        }

        ObjectId deliveryManId = deliveryTrip.getDeliveryManId();
        if (deliveryManId == null) {
            throw new IllegalArgumentException("DeliveryMan ID must not be null.");
        }

        DeliveryMan deliveryMan = deliveryManRepository.findById(String.valueOf(deliveryManId))
                .orElseThrow(() -> new IllegalArgumentException("DeliveryMan ID does not exist."));

        if (deliveryMan.getStatus() != DeliveryManStatus.AVAILABLE) {
            throw new RuntimeException("Delivery man is not available.");
        }

        DeliveryTrip savedTrip = deliveryTripRepository.save(deliveryTrip);
        ObjectId generatedTripId = savedTrip.getId();
        deliveryMan.setDelivery_trip_id(savedTrip.getId());
        List<PackageOrder> updatedPackages = new ArrayList<>();

        for (PackageOrder order : packages) {
            ObjectId customerId = order.getCustomerId();
            ObjectId packageId = order.getId();

            if (customerId == null || packageId == null) {
                throw new IllegalArgumentException("Both Customer ID and Package ID are required.");
            }

            PackageOrder existingPackage = packageRepository.findById(packageId.toHexString())
                    .orElseThrow(() -> new IllegalArgumentException("Package ID does not exist."));
            Customer customer = customerRepository.findById(customerId.toHexString())
                    .orElseThrow(() -> new IllegalArgumentException("Customer ID does not exist."));

            existingPackage.setPhone(customer.getPhone());
            existingPackage.setStatus(PackageStatus.ASSIGNED);
            existingPackage.setDelivery_trip_id(generatedTripId);

            packageRepository.save(existingPackage);
            updatedPackages.add(existingPackage);
        }
        savedTrip.setPackages(updatedPackages);
        deliveryMan.setStatus(DeliveryManStatus.ON_TRIP);
        deliveryManRepository.save(deliveryMan);

        return deliveryTripRepository.save(savedTrip);
    }


    //DELIVERYMAN
    public List<DeliveryTrip> getAllAssignedTrips(ObjectId id){
        return deliveryTripRepository.findByStatusAndDeliveryManId(DeliveryTripStatus.ASSIGNED,id);
    }


    //DELIVERYMAN
    public DeliveryTrip acceptTrip(String tripId) {
        DeliveryTrip trip = deliveryTripRepository.findById(tripId).orElse(null);
        if(trip != null){
            if(trip.getStatus() == DeliveryTripStatus.CREATED){
                trip.setStatus(DeliveryTripStatus.ASSIGNED);
                return deliveryTripRepository.save(trip);
            }else {
                throw new RuntimeException("Trip is not created");//logically he dont have the id of the trip so he can not accept it
            }
        }
        return null;
    }

    //DELIVERYMAN
    public DeliveryTrip startTrip(String tripId) {
        DeliveryTrip trip = deliveryTripRepository.findById(tripId).orElse(null);
        if(trip != null){
            if(trip.getStatus() == DeliveryTripStatus.ASSIGNED){
            trip.setStatus(DeliveryTripStatus.OUT_FOR_DELIVERY);
            return deliveryTripRepository.save(trip);
            }else {
                throw new RuntimeException("Trip is not assigned");
            }
        }

        return null;
    }

    //DELIVERYMAN
    public DeliveryTrip markTripAsEnded(String tripId) {
        DeliveryTrip trip = deliveryTripRepository.findById(tripId).orElse(null);
        if(trip != null){
            if(trip.getStatus() == DeliveryTripStatus.OUT_FOR_DELIVERY){
                trip.setStatus(DeliveryTripStatus.COMPLETED);
                ObjectId deliveryManId = trip.getDeliveryManId();
                DeliveryMan deliveryMan = deliveryManRepository.findById(deliveryManId.toHexString()).orElseThrow(()->
                        new RuntimeException("DeliveryMan is not founded"));
                deliveryMan.setStatus(DeliveryManStatus.AVAILABLE);
                deliveryManRepository.save(deliveryMan);
                return deliveryTripRepository.save(trip);
            }else {
                throw new RuntimeException("Trip is not in delivery");
            }
        }
        return null;
    }
}
