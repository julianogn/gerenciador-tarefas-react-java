package com.julianogn.tarefas.infrastructure.persistence;

import com.julianogn.tarefas.domain.model.Task;
import com.julianogn.tarefas.domain.model.TaskId;
import com.julianogn.tarefas.domain.repository.TaskRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/** Adapter pattern: implements the domain port using Spring Data JPA. */
@Repository
class JpaTaskRepositoryAdapter implements TaskRepository {

    private final SpringDataTaskRepository jpaRepository;

    JpaTaskRepositoryAdapter(SpringDataTaskRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Task save(Task task) {
        jpaRepository.save(TaskEntityMapper.toEntity(task));
        return task;
    }

    @Override
    public Optional<Task> findById(TaskId id) {
        return jpaRepository.findById(id.value()).map(TaskEntityMapper::toDomain);
    }

    @Override
    public List<Task> findAll() {
        return jpaRepository.findAll().stream().map(TaskEntityMapper::toDomain).toList();
    }

    @Override
    public void delete(TaskId id) {
        jpaRepository.deleteById(id.value());
    }
}
