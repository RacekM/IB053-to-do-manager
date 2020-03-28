package cz.muni.fi.ib053.todomanager;

import com.google.common.base.Predicates;
import cz.muni.fi.ib053.todomanager.entity.Task;
import cz.muni.fi.ib053.todomanager.entity.User;
import cz.muni.fi.ib053.todomanager.repository.TaskRepository;
import cz.muni.fi.ib053.todomanager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Optional;

@Configuration
@SpringBootApplication
@EnableSwagger2
public class ToDoManagerApplication {

        public static void main(String[] args) {
                SpringApplication.run(ToDoManagerApplication.class, args);
        }

        @Bean
        public Docket api() {
                return new Docket(DocumentationType.SWAGGER_2)
                        .select()
                        .apis(RequestHandlerSelectors.any())
                        .paths(Predicates.not(PathSelectors.regex("/error.*")))//<6>, regex must be in double quotes.
                        .build()
                        .apiInfo(apiInfo())
                        .useDefaultResponseMessages(false)
                        ;
        }

        private ApiInfo apiInfo() {

                return new ApiInfoBuilder()
                        .title("My REST API")
                        .description("Some custom description of API.")
                        .termsOfServiceUrl("Terms of service")
                        .contact(new Contact("John Doe", "www.example.com", "myeaddress@company.com"))
                        .license("License of API")
                        .licenseUrl("API license URL")
                        .version("1.0")
                        .build();
        }

        @Bean
        public CommandLineRunner demo(UserRepository userRepository, TaskRepository taskRepository) {
                return args -> {
                        User matus = userRepository.save(new User("Matúš", "Raček", "kangaroo", "12345"));
                        User adam = userRepository.save(new User("Adam", "Vanko", "ReDo", "somOpica"));
                        User joe = userRepository.save(new User("Joe", "Smith", "smithy", "pass"));

                        System.out.println(matus.getId());
                        System.out.println(adam.getId());
                        System.out.println(joe.getId());

                        User kangaroo = userRepository.findByUsername("kangaroo");
                        System.out.println(matus.equals(kangaroo));

                        Optional<User> redo = userRepository.findById(adam.getId());
                        System.out.println(redo.isEmpty() || (adam.equals(redo.get())));

                        Task task = new Task();
                        task.setUser(joe);
                        task.setEstimatedFinishTime(10L);
                        System.out.println(taskRepository.findAllByUser_Id(joe.getId()).isEmpty());
                        taskRepository.save(task);
                        System.out.println((taskRepository.findAllByUser_Id(joe.getId()).size() == 1));
                };
        }

}
