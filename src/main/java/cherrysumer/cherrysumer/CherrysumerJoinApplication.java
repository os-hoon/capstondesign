package cherrysumer.cherrysumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CherrysumerJoinApplication {

	public static void main(String[] args) {
		SpringApplication.run(CherrysumerJoinApplication.class, args);
	}

}
