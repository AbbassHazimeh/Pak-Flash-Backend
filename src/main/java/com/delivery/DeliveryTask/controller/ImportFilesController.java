package com.delivery.DeliveryTask.controller;

import com.delivery.DeliveryTask.service.ImportFilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/import")
@RequiredArgsConstructor
public class ImportFilesController {
    private final ImportFilesService importFilesService;

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> importUsers(@RequestParam("file") MultipartFile file) {
        importFilesService.importUsersFromFile(file);
        return ResponseEntity.ok("File upload accepted.");
    }
}
