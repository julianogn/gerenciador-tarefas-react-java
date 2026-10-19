package com.julianogn.tarefas.application.dto;

import com.julianogn.tarefas.domain.model.Priority;
import com.julianogn.tarefas.domain.model.Task;
import com.julianogn.tarefas.domain.model.TaskStatus;
import java.time.Instant;
import java.time.LocalDate;

public record TaskView(
        String id,
        String title,
        String description,
        Priority priority,
        TaskStatus status,
        LocalDate dueDate,
        boolean overdue,
        Instant createdAt) {

    public static TaskView from(Task task, LocalDate today) {
        return new TaskView(
                task.getId().toString(),
                task.getTitle().value(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getDueDate(),
                task.isOverdue(today),
                task.getCreatedAt());
    }
}
