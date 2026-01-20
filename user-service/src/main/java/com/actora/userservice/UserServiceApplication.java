package com.actora.userservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.actora.userservice", "com.actora.common"})
@EnableDiscoveryClient
@EnableFeignClients
@OpenAPIDefinition(
	info = @Info(
		title = "User Service API",
		version = "1.0.0",
		description = "REST API for User Management in the Microservices Architecture",
		contact = @Contact(
			name = "Actora Team",
			email = "support@actora.com"
		),
		license = @License(
			name = "Apache 2.0",
			url = "https://www.apache.org/licenses/LICENSE-2.0"
		)
	)
)
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
