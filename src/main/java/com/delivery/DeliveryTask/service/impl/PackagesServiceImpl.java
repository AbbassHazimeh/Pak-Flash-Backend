package com.delivery.DeliveryTask.service.impl;

import com.delivery.DeliveryTask.enums.PackageStatus;
import com.delivery.DeliveryTask.enums.Role;
import com.delivery.DeliveryTask.exception.InvalidRequestException;
import com.delivery.DeliveryTask.exception.PackageNotFoundException;
import com.delivery.DeliveryTask.exception.UserNotFoundException;
import com.delivery.DeliveryTask.model.Customer;
import com.delivery.DeliveryTask.model.PackageOrder;
import com.delivery.DeliveryTask.repo.PackageRepository;
import com.delivery.DeliveryTask.service.PackagesService;
import com.delivery.DeliveryTask.service.dto.PackageOrderDTO;
import com.delivery.DeliveryTask.service.dto.UserClassDTO;
import com.delivery.DeliveryTask.service.mapper.PackageOrderMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PackagesServiceImpl implements PackagesService {

    private final PackageRepository packageRepository;
    private final UsersServiceImpl usersService;
    private final PackageOrderMapper packageOrderMapper;
    public PackagesServiceImpl(PackageRepository packageRepository, UsersServiceImpl usersService, PackageOrderMapper packageOrderMapper) {
        this.packageRepository = packageRepository;
        this.usersService = usersService;
        this.packageOrderMapper = packageOrderMapper;
    }
    @Override
    public PackageOrderDTO createPackage(PackageOrderDTO newPackageDto) {
        if (newPackageDto == null) {
            throw new InvalidRequestException("Package data must not be null.");
        }
        String customerId = String.valueOf(newPackageDto.getCustomerId());
        if (customerId.isEmpty() || customerId.isBlank()) {
            throw new InvalidRequestException("Customer ID must be provided.");
        }

        Optional<UserClassDTO> optionalUser = usersService.getUserById(customerId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Customer not found with ID: " + customerId);
        }

        UserClassDTO user = optionalUser.get();
        if(user.getRole() != Role.CUSTOMER){
            throw new InvalidRequestException("not Customer");
        }
        Customer customer = user.getCustomer();
        if(customer == null){
            throw new InvalidRequestException("Customer details not found.");
        }
        PackageOrder packageOrder = packageOrderMapper.toEntity(newPackageDto);
        packageOrder.setStatus(PackageStatus.PENDING);
        packageOrder.setDeliveryTripId(null);
        packageOrder.setPhone(customer.getPhone());
        packageRepository.save(packageOrder);
        return packageOrderMapper.toDto(packageOrder);
    }
    public void savePackageFromTrip(PackageOrder packageOrder) {
        if (packageOrder == null) {
            throw new InvalidRequestException("PackageOrder must not be null.");
        }
        packageRepository.save(packageOrder);
    }
    @Override
    public List<PackageOrderDTO> getAllPackages() {

        return packageRepository.findAll().stream().map(packageOrderMapper::toDto).toList();
    }
    @Override
    public List<PackageOrderDTO> getAllAssignedPackages() {
        List<PackageOrderDTO> assignedPackages = packageRepository.findByStatusIn(List.of(PackageStatus.ASSIGNED))
                .stream()
                .map(packageOrderMapper::toDto).toList();
        if (assignedPackages.isEmpty()) {
            throw new PackageNotFoundException("No assigned packages");
        }
        return assignedPackages;
    }
    @Override
    public List<PackageOrderDTO> getAllAssignedPackagesByCustomer(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new InvalidRequestException("Customer ID must be provided.");
        }
        List<PackageOrderDTO> assignedPackages = packageRepository.findByCustomerIdAndStatusIn(customerId, List.of(PackageStatus.ASSIGNED))
                .stream()
                .map(packageOrderMapper::toDto).toList();
        if (assignedPackages.isEmpty()) {
            throw new InvalidRequestException("No assigned packages found for customer ID: " + customerId);
        }
        return assignedPackages;
    }
    @Override
    public List<PackageOrderDTO> getAllAssignedPackagesByDeliveryMan(String deliveryManId) {
        if (deliveryManId == null || deliveryManId.isBlank()) {
            throw new InvalidRequestException("Customer ID must be provided.");
        }
        List<PackageOrderDTO> assignedPackages = packageRepository.findByDeliveryManIdAndStatusIn(deliveryManId, List.of(PackageStatus.ASSIGNED))
                .stream()
                .map(packageOrderMapper::toDto).toList();
        if (assignedPackages.isEmpty()) {
            throw new InvalidRequestException("No assigned packages found for delivery man ID: " + deliveryManId);
        }
        return assignedPackages;
    }
    @Override
    public PackageOrderDTO markPackageAsDelivered(String id){
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("Package ID must be provided.");
        }

        PackageOrder pkg = packageRepository.findById(id)
                .orElseThrow(() -> new PackageNotFoundException("Package not found with ID: " + id));
        if(pkg.getStatus() == PackageStatus.ASSIGNED){
            pkg.setStatus(PackageStatus.DELIVERED);
        }else{
            throw new InvalidRequestException("Package with Id:" + id + " is not assigned yet.");
        }

        packageRepository.save(pkg);
        return packageOrderMapper.toDto(pkg);
    }
    @Override
    public PackageOrderDTO markPackageAsConfirmed(String id){
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("Package ID must be provided.");
        }
        PackageOrder pkg = packageRepository.findById(id).orElseThrow(() -> new PackageNotFoundException("Package not found with ID: "+ id));
        if(pkg.getStatus() == PackageStatus.DELIVERED){
            pkg.setStatus(PackageStatus.CONFIRMED);
        }else{
            throw new InvalidRequestException("Package with Id:" + id + " is not DELIVERED yet.");
        }
        packageRepository.save(pkg);
        return packageOrderMapper.toDto(pkg);
    }

    @Override
    public PackageOrderDTO findPackageById(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("Package ID must be provided.");
        }
        PackageOrder founded = packageRepository.findPackageById(id);
        if (founded == null) {
            throw new PackageNotFoundException("Package not found with ID: " + id);
        }
        return packageOrderMapper.toDto(founded);
    }
}
