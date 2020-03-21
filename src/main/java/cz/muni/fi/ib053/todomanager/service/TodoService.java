package cz.muni.fi.ib053.todomanager.service;

import cz.muni.fi.ib053.todomanager.entity.Task;

import java.util.List;

public interface TodoService {

        Boolean login(String username, String password);

        List<Task> getTaskList(String username, String password);

        Task addTask(String username, String password, Task task);

        Task changeTask(String username, String password, Long taskId, Task task);

        void removeTask(String username, String password, Long taskId);

        Long getTotalTime(String username, String password);

        Task addSubTask(String username, String password, Long taskId, Task task);
}
