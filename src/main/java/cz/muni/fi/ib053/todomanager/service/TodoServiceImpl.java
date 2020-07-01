package cz.muni.fi.ib053.todomanager.service;

import cz.muni.fi.ib053.todomanager.entity.Task;
import cz.muni.fi.ib053.todomanager.entity.User;
import cz.muni.fi.ib053.todomanager.exceptions.UnauthorizedException;
import cz.muni.fi.ib053.todomanager.repository.TaskRepository;
import cz.muni.fi.ib053.todomanager.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TodoServiceImpl implements TodoService {
        private final UserRepository userRepository;
        private final TaskRepository taskRepository;

        @Autowired
        public TodoServiceImpl(UserRepository userRepository, TaskRepository taskRepository) {
                this.userRepository = userRepository;
                this.taskRepository = taskRepository;
        }

        @Override
        public Boolean login(String username, String password) {
                log.debug("Username: \"{}\" is trying to log in.", username);
                User user = userRepository.findByUsername(username);
                if (user == null) {
                        throw new UnauthorizedException(username);
                }
                return user.getPassword().equals(password);
        }

        @Override
        public List<Task> getTaskList(String username, String password) {
                log.debug("Username: \"{}\" is trying to get all tasks.", username);
                login(username, password);
                User user = userRepository.findByUsername(username);
                return taskRepository.findAllByOwner_Id(user.getId());
        }

        @Override
        public Task addTask(String username, String password, Task task) {
                log.debug("Username: \"{}\" is trying to add new task. Task specification is: \"{}\".", username, task);
                login(username, password);
                User user = userRepository.findByUsername(username);
                task.setOwner(user);
                checkOrdering(user, task);
                return taskRepository.save(task);
        }

        @Override
        public Task changeTask(String username, String password, Long taskId, Task task) {
                log.debug("Username: \"{}\" is trying to change task with id {}. New task specification is: \"{}\".", username, taskId, task);
                login(username, password);
                Task oldTask = taskRepository.getOne(taskId);
                oldTask.setEstimatedFinishTime(task.getEstimatedFinishTime());
                oldTask.setOrderIndex(task.getOrderIndex());
                oldTask.setOwner(task.getOwner());
                oldTask.setPrerequisites(task.getPrerequisites());
                checkOrdering(oldTask.getOwner(), oldTask);

                return taskRepository.save(oldTask);
        }

        private void checkOrdering(User user, Task task) {
                List<Task> tasks = taskRepository.findAllByOwner_Id(user.getId());
                if (tasks.stream().anyMatch(t -> t.getOrderIndex().equals(task.getOrderIndex()))) {
                        for (Task t : tasks) {
                                if ((long) t.getOrderIndex() > task.getOrderIndex()) {
                                        t.setOrderIndex(t.getOrderIndex() + 1);
                                        taskRepository.save(t);
                                }
                        }
                }
        }

        @Override
        public void removeTask(String username, String password, Long taskId) {
                log.debug("Username: \"{}\" is trying to remove task with id {}.", username, taskId);
                login(username, password);
                User owner = userRepository.findByUsername(username);
                List<Task> tasks = taskRepository.findAllByOwner_Id(owner.getId());
                for (Task task : tasks) {
                        task.getPrerequisites()
                                .removeIf(t -> t.getId().equals(taskId));
                }
                taskRepository.deleteById(taskId);
        }

        @Override
        public Long getTotalTime(String username, String password) {
                log.debug("Username: \"{}\" wants to get total time of his tasks.", username);
                login(username, password);
                return taskRepository
                        .findAllByOwner_Id(userRepository.findByUsername(username).getId())
                        .stream()
                        .mapToLong(Task::getEstimatedFinishTime)
                        .sum();
        }
}
