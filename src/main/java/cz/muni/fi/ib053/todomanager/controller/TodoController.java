package cz.muni.fi.ib053.todomanager.controller;

import cz.muni.fi.ib053.todomanager.dto.*;
import cz.muni.fi.ib053.todomanager.entity.Task;
import cz.muni.fi.ib053.todomanager.service.TodoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Main controller of the todoManager application
 */

@RestController
public class TodoController {
        private final TodoService todoService;
        private final ModelMapper modelMapper;

        @Autowired
        public TodoController(TodoService todoService, ModelMapper modelMapper) {
                this.todoService = todoService;
                this.modelMapper = modelMapper;
        }

        @PostMapping("/login")
        public LoginResultDTO login(@RequestHeader("username") String username, @RequestHeader("password") String password) {
                LoginResultDTO loginResultDTO = new LoginResultDTO();
                loginResultDTO.setLoggedIn(todoService.login(username, password));
                return loginResultDTO;
        }

        @GetMapping("/tasks")
        public List<TaskDTO> getTaskList(@RequestHeader("username") String username, @RequestHeader("password") String password) {
                List<Task> tasks = todoService.getTaskList(username, password);
                return tasks.stream()
                        //.sorted(Comparator.comparing(Task::getOrderIndex))
                        .map(this::toTaskDTO).collect(Collectors.toList());
        }

        @PostMapping("/tasks")
        public TaskDTO addTask(@RequestHeader("username") String username, @RequestHeader("password") String password, @RequestBody @Valid NewTaskDTO newTaskDTO) {
                Task task = modelMapper.map(newTaskDTO, Task.class);
                return toTaskDTO(todoService.addTask(username, password, task));
        }

        @PutMapping("/tasks/{taskId}")
        public TaskDTO changeTask(@RequestHeader("username") String username, @RequestHeader("password") String password, @PathVariable Long taskId,
                                  @RequestBody @Valid ChangeTaskDTO changeTaskDTO) {
                Task task = modelMapper.map(changeTaskDTO, Task.class);
                return toTaskDTO(todoService.changeTask(username, password, taskId, task));
        }

        @DeleteMapping("/tasks/{taskId}")
        public void removeTask(@RequestHeader("username") String username, @RequestHeader("password") String password, @PathVariable Long taskId) {
                todoService.removeTask(username, password, taskId);
        }

        @GetMapping("/tasks/total_time")
        public Long getTotalTime(@RequestHeader("username") String username, @RequestHeader("password") String password) {
                return todoService.getTotalTime(username, password);
        }

        private TaskDTO toTaskDTO(Task task) {
                TaskDTO taskDTO = new TaskDTO();
                taskDTO.setId(task.getId());
                taskDTO.setOrderIndex(task.getOrderIndex());
                taskDTO.setEstimatedFinishTime(task.getEstimatedFinishTime());
                taskDTO.setOwner(modelMapper.map(task.getOwner(), UserDTO.class));
                taskDTO.setPrerequisites(task.getPrerequisites().stream().map(this::toTaskDTO).collect(Collectors.toList()));
                return taskDTO;
        }
}
