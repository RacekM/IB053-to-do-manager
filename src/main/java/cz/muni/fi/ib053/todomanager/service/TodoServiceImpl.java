package cz.muni.fi.ib053.todomanager.service;

import cz.muni.fi.ib053.todomanager.entity.Task;
import cz.muni.fi.ib053.todomanager.entity.User;
import cz.muni.fi.ib053.todomanager.repository.TaskRepository;
import cz.muni.fi.ib053.todomanager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {
        // todo log something
        private static final Logger LOG = LoggerFactory.getLogger(TodoServiceImpl.class);
        private final UserRepository userRepository;
        private final TaskRepository taskRepository;

        @Autowired
        public TodoServiceImpl(UserRepository userRepository, TaskRepository taskRepository) {
                this.userRepository = userRepository;
                this.taskRepository = taskRepository;
        }

        public Boolean login(String username, String password) {
                LOG.info("Logging works");
                User user = userRepository.findByUsername(username);
                if (user == null) {
                        return false;
                }
                return user.getPassword().equals(password);
        }

        @Override
        public List<Task> getTaskList(String username, String password) {
                if (!login(username, password)) {
                        return Collections.emptyList();
                }

                User user = userRepository.findByUsername(username);
                return user.getTodos();
        }

        @Override
        public Task addTask(String username, String password, Task task) {
                if (!login(username, password)) {
                        return new Task();
                }
                User user = userRepository.findByUsername(username);
                task.setUser(user);
                return taskRepository.save(task);
        }

        @Override
        public Task changeTask(String username, String password, Long taskId, Task task) {
                if (!login(username, password)) {
                        return new Task();
                }
                return taskRepository.findById(taskId)
                        .map(t -> {
                                t.setParentTask(task.getParentTask());
                                t.setUser(task.getUser());
                                t.setEstimatedFinishTime(task.getEstimatedFinishTime());
                                t.setPrerequisites(task.getPrerequisites());
                                return taskRepository.save(t);
                        }).orElseGet(() -> {
                                task.setId(taskId);
                                return taskRepository.save(task);
                        });
        }

        @Override
        // todo problem with JPA annotations, most probably samotehing with bidirectional mapping or cascade
        public void removeTask(String username, String password, Long taskId) {
                if (!login(username, password)) {
                        //throw exception
                        return;
                }
                taskRepository.deleteById(taskId);
        }

        @Override
        public Long getTotalTime(String username, String password) {
                if (!login(username, password)) {
                        return -1L;
                }
                return userRepository.findByUsername(username).getTodos().stream().mapToLong(Task::getEstimatedFinishTime).sum();
        }

        @Override
        public Task addSubTask(String username, String password, Long taskId, Task task) {
                Optional<Task> parentTask = taskRepository.findById(taskId);
                if (parentTask.isEmpty()) {
                        return new Task();
                }

                task.setUser(parentTask.get().getUser());
                task.setParentTask(parentTask.get());
                return taskRepository.save(task);
        }


}
