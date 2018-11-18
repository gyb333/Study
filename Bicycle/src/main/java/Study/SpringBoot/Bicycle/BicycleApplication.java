package Study.SpringBoot.Bicycle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, MongoAutoConfiguration.class})
@SpringBootApplication

//public class BicycleApplication {
public class BicycleApplication {

    public static void main(String[] args) {

        SpringApplication.run(BicycleApplication.class, args);

    }

    //mvn spring-boot:run 回车启动项目
}
