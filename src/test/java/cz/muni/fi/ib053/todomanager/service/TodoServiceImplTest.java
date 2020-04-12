package cz.muni.fi.ib053.todomanager.service;

import cz.muni.fi.ib053.todomanager.TestApplication;
import cz.muni.fi.ib053.todomanager.entity.Task;
import cz.muni.fi.ib053.todomanager.entity.User;
import cz.muni.fi.ib053.todomanager.exceptions.UnauthorizedException;
import cz.muni.fi.ib053.todomanager.repository.TaskRepository;
import cz.muni.fi.ib053.todomanager.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestApplication.class)
public class TodoServiceImplTest {

        @Autowired
        private TodoService todoService;

        @MockBean
        private UserRepository userRepository;

        @MockBean
        private TaskRepository taskRepository;

        private List<Task> tasks;
        private User joe;

        @Before
        public void setUp() {
                joe = new User("joseph", "black", "joe", "pass");

                Mockito.when(userRepository.findByUsername(joe.getUsername()))
                        .thenReturn(joe);

                tasks = new ArrayList<>();
                Task task = new Task();
                task.setId(1L);
                task.setOwner(joe);
                task.setPrerequisites(new ArrayList<>());
                task.setEstimatedFinishTime(1L);
                task.setOrderIndex(10L);
                tasks.add(task);
                task = new Task();
                task.setOwner(joe);
                task.setId(2L);
                task.setPrerequisites(new ArrayList<>());
                task.setEstimatedFinishTime(2L);
                task.setOrderIndex(20L);
                tasks.add(task);
                task = new Task();
                task.setOwner(joe);
                task.setId(3L);
                task.setPrerequisites(new ArrayList<>());
                task.setEstimatedFinishTime(3L);
                task.setOrderIndex(30L);
                tasks.add(task);

                Mockito.when(taskRepository.findAllByOwner_Id(joe.getId()))
                        .thenReturn(tasks);
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

        @Test
        public void whenValidUserGetTasks_thenReturnTasks() {
                //given

                //when
                List<Task> retrievedTasks = todoService.getTaskList(joe.getUsername(), joe.getPassword());

                //then
                assertEquals(tasks, retrievedTasks);
        }

        @Test
        public void whenValidUserAddTask_thenReturnPersistedTask() {
                //given
                Task newTask = new Task();
                newTask.setOwner(joe);
                newTask.setEstimatedFinishTime(50L);
                newTask.setOrderIndex(100L);
                newTask.setPrerequisites(Collections.emptyList());

                Mockito.when(taskRepository.save(newTask))
                        .thenReturn(newTask);

                //when
                Task persistedTask = todoService.addTask(joe.getUsername(), joe.getPassword(), newTask);

                //then
                assertEquals(newTask, persistedTask);
                verify(taskRepository, times(1)).save(newTask);
        }

        @Test
        public void whenValidUserChangeTask_thenChangeTask() {
                //given
                Long oldTaskId = 0L;
                Task oldTask = tasks.get(oldTaskId.intValue());
                Task changeTask = new Task();
                changeTask.setEstimatedFinishTime(oldTask.getEstimatedFinishTime() + 1);
                changeTask.setOwner(oldTask.getOwner());
                changeTask.setPrerequisites(oldTask.getPrerequisites());
                changeTask.setOrderIndex(oldTask.getOrderIndex() + 1);

                when(taskRepository.getOne(oldTaskId)).thenReturn(oldTask);
                when(taskRepository.save(oldTask)).thenReturn(oldTask);

                //when
                Task changedTask = todoService.changeTask(joe.getUsername(), joe.getPassword(), oldTaskId, changeTask);

                //then
                assertEquals(changeTask, changedTask);
                verify(taskRepository, times(1)).save(oldTask);
        }

        @Test
        public void whenValidUserUseSameOrderIndex_thenChangeOtherOrderIndexes() {
                //given
                Long oldTaskId = 0L;
                Task oldTask = tasks.get(oldTaskId.intValue());

                Task changeTask = new Task();
                changeTask.setEstimatedFinishTime(oldTask.getEstimatedFinishTime());
                changeTask.setOwner(oldTask.getOwner());
                changeTask.setPrerequisites(oldTask.getPrerequisites());
                changeTask.setOrderIndex(oldTask.getOrderIndex());
                Long orderIndex = oldTask.getOrderIndex();

                when(taskRepository.getOne(oldTaskId)).thenReturn(oldTask);
                when(taskRepository.save(oldTask)).thenReturn(oldTask);

                //when
                Task changedTask = todoService.changeTask(joe.getUsername(), joe.getPassword(), oldTaskId, changeTask);

                //then
                assertEquals(changeTask, changedTask);
                verify(taskRepository, times(1)).save(oldTask);
                assertTrue(tasks.stream().anyMatch(t -> t.getOrderIndex().equals(orderIndex)));
        }

        @Test
        public void whenValidUserRemoveTask_thenTaskIsRemoved() {
                //given
                Long taskId = tasks.get(0).getId();

                //when
                todoService.removeTask(joe.getUsername(), joe.getPassword(), taskId);

                //then
                verify(taskRepository, times(1)).deleteById(taskId);
        }

        @Test
        public void whenValidUserGetTotalTime_thenReturnTotalTime() {
                //given
                Long totalTime = tasks.stream().map(Task::getEstimatedFinishTime).reduce(0L, Long::sum);

                //when
                Long returnedTime = todoService.getTotalTime(joe.getUsername(), joe.getPassword());

                //then
                assertEquals(totalTime, returnedTime);
        }
}
