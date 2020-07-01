package cz.muni.fi.ib053.todomanager.controller;

import cz.muni.fi.ib053.todomanager.dto.*;
import cz.muni.fi.ib053.todomanager.entity.Task;
import cz.muni.fi.ib053.todomanager.service.TodoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

        @ApiOperation(value = "Verify credentials of the user", response = LoginResultDTO.class)
        @PostMapping("/login")
        public LoginResultDTO login(
                @ApiParam(name = "username", value = "Username of the user which should be authenticated", required = true)
                @RequestHeader("username") String username,
                @ApiParam(name = "password", value = "Password of the user which should be authenticated", required = true)
                @RequestHeader("password") String password) {
                LoginResultDTO loginResultDTO = new LoginResultDTO();
                loginResultDTO.setLoggedIn(todoService.login(username, password));
                return loginResultDTO;
        }

        @ApiOperation(value = "Get task list of the corresponding user", response = TaskDTO.class, responseContainer = "List")
        @GetMapping("/tasks")
        public List<TaskDTO> getTaskList(
                @ApiParam(name = "username", value = "Username of the user which should be authenticated", required = true)
                @RequestHeader("username") String username,
                @ApiParam(name = "password", value = "Password of the user which should be authenticated", required = true)
                @RequestHeader("password") String password) {
                List<Task> tasks = todoService.getTaskList(username, password);
                return tasks.stream()
                        //.sorted(Comparator.comparing(Task::getOrderIndex))
                        .map(this::toTaskDTO).collect(Collectors.toList());
        }

        @ApiOperation(value = "Add new task to the user", response = TaskDTO.class)
        @PostMapping("/tasks")
        public TaskDTO addTask(
                @ApiParam(name = "username", value = "Username of the user which should be authenticated", required = true)
                @RequestHeader("username") String username,
                @ApiParam(name = "password", value = "Password of the user which should be authenticated", required = true)
                @RequestHeader("password") String password,
                @ApiParam(name = "newTaskDTO", value = "New task which should be added to the users tasks", required = true)
                @RequestBody @Valid NewTaskDTO newTaskDTO) {
                Task task = modelMapper.map(newTaskDTO, Task.class);
                return toTaskDTO(todoService.addTask(username, password, task));
        }

        @ApiOperation(value = "Modify existing task", response = TaskDTO.class)
        @PutMapping("/tasks/{taskId}")
        public TaskDTO changeTask(
                @ApiParam(name = "username", value = "Username of the user which should be authenticated", required = true)
                @RequestHeader("username") String username,
                @ApiParam(name = "password", value = "Password of the user which should be authenticated", required = true)
                @RequestHeader("password") String password,
                @ApiParam(name = "taskId", value = "Id of the task which should be updated", required = true)
                @PathVariable Long taskId,
                @ApiParam(name = "changeTaskDTO", value = "New values which should be updated in existing task with given id", required = true)
                @RequestBody @Valid ChangeTaskDTO changeTaskDTO) {
                Task task = modelMapper.map(changeTaskDTO, Task.class);
                return toTaskDTO(todoService.changeTask(username, password, taskId, task));
        }

        @ApiOperation(value = "Remove existing task")
        @DeleteMapping("/tasks/{taskId}")
        public void removeTask(
                @ApiParam(name = "username", value = "Username of the user which should be authenticated", required = true)
                @RequestHeader("username") String username,
                @ApiParam(name = "password", value = "Password of the user which should be authenticated", required = true)
                @RequestHeader("password") String password,
                @ApiParam(name = "taskId", value = "Id of the task which should be removed", required = true)
                @PathVariable Long taskId) {
                todoService.removeTask(username, password, taskId);
        }

        @ApiOperation(value = "Get estimated time of all of the tasks", response = Long.class)
        @GetMapping("/tasks/total_time")
        public Long getTotalTime(
                @ApiParam(name = "username", value = "Username of the user which should be authenticated", required = true)
                @RequestHeader("username") String username,
                @ApiParam(name = "password", value = "Password of the user which should be authenticated", required = true)
                @RequestHeader("password") String password) {
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
