package com.delivery.DeliveryTask.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImportFilesService {

    void importUsersFromFile(MultipartFile file);


}
