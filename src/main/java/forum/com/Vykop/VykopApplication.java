package forum.com.Vykop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication()
public class VykopApplication {

	public static void main(String[] args) {
		SpringApplication.run(VykopApplication.class, args);
	}
}
