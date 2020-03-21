package cz.muni.fi.ib053.todomanager.controller;

import cz.muni.fi.ib053.todomanager.entity.Task;
import cz.muni.fi.ib053.todomanager.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoController {
        private final TodoService toDoService;

        @Autowired
        public TodoController(TodoService toDoService) {
                this.toDoService = toDoService;
        }

        @PostMapping("/login")
        public Boolean login(@RequestHeader("username") String username, @RequestHeader("password") String password) {
                return toDoService.login(username, password);
        }

        @GetMapping("/tasks")
        public List<Task> getTaskList(@RequestHeader("username") String username, @RequestHeader("password") String password) {
                return toDoService.getTaskList(username, password);
        }

        @PostMapping("/tasks")
        public Task addTask(@RequestHeader("username") String username, @RequestHeader("password") String password, @RequestBody Task task) {
                return toDoService.addTask(username, password, task);
        }

        @PostMapping("/tasks/{taskId}")
        public Task addSubTask(@RequestHeader("userName") String username, @RequestHeader("password") String password, @PathVariable Long taskId,
                            @RequestBody Task task) {
                return toDoService.addSubTask(username, password, taskId, task);
        }

        @PutMapping("/tasks/{taskId}")
        public Task changeTask(@RequestHeader("username") String username, @RequestHeader("password") String password, @PathVariable Long taskId,
                                  @RequestBody Task task) {
                return toDoService.changeTask(username, password, taskId, task);
        }

        @DeleteMapping("/tasks/{taskId}")
        public void removeTask(@RequestHeader("username") String username, @RequestHeader("password") String password, @PathVariable Long taskId) {
                toDoService.removeTask(username, password, taskId);
        }

        @GetMapping("/tasks/totalTime")
        public Long getTotalTime(@RequestHeader("username") String username, @RequestHeader("password") String password) {
                return toDoService.getTotalTime(username, password);
        }


}
