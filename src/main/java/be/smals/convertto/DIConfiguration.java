package be.smals.convertto;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by soldiertt on 18-07-14.
 */
@Configuration
@ComponentScan(value={"be.smals.convertto"})
@ImportResource("classpath:spring-module.xml") // XML with DataSource bean
public class DIConfiguration {

}

