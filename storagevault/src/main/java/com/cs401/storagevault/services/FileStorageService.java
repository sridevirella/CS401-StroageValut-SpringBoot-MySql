package com.cs401.storagevault.services;

import com.cs401.storagevault.model.FileStorage;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
public class FileStorageService implements FileStorage {

    private final Path filePath = Paths.get("file-upload-directory");

    @Override
    public void initService() throws IOException {
        Files.createDirectories(filePath);
    }

    @Override
    public void storeFile(MultipartFile file) throws IOException {


        Path destinationPath = this.filePath.resolve(Paths.get(Objects.requireNonNull(file.getOriginalFilename()))).normalize().toAbsolutePath();
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        }

    }

    @Override
    public Stream<Path> loadAllFiles() throws IOException {

        return Files.walk(this.filePath, 1).filter(path -> !path.equals(this.filePath)).map(this.filePath::relativize);
    }

    @Override
    public Path loadFile(String filename) {
        return filePath.resolve(filename);
    }

    @Override
    public Resource loadAsResourceFile(String filename) throws Exception {

        Path file = loadFile(filename);
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable())
            return resource;
        else
            throw new Exception("Couldn't not read file..");
    }
}
