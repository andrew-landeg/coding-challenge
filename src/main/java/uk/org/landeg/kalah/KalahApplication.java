package uk.org.landeg.kalah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import uk.org.landeg.kalah.resolver.RequestUriResolver;

@SpringBootApplication
public class KalahApplication {

	public static void main(String[] args) {
		SpringApplication.run(KalahApplication.class, args);
	}
}
