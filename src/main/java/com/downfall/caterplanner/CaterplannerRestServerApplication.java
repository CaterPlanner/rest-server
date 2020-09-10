package com.downfall.caterplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@ServletComponentScan
@EnableJpaAuditing
public class CaterplannerRestServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaterplannerRestServerApplication.class, args);
	}

}
