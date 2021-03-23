package com.senla.srs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Starter {

	public static void main(String[] args) {
		SpringApplication.run(Starter.class, args);
	}

//	@Bean
//	public ModelMapper modelMapper() {
//		ModelMapper mapper = new ModelMapper();
//		mapper.getConfiguration()
//				.setMatchingStrategy(MatchingStrategies.STRICT)
//				.setFieldMatchingEnabled(true)
//				.setSkipNullEnabled(true)
//				.setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
//		return mapper;
//	}

}
