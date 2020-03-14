package cz.muni.fi.ib053.todomanager.controller;

import cz.muni.fi.ib053.todomanager.entity.Task;
import cz.muni.fi.ib053.todomanager.entity.User;
import cz.muni.fi.ib053.todomanager.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TodoController {
        private final TodoService toDoService;

        @Autowired
        public TodoController(TodoService toDoService) {
                this.toDoService = toDoService;
        }

        @PostMapping("/login")
        public Boolean login(@RequestBody User user) {
                return toDoService.login(user.getUsername(), user.getPassword());
        }

        @PostMapping("/tasks")
        public List<Task> getTaskList(@RequestBody User user) {
                return toDoService.getTaskList(user.getUsername(), user.getPassword());
        }

        @PostMapping("/tasks/add")
        //todo nepojde musi byt jeden objekt
        public Long addTask(User user, Task task) {
                return toDoService.addTask(user.getUsername(), user.getPassword(), task);
        }


        public boolean changeTask(String username, String password, Task task) {
                return true;
        }

        public boolean removeTask(String username, String password, Long taskId) {
                return true;
        }

        public Long getTotalTime(String username, String password) {
                return -1L;
        }


}
