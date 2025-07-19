package com.delivery.DeliveryTask.service;

import com.delivery.DeliveryTask.enums.DeliveryManStatus;
import com.delivery.DeliveryTask.enums.DeliveryTripStatus;
import com.delivery.DeliveryTask.enums.PackageStatus;
import com.delivery.DeliveryTask.exception.InvalidRequestException;
import com.delivery.DeliveryTask.exception.TripNotFoundException;
import com.delivery.DeliveryTask.exception.UserNotFoundException;
import com.delivery.DeliveryTask.model.*;
import com.delivery.DeliveryTask.repo.DeliveryTripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TripsService {
    private final DeliveryTripRepository deliveryTripRepository;
    private final PackagesService packagesService;
    private final UsersService usersService;

    //ADMIN
    @Transactional
    public DeliveryTrip createDeliveryTrip(DeliveryTrip deliveryTrip) {
        if (deliveryTrip == null) {
            throw new InvalidRequestException("DeliveryTrip data must not be null.");
        }


        List<PackageOrder> packages = deliveryTrip.getPackages();
        if (packages == null || packages.isEmpty()) {
            throw new InvalidRequestException("At least one package must be assigned to the trip.");
        }

        String deliveryManId = deliveryTrip.getDeliveryManId();
        if (deliveryManId == null) {
            throw new InvalidRequestException("DeliveryMan ID must not be null.");
        }

        UserClass deliveryManUser = usersService.getUserById(deliveryManId)
                .orElseThrow(() -> new UserNotFoundException("DeliveryMan ID does not exist."));

        DeliveryMan deliveryMan = deliveryManUser.getDeliveryMan();
        if(deliveryMan == null){
            throw new InvalidRequestException("DeliveryMan details are missing.");
        }
        if (deliveryMan.getStatus() != DeliveryManStatus.AVAILABLE) {
            throw new InvalidRequestException("Delivery man is not available.");
        }
        deliveryTrip.setStatus(DeliveryTripStatus.CREATED);

        DeliveryTrip savedTrip = deliveryTripRepository.save(deliveryTrip);
        String generatedTripId = savedTrip.getId();
        deliveryMan.setDeliveryTripId(generatedTripId);

        List<PackageOrder> updatedPackages = new ArrayList<>();

        for (PackageOrder order : packages) {
            if (order == null) {
                throw new InvalidRequestException("Package in the trip cannot be null.");
            }
            String customerId = order.getCustomerId();
            String packageId = order.getId();

            if (customerId == null || packageId == null) {
                throw new InvalidRequestException("Both Customer ID and Package ID are required.");
            }

            PackageOrder existingPackage = packagesService.findPackageById(packageId);

            UserClass customerUser = usersService.getUserById(customerId)
                    .orElseThrow(() -> new UserNotFoundException("Customer ID does not exist."));

            Customer customer = customerUser.getCustomer();
            if (customer == null) {
                throw new InvalidRequestException("Customer details are missing.");
            }
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
    public List<DeliveryTrip> getAllAssignedTrips(String id){
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("DeliveryMan ID must be provided.");
        }
        return deliveryTripRepository.findByStatusAndDeliveryManId(DeliveryTripStatus.ASSIGNED,id);
    }


    //DELIVERYMAN
    public DeliveryTrip acceptTrip(String tripId) {
        if (tripId == null || tripId.isBlank()) {
            throw new InvalidRequestException("Trip ID must not be null or empty.");
        }

        Optional<DeliveryTrip> optionalTrip = deliveryTripRepository.findById(tripId);
        if (optionalTrip.isEmpty()) {
            throw new TripNotFoundException("Trip not found with ID: " + tripId);
        }
        DeliveryTrip trip = optionalTrip.get();
        if (trip.getStatus() != DeliveryTripStatus.CREATED) {
            throw new InvalidRequestException("Only trips with status 'CREATED' can be accepted.");
        }
                trip.setStatus(DeliveryTripStatus.ASSIGNED);
                return deliveryTripRepository.save(trip);
    }

    //DELIVERYMAN
    public DeliveryTrip startTrip(String tripId) {
        if (tripId == null || tripId.isBlank()) {
            throw new InvalidRequestException("Trip ID must be provided.");
        }
        Optional<DeliveryTrip> optionalTrip = deliveryTripRepository.findById(tripId);
        if (optionalTrip.isEmpty()) {
            throw new TripNotFoundException("Trip not found with ID: " + tripId);
        }
        DeliveryTrip trip = optionalTrip.get();
        if (trip.getStatus() != DeliveryTripStatus.ASSIGNED) {
            throw new InvalidRequestException("Only trips with status 'ASSIGNED' can be started.");
        }
        trip.setStatus(DeliveryTripStatus.OUT_FOR_DELIVERY);
        return deliveryTripRepository.save(trip);

    }

    //DELIVERYMAN
    public DeliveryTrip markTripAsEnded(String tripId) {
        if (tripId == null || tripId.isBlank()) {
            throw new InvalidRequestException("Trip ID must be provided.");
        }
        Optional<DeliveryTrip> optionalTrip = deliveryTripRepository.findById(tripId);
        if (optionalTrip.isEmpty()) {
            throw new TripNotFoundException("Trip not found with ID: " + tripId);
        }
        DeliveryTrip trip = optionalTrip.get();
        if (trip.getStatus() != DeliveryTripStatus.OUT_FOR_DELIVERY) {
            throw new InvalidRequestException("Only trips 'OUT_FOR_DELIVERY' can be marked as completed.");
        }
                trip.setStatus(DeliveryTripStatus.COMPLETED);
                String deliveryManId = trip.getDeliveryManId();
        if (deliveryManId == null || deliveryManId.isBlank()) {
            throw new InvalidRequestException("DeliveryMan ID is missing from the trip.");
        }
        Optional<UserClass> optionalUser = usersService.getUserById(deliveryManId);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Delivery man not found with ID: " + deliveryManId);
        }

        UserClass deliveryManUser = optionalUser.get();
        DeliveryMan deliveryMan = deliveryManUser.getDeliveryMan();
        if (deliveryMan == null) {
            throw new InvalidRequestException("DeliveryMan details are missing.");
        }
        deliveryMan.setStatus(DeliveryManStatus.AVAILABLE);
        deliveryManUser.setDeliveryMan(deliveryMan);
        usersService.updateUser(deliveryManUser);

        return deliveryTripRepository.save(trip);
    }
}
