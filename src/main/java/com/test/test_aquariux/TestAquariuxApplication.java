package com.test.test_aquariux;

import com.test.test_aquariux.entity.User;
import com.test.test_aquariux.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling
public class TestAquariuxApplication implements CommandLineRunner {

	@Autowired
	UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(TestAquariuxApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	public void run(String... args) throws Exception {
		User user = new User();
		user.setUsername("admin");
		user.setPassword("admin");
		user.setWalletBalance(BigDecimal.valueOf(50000));
		userService.save(user);
	}
}
