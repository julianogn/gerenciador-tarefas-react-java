package com.julianogn.tarefas.application;

import com.julianogn.tarefas.domain.model.Task;
import com.julianogn.tarefas.domain.model.TaskId;
import com.julianogn.tarefas.domain.repository.TaskRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class InMemoryTaskRepository implements TaskRepository {

    private final Map<TaskId, Task> tasks = new LinkedHashMap<>();

    @Override
    public Task save(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(TaskId id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void delete(TaskId id) {
        tasks.remove(id);
    }
}
