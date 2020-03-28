package cz.muni.fi.ib053.todomanager.service;

import cz.muni.fi.ib053.todomanager.entity.Task;
import cz.muni.fi.ib053.todomanager.entity.User;
import cz.muni.fi.ib053.todomanager.exceptions.EntityNotFoundException;
import cz.muni.fi.ib053.todomanager.exceptions.UnauthorizedException;
import cz.muni.fi.ib053.todomanager.repository.TaskRepository;
import cz.muni.fi.ib053.todomanager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {
        private static final Logger LOG = LoggerFactory.getLogger(TodoServiceImpl.class);
        private final UserRepository userRepository;
        private final TaskRepository taskRepository;

        @Autowired
        public TodoServiceImpl(UserRepository userRepository, TaskRepository taskRepository) {
                this.userRepository = userRepository;
                this.taskRepository = taskRepository;
        }

        @Override
        public Boolean login(String username, String password) {
                LOG.info("Logging works");
                User user = userRepository.findByUsername(username);
                if (user == null) {
                        throw new UnauthorizedException(username);
                }
                return user.getPassword().equals(password);
        }

        @Override
        public List<Task> getTaskList(String username, String password) {
                login(username, password);
                User user = userRepository.findByUsername(username);
                return user.getTodos();
        }

        @Override
        public Task addTask(String username, String password, Task task) {
                login(username, password);
                User user = userRepository.findByUsername(username);
                task.setUser(user);
                return taskRepository.save(task);
        }

        @Override
        public Task changeTask(String username, String password, Long taskId, Task task) {
                login(username, password);
                return taskRepository.findById(taskId)
                        .map(t -> {
                                //t.setParentTask(task.getParentTask());
                                //t.setUser(task.getUser());
                                t.setEstimatedFinishTime(task.getEstimatedFinishTime());
                                //t.setPrerequisites(task.getPrerequisites());
                                return taskRepository.save(t);
                        }).orElseGet(() -> {
                                task.setId(taskId);
                                return taskRepository.save(task);
                        });
        }

        @Override
        public void removeTask(String username, String password, Long taskId) {
                login(username, password);
                taskRepository.deleteById(taskId);
        }

        @Override
        public Long getTotalTime(String username, String password) {
                login(username, password);
                return userRepository.findByUsername(username).getTodos().stream().mapToLong(Task::getEstimatedFinishTime).sum();
        }

        @Override
        public Task addSubTask(String username, String password, Long taskId, Task task) {
                login(username, password);
                Optional<Task> parentTask = taskRepository.findById(taskId);
                if (parentTask.isEmpty()) {
                        throw new EntityNotFoundException("Task", taskId);
                }

                task.setUser(parentTask.get().getUser());
                task.setParentTask(parentTask.get());
                return taskRepository.save(task);
        }

}
