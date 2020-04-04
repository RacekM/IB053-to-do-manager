package cz.muni.fi.ib053.todomanager.repository;

import cz.muni.fi.ib053.todomanager.TestApplication;
import cz.muni.fi.ib053.todomanager.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = TestApplication.class)
public class UserRepositoryTest {

        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        private UserRepository userRepository;

        @Test
        public void whenFindByUsername_thenReturnUser() {
                // given
                User joe = new User("joseph", "black", "joe", "pass");
                entityManager.persist(joe);
                entityManager.flush();

                // when
                User found = userRepository.findByUsername(joe.getUsername());

                // then
                assertThat(found.getId()).isEqualTo(joe.getId());
                assertThat(found.getName()).isEqualTo(joe.getName());
        }

        @Test
        public void whenFindByUsernameNonExisting_thenReturnNull() {
                // given

                // when
                User found = userRepository.findByUsername("joe");

                // then
                assertThat(found).isEqualTo(null);
        }
}
