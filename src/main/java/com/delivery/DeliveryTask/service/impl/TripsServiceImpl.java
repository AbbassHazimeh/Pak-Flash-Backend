package com.delivery.DeliveryTask.service.impl;

import com.delivery.DeliveryTask.enums.DeliveryManStatus;
import com.delivery.DeliveryTask.enums.DeliveryTripStatus;
import com.delivery.DeliveryTask.enums.PackageStatus;
import com.delivery.DeliveryTask.exception.InvalidRequestException;
import com.delivery.DeliveryTask.exception.TripNotFoundException;
import com.delivery.DeliveryTask.exception.UserNotFoundException;
import com.delivery.DeliveryTask.model.*;
import com.delivery.DeliveryTask.repo.DeliveryTripRepository;
import com.delivery.DeliveryTask.service.PackagesService;
import com.delivery.DeliveryTask.service.TripsService;
import com.delivery.DeliveryTask.service.UsersService;
import com.delivery.DeliveryTask.service.dto.DeliveryTripDTO;
import com.delivery.DeliveryTask.service.dto.PackageOrderDTO;
import com.delivery.DeliveryTask.service.dto.UserClassDTO;
import com.delivery.DeliveryTask.service.mapper.DeliveryTripMapper;
import com.delivery.DeliveryTask.service.mapper.PackageOrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TripsServiceImpl implements TripsService {
    private final DeliveryTripRepository deliveryTripRepository;
    private final PackagesService packagesService;
    private final UsersService usersService;
    private final DeliveryTripMapper deliveryTripMapper;
    private final PackageOrderMapper packageOrderMapper;

    public TripsServiceImpl(DeliveryTripRepository deliveryTripRepository, PackagesService packagesService, UsersServiceImpl usersService, DeliveryTripMapper deliveryTripMapper, PackageOrderMapper packageOrderMapper) {
        this.deliveryTripRepository = deliveryTripRepository;
        this.packagesService = packagesService;
        this.usersService = usersService;
        this.deliveryTripMapper = deliveryTripMapper;
        this.packageOrderMapper = packageOrderMapper;
    }

    //ADMIN
    @Override
    @Transactional
    public DeliveryTripDTO createDeliveryTrip(DeliveryTripDTO deliveryTrip) {
        if (deliveryTrip == null) {
            throw new InvalidRequestException("DeliveryTrip data must not be null.");
        }
        List<String> packagesIds = deliveryTrip.getPackageIds();
        if (packagesIds == null || packagesIds.isEmpty()) {
            throw new InvalidRequestException("At least one package must be assigned to the trip.");
        }
        List<PackageOrder> packages = new ArrayList<>();
        for(String packageId: packagesIds){
            PackageOrderDTO packageOrder= packagesService.findPackageById(packageId);
            if (packageOrder != null){
                packages.add(packageOrderMapper.toEntity(packageOrder));
            }
        }
        String deliveryManId = deliveryTrip.getDeliveryManId();
        if (deliveryManId == null) {
            throw new InvalidRequestException("DeliveryMan ID must not be null.");
        }

        UserClassDTO deliveryManUser = usersService.getUserById(deliveryManId)
                .orElseThrow(() -> new UserNotFoundException("DeliveryMan ID does not exist."));

        DeliveryMan deliveryMan = deliveryManUser.getDeliveryMan();
        if(deliveryMan == null){
            throw new InvalidRequestException("DeliveryMan details are missing.");
        }
        if (deliveryMan.getStatus() != DeliveryManStatus.AVAILABLE) {
            throw new InvalidRequestException("Delivery man is not available.");
        }
        deliveryTrip.setStatus(DeliveryTripStatus.CREATED);

        DeliveryTrip savedTrip = deliveryTripRepository.save(deliveryTripMapper.toEntity(deliveryTrip));
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

            PackageOrderDTO existingPackage = packagesService.findPackageById(packageId);

            UserClassDTO customerUser = usersService.getUserById(customerId)
                    .orElseThrow(() -> new UserNotFoundException("Customer ID does not exist."));

            Customer customer = customerUser.getCustomer();
            if (customer == null) {
                throw new InvalidRequestException("Customer details are missing.");
            }
            existingPackage.setPhone(customer.getPhone());
            existingPackage.setStatus(PackageStatus.ASSIGNED);
            existingPackage.setDeliveryTripId(generatedTripId);

            existingPackage.setTime(LocalDateTime.now());

            packagesService.savePackageFromTrip(packageOrderMapper.toEntity(existingPackage));

            updatedPackages.add(packageOrderMapper.toEntity(existingPackage));
        }
        savedTrip.setPackages(updatedPackages);
        deliveryMan.setStatus(DeliveryManStatus.ON_TRIP);
        deliveryManUser.setDeliveryMan(deliveryMan);
        usersService.updateUser(deliveryManUser);
        deliveryTripRepository.save(savedTrip);
        return deliveryTripMapper.toDto(savedTrip);
    }


    //DELIVERYMAN
    @Override
    public List<DeliveryTripDTO> getAllAssignedTrips(String id){
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("DeliveryMan ID must be provided.");
        }
        usersService.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("DeliveryMan ID does not exist"));

        List<DeliveryTrip> deliveryTrip = deliveryTripRepository.findByStatusAndDeliveryManId(DeliveryTripStatus.ASSIGNED,id);
        return deliveryTrip.stream().map(deliveryTripMapper::toDto).toList();
    }


    //DELIVERYMAN
    @Override
    public DeliveryTripDTO acceptTrip(String tripId) {
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
        return deliveryTripMapper.toDto(deliveryTripRepository.save(trip));
    }

    //DELIVERYMAN
    @Override
    public DeliveryTripDTO startTrip(String tripId) {
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
        return deliveryTripMapper.toDto(deliveryTripRepository.save(trip));

    }

    //DELIVERYMAN
    @Override
    public DeliveryTripDTO markTripAsEnded(String tripId) {
        if (tripId == null || tripId.isBlank()) {
            throw new InvalidRequestException("Trip ID must be provided.");
        }
        DeliveryTrip trip = deliveryTripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with ID:" + tripId));

        if (trip.getStatus() != DeliveryTripStatus.OUT_FOR_DELIVERY) {
            throw new InvalidRequestException("Only trips 'OUT_FOR_DELIVERY' can be marked as completed.");
        }

        trip.setStatus(DeliveryTripStatus.COMPLETED);

        String deliveryManId = trip.getDeliveryManId();

        if (deliveryManId == null || deliveryManId.isBlank()) {
            throw new InvalidRequestException("DeliveryMan ID is missing from the trip.");
        }

        UserClassDTO deliveryManUser = usersService.getUserById(deliveryManId)
                .orElseThrow(() -> new  UserNotFoundException("Delivery man not found with ID: " + deliveryManId));

        DeliveryMan deliveryMan = deliveryManUser.getDeliveryMan();

        if (deliveryMan == null) {
            throw new InvalidRequestException("DeliveryMan details are missing.");
        }
        List<DeliveryTrip> trips = deliveryTripRepository.findByDeliveryManId(deliveryManId);
        for(DeliveryTrip deliveryTrip : trips){
            if(deliveryTrip.getStatus() != DeliveryTripStatus.COMPLETED){
                return deliveryTripMapper.toDto(deliveryTripRepository.save(trip));
            }
        }
        deliveryMan.setStatus(DeliveryManStatus.AVAILABLE);
        deliveryManUser.setDeliveryMan(deliveryMan);
        usersService.updateUser(deliveryManUser);

        return deliveryTripMapper.toDto(deliveryTripRepository.save(trip));
    }
}
