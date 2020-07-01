package cz.muni.fi.ib053.todomanager;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class TestApplication {

        public static void main(String[] args) {
                SpringApplication.run(TestApplication.class, args);
        }

        @Bean
        public ModelMapper modelMapper() {
                return new ModelMapper();
        }

}