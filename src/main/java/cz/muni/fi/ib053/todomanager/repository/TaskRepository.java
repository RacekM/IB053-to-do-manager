package cz.muni.fi.ib053.todomanager.repository;

import cz.muni.fi.ib053.todomanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

        List<Task> findAllByUser_Id(Long id);

}
