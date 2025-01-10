package com.logiant.modules.license;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class LicenseCoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(LicenseCoreApplication.class, args);
	}
}