package com.petmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulithic;

@SpringBootApplication
@Modulithic(systemName = "PetManagement")
public class PetManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetManagementApplication.class, args);
	}

}
