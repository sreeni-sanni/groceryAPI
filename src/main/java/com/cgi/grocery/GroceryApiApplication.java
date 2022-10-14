package com.cgi.grocery;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cgi.grocery.service.XlsxFileReadService;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
public class GroceryApiApplication implements CommandLineRunner {

	@Autowired
	XlsxFileReadService xlsxFileReadService;

	public static void main(String[] args) {
		SpringApplication.run(GroceryApiApplication.class, args);
	}

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.cgi.grocery")).build();
	}

	@Override
	public void run(String... args) throws Exception {
		InputStream is=getResourceFileAsInputStream("vegetables.xlsx");
		xlsxFileReadService.readXlsxFile(is);
	}
	
	public InputStream getResourceFileAsInputStream(String fileName) {
	    ClassLoader classLoader = getClass().getClassLoader();
		return classLoader.getResourceAsStream(fileName);
	}

}
