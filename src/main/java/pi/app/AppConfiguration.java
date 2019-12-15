package pi.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfiguration {
	@Value("10")
	private int value;
	  
	@Value("Hello World")
	private String msg;
	
	public AppConfiguration() {
		System.out.println(value);
		System.out.println(msg);
	}
}
