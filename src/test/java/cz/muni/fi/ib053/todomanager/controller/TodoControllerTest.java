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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                String password = "pass";
                given(service.login(username, password)).willReturn(true);
                // when
                // then
                mvc.perform(MockMvcRequestBuilders.post("/login")
                        .header("username", "joe")
                        .header("password", "pass"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.loggedIn", Is.is(true)));
        }
}
