package com.julianogn.tarefas.infrastructure.persistence;

import com.julianogn.tarefas.domain.model.Task;
import com.julianogn.tarefas.domain.model.TaskId;
import com.julianogn.tarefas.domain.model.Title;

/** Data Mapper: keeps JPA annotations out of the domain model. */
final class TaskEntityMapper {

    private TaskEntityMapper() {
    }

    static TaskJpaEntity toEntity(Task task) {
        return new TaskJpaEntity(
                task.getId().value(),
                task.getTitle().value(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getDueDate(),
                task.getCreatedAt());
    }

    static Task toDomain(TaskJpaEntity entity) {
        return Task.builder()
                .id(new TaskId(entity.getId()))
                .title(new Title(entity.getTitle()))
                .description(entity.getDescription())
                .priority(entity.getPriority())
                .status(entity.getStatus())
                .dueDate(entity.getDueDate())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
