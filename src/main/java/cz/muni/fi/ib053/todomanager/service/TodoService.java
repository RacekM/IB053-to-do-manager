package cz.muni.fi.ib053.todomanager.service;

import cz.muni.fi.ib053.todomanager.entity.Task;

import java.util.List;

public interface TodoService {

        /**
         * Method used for logging
         *
         * @param username Username of the user
         * @param password Password used for logging into application
         * @return Method returns true if the provided password is correct for the user with provided username. If the password does not match then method returns False.
         */
        Boolean login(String username, String password);

        /**
         * Method used retrieving all of the user {@link Task}.
         *
         * @param username Username of the user
         * @param password Password of the user
         * @return Method returns list of the tasks which are assigned to the user if the provided password is correct.
         */
        List<Task> getTaskList(String username, String password);

        /**
         * Method used for adding a new {@link Task} to the user.
         *
         * @param username Username of the user
         * @param password Password of the user
         * @param task     Specification of the task which should be added
         * @return Method returns a specification of the newly created task
         */
        Task addTask(String username, String password, Task task);

        /**
         * Method used for changing some attributes of the {@link Task}.
         *
         * @param username Username of the user
         * @param password Password of the user
         * @param taskId   Id of the task which should be modified
         * @param task     Specification of the new values which should be changed in existing task with given id
         * @return Method returns a new specification of the modified task.
         */
        Task changeTask(String username, String password, Long taskId, Task task);

        /**
         * Method used for removing of the {@link Task}.
         *
         * @param username Username of the user
         * @param password Password of the user
         * @param taskId   Id of the task which should be removed.
         */
        void removeTask(String username, String password, Long taskId);

        /**
         * Method used for retrieving total time of the all tasks assigned to the user.
         *
         * @param username Username of the user
         * @param password Password of the user
         * @return Total time of the all tasks which are assigned to the user with given credentials.
         */
        Long getTotalTime(String username, String password);

        /**
         * Method used for adding subtask to already existing task.
         *
         * @param username Username of the user
         * @param password Password of the user
         * @param taskId   Id of the tasks to witch the subtask should be assigned to
         * @param subTask  Specification of the subtask
         * @return Total time of the all tasks which are assigned to the user with given credentials
         */
        Task addSubTask(String username, String password, Long taskId, Task subTask);
}
