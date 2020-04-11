package cz.muni.fi.ib053.todomanager;

import com.google.common.base.Predicates;
import cz.muni.fi.ib053.todomanager.entity.Task;
import cz.muni.fi.ib053.todomanager.entity.User;
import cz.muni.fi.ib053.todomanager.repository.TaskRepository;
import cz.muni.fi.ib053.todomanager.repository.UserRepository;
import org.modelmapper.ModelMapper;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@SpringBootApplication
@EnableSwagger2
public class ToDoManagerApplication {

        public static void main(String[] args) {
                SpringApplication.run(ToDoManagerApplication.class, args);
        }

        @Bean
        public ModelMapper modelMapper() {
                return new ModelMapper();
        }

        @Bean
        public Docket api() {
                return new Docket(DocumentationType.SWAGGER_2)
                        .select()
                        .apis(RequestHandlerSelectors.any())
                        .paths(Predicates.not(PathSelectors.regex("/error.*")))
                        .build()
                        .apiInfo(apiInfo())
                        .useDefaultResponseMessages(false);
        }

        private ApiInfo apiInfo() {

                return new ApiInfoBuilder()
                        .title("Todo-manager REST API")
                        .description("REST API of the application used for task managing.")
                        .contact(new Contact("Adam Vanko", "", "445310@mail.muni.cz"))
                        .license("Apache 2.0")
                        .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                        .version("1.0")
                        .build();
        }

        @Bean
        public CommandLineRunner demo(UserRepository userRepository, TaskRepository taskRepository) {
                return args -> {
                        User matus = userRepository.save(new User("Matúš", "Raček", "kangaroo", "12345"));
                        User adam = userRepository.save(new User("Adam", "Vanko", "redo", "heslo"));
                        User joe = userRepository.save(new User("Joe", "Smith", "joe", "pass"));

                        System.out.println(matus.getId());
                        System.out.println(adam.getId());
                        System.out.println(joe.getId());

                        User kangaroo = userRepository.findByUsername("kangaroo");
                        System.out.println(matus.equals(kangaroo));

                        Optional<User> redo = userRepository.findById(adam.getId());
                        System.out.println(redo.isEmpty() || (adam.equals(redo.get())));

                        Task task = new Task();
                        task.setOwner(joe);
                        task.setEstimatedFinishTime(10L);
                        task.setOrderIndex(0L);

                        Task prerequisite1 = new Task();
                        prerequisite1.setOwner(joe);
                        prerequisite1.setEstimatedFinishTime(50L);
                        prerequisite1.setOrderIndex(1L);

                        Task prerequisite2 = new Task();
                        prerequisite2.setOwner(joe);
                        prerequisite2.setEstimatedFinishTime(25L);
                        prerequisite2.setOrderIndex(2L);

                        List<Task> prerequisites = new ArrayList<>();
                        prerequisites.add(prerequisite1);
                        task.setPrerequisites(prerequisites);

                        taskRepository.save(prerequisite1);
                        taskRepository.save(task);

                        Task task2 = new Task();
                        task2.setEstimatedFinishTime(34L);
                        task2.setPrerequisites(prerequisites);
                        task2.setOrderIndex(3L);
                        task2.setOwner(joe);
                        taskRepository.save(task2);

                        System.out.println(taskRepository.findAllByOwner_Id(joe.getId()).isEmpty());
                        System.out.println((taskRepository.findAllByOwner_Id(joe.getId()).size() == 1));
                };
        }

}
