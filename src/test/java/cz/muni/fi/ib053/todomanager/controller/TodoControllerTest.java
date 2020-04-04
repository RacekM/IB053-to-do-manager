package cz.muni.fi.ib053.todomanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.ib053.todomanager.TestApplication;
import cz.muni.fi.ib053.todomanager.dto.NewTaskDTO;
import cz.muni.fi.ib053.todomanager.entity.Task;
import cz.muni.fi.ib053.todomanager.entity.User;
import cz.muni.fi.ib053.todomanager.service.TodoService;
import org.hamcrest.core.Is;
import org.junit.Before;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
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

        private List<Task> tasks = new ArrayList<>();

        private static String asJsonString(final Object obj) {
                try {
                        return new ObjectMapper().writeValueAsString(obj);
                } catch (Exception e) {
                        throw new RuntimeException(e);
                }
        }

        @Before
        public void setUp() {
                User owner = new User();
                owner.setUsername("joe");
                Task task = new Task();
                task.setId(1L);
                task.setOwner(owner);
                task.setPrerequisites(new ArrayList<>());
                task.setEstimatedFinishTime(1L);
                tasks.add(task);
                task = new Task();
                task.setOwner(owner);
                task.setId(2L);
                task.setPrerequisites(new ArrayList<>());
                task.setEstimatedFinishTime(2L);
                tasks.add(task);
                task = new Task();
                task.setOwner(owner);
                task.setId(3L);
                task.setPrerequisites(new ArrayList<>());
                task.setEstimatedFinishTime(3L);
                tasks.add(task);
        }

        @Test
        public void givenValidCredentials_whenLogin_thenReturnLoggedInTrue() throws Exception {
                // given
                String username = "joe";
                String password = "pass";
                given(service.login(username, password)).willReturn(true);
                // when
                // then
                mvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .header("username", "joe")
                        .header("password", "pass"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.loggedIn", Is.is(true)));
        }

        @Test
        public void givenValidCredentials_whenGetAllTasks_thenReturnAllUserTasks() throws Exception {
                String username = "joe";
                String password = "pass";
                given(service.getTaskList(username, password)).willReturn(tasks);
                // when
                // then
                mvc.perform(MockMvcRequestBuilders
                        .get("/tasks")
                        .header("username", "joe")
                        .header("password", "pass"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(3)))
                        .andExpect(jsonPath("$.[*].owner.username", containsInAnyOrder("joe", "joe", "joe")));
        }

        //todo needs to be fixed
        @Test
        public void givenValidCredentials_whenAddTasks_thenTaskWasAdded() throws Exception {
                String username = "joe";
                String password = "pass";
                Task taskToBeReturned = new Task();
                taskToBeReturned.setId(1L);
                NewTaskDTO taskToBeAdded = new NewTaskDTO();
                taskToBeAdded.setEstimatedFinishTime(2L);
                given(service.addTask(eq(username), eq(password), any(Task.class))).willReturn(taskToBeReturned);
                // when
                // then
                mvc.perform(MockMvcRequestBuilders
                        .post("/tasks")
                        .header("username", "joe")
                        .header("password", "pass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{estimatedFinishTime: 1, prerequisites: null, orderIndex:2, owner:null}")
                        .characterEncoding("utf-8"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$.id", is(1)));
        }

        //todo needs to be implemented
        @Test
        public void givenValidCredentials_whenChangeTask_thenTaskWasChanged() {
                assert false;
        }

        @Test
        public void givenValidCredentials_whenRemoveTask_thenTaskWasRemoved() throws Exception {
                String username = "joe";
                String password = "pass";
                doAnswer((i) -> tasks.removeIf(t -> t.getId() == 1L)).when(service).removeTask(username, password, 1L);
                given(service.getTaskList(username, password)).willReturn(tasks);

                // when
                // then
                mvc.perform(MockMvcRequestBuilders
                        .delete("/tasks/{id}", 1)
                        .header("username", "joe")
                        .header("password", "pass")
                        .characterEncoding("utf-8"))
                        .andDo(print())
                        .andExpect(status().isOk());

                mvc.perform(MockMvcRequestBuilders
                        .get("/tasks")
                        .header("username", "joe")
                        .header("password", "pass"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(2)))
                        .andExpect(jsonPath("$.[*].id", containsInAnyOrder(2, 3)));

        }

        @Test
        public void givenValidCredentials_whenGetTotalTime_thenReturnTotalTime() throws Exception {
                String username = "joe";
                String password = "pass";
                given(service.getTotalTime(username, password)).willReturn(100L);
                // when
                // then
                mvc.perform(MockMvcRequestBuilders
                        .get("/tasks/total_time")
                        .header("username", "joe")
                        .header("password", "pass")
                        .characterEncoding("utf-8"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().string("100"));
        }


}
