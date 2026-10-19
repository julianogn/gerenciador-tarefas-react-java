package com.julianogn.tarefas.domain.repository;

import com.julianogn.tarefas.domain.model.Task;
import com.julianogn.tarefas.domain.model.TaskId;
import java.util.List;
import java.util.Optional;

/** Port owned by the domain; infrastructure provides the implementation. */
public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(TaskId id);

    List<Task> findAll();

    void delete(TaskId id);
}
