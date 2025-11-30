package com.delivery.DeliveryTask.controller;

import com.delivery.DeliveryTask.model.DeliveryTrip;
import com.delivery.DeliveryTask.service.dto.DeliveryTripDTO;
import com.delivery.DeliveryTask.service.impl.TripsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trips")
public class TripsController {
    private final TripsServiceImpl tripsServiceImpl;

    //ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeliveryTripDTO> createDeliveryTrip(@RequestBody DeliveryTripDTO deliveryTrip){
        DeliveryTripDTO createdDeliveryTrip = tripsServiceImpl.createDeliveryTrip(deliveryTrip);
        return new ResponseEntity<>(createdDeliveryTrip, HttpStatus.CREATED);
    }

    //DELIVERYMAN
    @GetMapping("/{id}/assigned")
    @PreAuthorize("hasRole('DELIVERYMAN','ADMIN)")
    public ResponseEntity<List<DeliveryTripDTO>> viewAllAssignedTrips(@PathVariable String id){
        return new ResponseEntity<>(tripsServiceImpl.getAllAssignedTrips(id),HttpStatus.OK);
    }

    //DELIVERYMAN
    @PutMapping("{tripId}/accept")
    @PreAuthorize("hasRole('DELIVERYMAN')")
    public ResponseEntity<DeliveryTripDTO> acceptTrip(@PathVariable String tripId){
        DeliveryTripDTO acceptedTrip = tripsServiceImpl.acceptTrip(tripId);
        if (acceptedTrip == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(acceptedTrip);
    }

    //DELIVERYMAN
    @PutMapping("{tripId}/start")
    @PreAuthorize("hasRole('DELIVERYMAN')")
    public ResponseEntity<DeliveryTripDTO> startTrip(@PathVariable String tripId){
        DeliveryTripDTO startedTrip = tripsServiceImpl.startTrip(tripId);
        if (startedTrip == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(startedTrip);
    }

    //DELIVERYMAN
    @PutMapping("{tripId}/end")
    @PreAuthorize("hasRole('DELIVERYMAN')")
    public ResponseEntity<DeliveryTripDTO> endTrip(@PathVariable String tripId){
        DeliveryTripDTO endedTrip = tripsServiceImpl.markTripAsEnded(tripId);
        if (endedTrip == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(endedTrip);
    }

}
