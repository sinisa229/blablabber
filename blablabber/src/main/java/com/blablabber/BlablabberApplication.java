package com.blablabber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@SuppressWarnings("unused")
@EnableSwagger2
@SpringBootApplication
public class BlablabberApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlablabberApplication.class, args);
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Blablabber")
				.select()
				.paths(input -> input.contains("analysis"))
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build().apiInfo(new ApiInfo("Blablabber", "Checks the PMD violations between a source and a target branch of merge requests", "", "", null, "", "", new ArrayList<>()));
	}
}
