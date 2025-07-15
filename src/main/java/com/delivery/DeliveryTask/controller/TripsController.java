package com.delivery.DeliveryTask.controller;

import com.delivery.DeliveryTask.model.DeliveryTrip;
import com.delivery.DeliveryTask.service.TripsService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trips")
public class TripsController {
    private final TripsService tripsService;

    //ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeliveryTrip> createDeliveryTrip(@RequestBody DeliveryTrip deliveryTrip){
        DeliveryTrip createdDeliveryTrip = tripsService.createDeliveryTrip(deliveryTrip);
        return new ResponseEntity<>(createdDeliveryTrip, HttpStatus.CREATED);
    }

    //DELIVERYMAN
    @GetMapping("/trip/{id}/assigned")
    @PreAuthorize("hasRole('DELIVERYMAN')")
    public ResponseEntity<List<DeliveryTrip>> viewAllAssignedTrips(@PathVariable ObjectId id){
        return new ResponseEntity<>(tripsService.getAllAssignedTrips(id),HttpStatus.OK);
    }

    //DELIVERYMAN
    @PutMapping("/trip/{tripId}/accept")
    @PreAuthorize("hasRole('DELIVERYMAN')")
    public ResponseEntity<DeliveryTrip> acceptTrip(@PathVariable String tripId){
        DeliveryTrip acceptedTrip = tripsService.acceptTrip(tripId);
        if (acceptedTrip == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(acceptedTrip);
    }

    //DELIVERYMAN
    @PutMapping("/trip/{tripId}/start")
    @PreAuthorize("hasRole('DELIVERYMAN')")
    public ResponseEntity<DeliveryTrip> startTrip(@PathVariable String tripId){
        DeliveryTrip startedTrip = tripsService.startTrip(tripId);
        if (startedTrip == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(startedTrip);
    }

    //DELIVERYMAN
    @PutMapping("/trip/{tripId}/end")
    @PreAuthorize("hasRole('DELIVERYMAN')")
    public ResponseEntity<DeliveryTrip> endTrip(@PathVariable String tripId){
        DeliveryTrip endedTrip = tripsService.markTripAsEnded(tripId);
        if (endedTrip == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(endedTrip);
    }

}
