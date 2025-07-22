package com.delivery.DeliveryTask.service;

import com.delivery.DeliveryTask.enums.NotificationsType;
import com.delivery.DeliveryTask.enums.PackageStatus;
import com.delivery.DeliveryTask.exception.InvalidRequestException;
import com.delivery.DeliveryTask.model.Notifications;
import com.delivery.DeliveryTask.model.PackageOrder;
import com.delivery.DeliveryTask.repo.NotificationRepository;
import com.delivery.DeliveryTask.repo.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final PackageRepository packageRepository;
    private final NotificationRepository notificationRepository;

@Scheduled(fixedRate = 60 * 60 * 1000)
//@Scheduled(fixedRate = 10000)
    public void markFailed(){
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(72);
   //     LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(1);

        List<PackageOrder> packageOrders = packageRepository.findByStatusInAndTimeBefore(Arrays.asList(
                PackageStatus.ASSIGNED,PackageStatus.OUT_FOR_DELIVERY
        ),localDateTime);

        for( PackageOrder packageOrder : packageOrders){
            packageOrder.setStatus(PackageStatus.FAILED);
            packageRepository.save(packageOrder);

            Notifications notification = new Notifications();
            notification.setMessage("Package " + packageOrder.getId() + " failed to deliver on time.");
            notification.setType(NotificationsType.PACKAGE_DELAYED);
            notification.setDate(LocalDateTime.now());
            notificationRepository.save(notification);
        }


    }

}
