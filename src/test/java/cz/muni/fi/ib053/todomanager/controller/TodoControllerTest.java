package cz.muni.fi.ib053.todomanager.controller;

import cz.muni.fi.ib053.todomanager.TestApplication;
import cz.muni.fi.ib053.todomanager.service.TodoService;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(TodoController.class)
@ContextConfiguration(classes = TestApplication.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoService service;

    @Test
    public void givenValidCredentials_whenLogin_thenReturnLoggedInTrue() throws Exception {
        // given
        String username = "joe";
        String password = "password";
        given(service.login(username, password)).willReturn(true);

        // when
        // then
        mvc.perform(MockMvcRequestBuilders.post("/login")
                .header("username", "joe")
                .header("password", "pass"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.loggedIn", Is.is(true)));
    }
}
