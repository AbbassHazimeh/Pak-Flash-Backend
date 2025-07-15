package com.delivery.DeliveryTask.service;

import com.delivery.DeliveryTask.enums.DeliveryManStatus;
import com.delivery.DeliveryTask.enums.DeliveryTripStatus;
import com.delivery.DeliveryTask.enums.PackageStatus;
import com.delivery.DeliveryTask.model.*;
import com.delivery.DeliveryTask.repo.DeliveryTripRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripsService {
    private final DeliveryTripRepository deliveryTripRepository;
    private final PackagesService packagesService;
    private final UsersService usersService;

    //ADMIN
    @Transactional
    public DeliveryTrip createDeliveryTrip(DeliveryTrip deliveryTrip) {
        deliveryTrip.setStatus(DeliveryTripStatus.CREATED);

        List<PackageOrder> packages = deliveryTrip.getPackages();
        if (packages == null || packages.isEmpty()) {
            throw new IllegalArgumentException("At least one package must be assigned to the trip.");
        }

        String deliveryManId = deliveryTrip.getDeliveryManId();
        if (deliveryManId == null) {
            throw new IllegalArgumentException("DeliveryMan ID must not be null.");
        }

        UserClass deliveryManUser = usersService.getUserById(deliveryManId)
                .orElseThrow(() -> new IllegalArgumentException("DeliveryMan ID does not exist."));

        DeliveryMan deliveryMan = deliveryManUser.getDeliveryMan();
        if (deliveryMan.getStatus() != DeliveryManStatus.AVAILABLE) {
            throw new RuntimeException("Delivery man is not available.");
        }

        DeliveryTrip savedTrip = deliveryTripRepository.save(deliveryTrip);
        String generatedTripId = savedTrip.getId();
        deliveryMan.setDeliveryTripId(generatedTripId);
        List<PackageOrder> updatedPackages = new ArrayList<>();

        for (PackageOrder order : packages) {
            String customerId = order.getCustomerId();
            String packageId = order.getId();

            if (customerId == null || packageId == null) {
                throw new IllegalArgumentException("Both Customer ID and Package ID are required.");
            }

            PackageOrder existingPackage = packagesService.findPackageById(packageId);

            UserClass customerUser = usersService.getUserById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Customer ID does not exist."));

            Customer customer = customerUser.getCustomer();
            existingPackage.setPhone(customer.getPhone());
            existingPackage.setStatus(PackageStatus.ASSIGNED);
            existingPackage.setDeliveryTripId(generatedTripId);

            packagesService.savePackage(existingPackage);
            updatedPackages.add(existingPackage);
        }
        savedTrip.setPackages(updatedPackages);
        deliveryMan.setStatus(DeliveryManStatus.ON_TRIP);
        deliveryManUser.setDeliveryMan(deliveryMan);
        usersService.updateUser(deliveryManUser);

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
                throw new RuntimeException("Trip is not created");//logically he don't have the id of the trip so he can not accept it
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
                String deliveryManId = trip.getDeliveryManId();
                UserClass deliveryManUser = usersService.getUserById(deliveryManId).orElseThrow(()->
                        new RuntimeException("DeliveryMan is not founded"));
                DeliveryMan deliveryMan = deliveryManUser.getDeliveryMan();
                deliveryMan.setStatus(DeliveryManStatus.AVAILABLE);
                deliveryManUser.setDeliveryMan(deliveryMan);
                usersService.updateUser(deliveryManUser);
                return deliveryTripRepository.save(trip);
            }else {
                throw new RuntimeException("Trip is not in delivery");
            }
        }
        return null;
    }
}
