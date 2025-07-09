package com.debabrata.AirBnbApllication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AirBnbApllicationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirBnbApllicationApplication.class, args);
	}

}
