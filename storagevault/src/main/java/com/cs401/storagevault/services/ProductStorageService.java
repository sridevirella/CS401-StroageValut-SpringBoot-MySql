package com.cs401.storagevault.services;

import com.cs401.storagevault.model.FileStorage;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class ProductStorageService {

    private final Path filePath = Paths.get("product-upload-directory");

    public void initService() throws IOException {
        Files.createDirectories(filePath);
    }

    public void storeFile(MultipartFile file) throws IOException {

        Path destinationPath = this.filePath.resolve(Paths.get(Objects.requireNonNull(file.getOriginalFilename()))).normalize().toAbsolutePath();
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
