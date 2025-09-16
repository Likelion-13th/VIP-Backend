package likelion13th.princess_edition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PrincessEditionApplication {
	public static void main(String[] args) {
		SpringApplication.run(PrincessEditionApplication.class, args);
	}
}