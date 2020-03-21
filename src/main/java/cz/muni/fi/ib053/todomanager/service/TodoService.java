package cz.muni.fi.ib053.todomanager.service;

import cz.muni.fi.ib053.todomanager.entity.Task;

import java.util.List;

public interface TodoService {

        Boolean login(String username, String passoword);

        List<Task> getTaskList(String username, String password);

        Long addTask(String username, String password, Task task);

        boolean changeTask(String username, String password, Task task);

        boolean removeTask(String username, String password, Long taskId);

        Long getTotalTime(String username, String password);

        Long addSubTask(String username, String password, Long taskId, Task task);
}
