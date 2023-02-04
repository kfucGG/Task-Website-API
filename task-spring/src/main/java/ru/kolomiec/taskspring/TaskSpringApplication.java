package ru.kolomiec.taskspring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import ru.kolomiec.taskspring.entity.Task;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class TaskSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskSpringApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper().findAndRegisterModules();
	}
	@Bean
	public RestTemplate  restTemplate() {return new RestTemplate();}

}
