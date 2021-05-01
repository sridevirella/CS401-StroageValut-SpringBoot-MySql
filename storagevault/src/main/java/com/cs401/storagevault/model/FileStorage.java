package com.cs401.storagevault.model;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorage {

    void initService() throws IOException;
    void storeFile(MultipartFile file) throws IOException;
    Stream<Path> loadAllFiles() throws IOException;
    Path loadFile(String filename);
    Resource loadAsResourceFile(String filename) throws Exception;
}
