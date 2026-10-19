package com.julianogn.tarefas.infrastructure.persistence;

import com.julianogn.tarefas.domain.model.Priority;
import com.julianogn.tarefas.domain.model.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class TaskJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    private LocalDate dueDate;

    @Column(nullable = false)
    private Instant createdAt;

    protected TaskJpaEntity() {
    }

    TaskJpaEntity(UUID id, String title, String description, Priority priority, TaskStatus status,
            LocalDate dueDate, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
    }

    UUID getId() {
        return id;
    }

    String getTitle() {
        return title;
    }

    String getDescription() {
        return description;
    }

    Priority getPriority() {
        return priority;
    }

    TaskStatus getStatus() {
        return status;
    }

    LocalDate getDueDate() {
        return dueDate;
    }

    Instant getCreatedAt() {
        return createdAt;
    }
}
