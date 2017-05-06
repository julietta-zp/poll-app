package xyz.hedo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import xyz.hedo.config.JpaConfig;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class PollAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(new Class<?>[] {PollAppApplication.class, JpaConfig.class}, args);
	}
}
