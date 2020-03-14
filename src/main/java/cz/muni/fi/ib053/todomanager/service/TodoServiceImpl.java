package cz.muni.fi.ib053.todomanager.service;

import cz.muni.fi.ib053.todomanager.entity.Task;
import cz.muni.fi.ib053.todomanager.entity.User;
import cz.muni.fi.ib053.todomanager.repository.TaskRepository;
import cz.muni.fi.ib053.todomanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {
        private final UserRepository userRepository;
        private final TaskRepository taskRepository;

        @Autowired
        public TodoServiceImpl(UserRepository userRepository, TaskRepository taskRepository) {
                this.userRepository = userRepository;
                this.taskRepository = taskRepository;
        }

        public Boolean login(String username, String password) {
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
        public Long addTask(String username, String password, Task task) {
                if (!login(username, password)) {
                        return -1L;
                }
                User user = userRepository.findByUsername(username);
                task.setUser(user);
                Task savedTask = taskRepository.save(task);
                return savedTask.getId();
        }

        @Override
        public boolean changeTask(String username, String password, Task task) {
                return login(username, password);
                //todo
        }

        @Override
        public boolean removeTask(String username, String password, Long taskId) {
                if (!login(username, password)) {
                        return false;
                }
                //todo
                taskRepository.deleteById(taskId);
                return true;
        }

        @Override
        public Long getTotalTime(String username, String password) {
                if (!login(username, password)) {
                        return -1L;
                }
                return userRepository.findByUsername(username).getTodos().stream().mapToLong(Task::getEstimatedFinishTime).sum();
        }


}
