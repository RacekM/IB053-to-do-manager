package cz.muni.fi.ib053.todomanager.repository;

import cz.muni.fi.ib053.todomanager.TestApplication;
import cz.muni.fi.ib053.todomanager.entity.Task;
import cz.muni.fi.ib053.todomanager.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = TestApplication.class)
public class TaskRepositoryTest {

        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        private TaskRepository taskRepository;

        @Test
        public void wheFindAllByOwner_Id_thenReturnAllUserTasks() {
                // given
                User joe = new User("joseph", "black", "joe", "pass");
                User adam = new User("adam", "pink", "pinky", "pass");
                joe = entityManager.persist(joe);
                adam = entityManager.persist(adam);
                entityManager.flush();

                Task joeTask1 = entityManager.persist(new Task(joe, 1L, 1L, new ArrayList<>()));
                Task adamTask1 = entityManager.persist(new Task(adam, 1L, 1L, new ArrayList<>()));
                Task joeTask2 = entityManager.persist(new Task(joe, 1L, 2L, new ArrayList<>()));


                // when
                List<Task> allByOwner_id = taskRepository.findAllByOwner_Id(joe.getId());

                // then
                assertThat(allByOwner_id.size()).isEqualTo(2);
                assertThat(allByOwner_id).containsExactlyInAnyOrder(joeTask1, joeTask2);
        }

        @Test
        public void whenFindAllByNonExistingOwner_Id_thenReturnEmptyCollection() {
                // given

                // when
                List<Task> allByOwner_id = taskRepository.findAllByOwner_Id(2L);

                // then
                assertThat(allByOwner_id).isEmpty();
        }


}
