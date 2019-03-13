package co.amazing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({"classpath:messages.properties"})
public class AmazingCoApplication {
	public static void main(String[] args) {
		SpringApplication.run(AmazingCoApplication.class, args);
	}
}
