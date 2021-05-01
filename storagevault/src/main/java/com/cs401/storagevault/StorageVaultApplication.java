package com.cs401.storagevault;

import com.cs401.storagevault.model.FileStorage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StorageVaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorageVaultApplication.class, args);
	}

	@Bean
	CommandLineRunner init(FileStorage fileStorage) {
		return (args) -> {
			fileStorage.initService();
		};
	}
}