package cz.muni.fi.ib053.todomanager.service;

import cz.muni.fi.ib053.todomanager.TestApplication;
import cz.muni.fi.ib053.todomanager.entity.User;
import cz.muni.fi.ib053.todomanager.exceptions.UnauthorizedException;
import cz.muni.fi.ib053.todomanager.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestApplication.class)
public class TodoServiceImplTest {

    @Autowired
    private TodoService todoService;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() {
        User joe = new User("joseph", "black", "joe", "pass");

        Mockito.when(userRepository.findByUsername(joe.getUsername()))
                .thenReturn(joe);
    }

    @Test
    public void whenValidUsernameAndPassword_thenLoggedInTrue() {
        // given

        // when
        boolean loggedIn = todoService.login("joe", "pass");

        // then
        assertTrue(loggedIn);
    }

    @Test(expected = UnauthorizedException.class)
    public void whenInvalidUsername_thenUnauthorizedException() {
        // given

        // when
        boolean loggedIn = todoService.login("invalid-username", "pass");

        // then
    }

    @Test
    public void whenInvalidPassword_thenLoggedInFalse() {
        // given

        // when
        boolean loggedIn = todoService.login("joe", "invalid-pass");

        // then
        assertFalse(loggedIn);
    }
}
