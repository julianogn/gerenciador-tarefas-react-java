package com.julianogn.tarefas.domain.model;

import com.julianogn.tarefas.domain.event.DomainEvent;
import com.julianogn.tarefas.domain.event.TaskCreated;
import com.julianogn.tarefas.domain.event.TaskStatusChanged;
import com.julianogn.tarefas.domain.exception.InvalidStatusTransitionException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Aggregate root. State changes only happen through its methods, which enforce the business rules.
 */
public class Task {

    private final TaskId id;
    private Title title;
    private String description;
    private Priority priority;
    private LocalDate dueDate;
    private TaskStatus status;
    private final Instant createdAt;
    private final List<DomainEvent> events = new ArrayList<>();

    private Task(Builder builder) {
        this.id = Objects.requireNonNull(builder.id);
        this.title = Objects.requireNonNull(builder.title);
        this.description = builder.description == null ? "" : builder.description.strip();
        this.priority = Objects.requireNonNullElse(builder.priority, Priority.MEDIUM);
        this.dueDate = builder.dueDate;
        this.status = Objects.requireNonNullElse(builder.status, TaskStatus.TODO);
        this.createdAt = Objects.requireNonNull(builder.createdAt);
    }

    public static Task create(Title title, String description, Priority priority, LocalDate dueDate, Clock clock) {
        Task task = builder()
                .id(TaskId.generate())
                .title(title)
                .description(description)
                .priority(priority)
                .dueDate(dueDate)
                .createdAt(clock.instant())
                .build();
        task.events.add(new TaskCreated(task.id, title.value(), clock.instant()));
        return task;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void edit(Title newTitle, String newDescription, Priority newPriority, LocalDate newDueDate) {
        this.title = Objects.requireNonNull(newTitle);
        this.description = newDescription == null ? "" : newDescription.strip();
        this.priority = Objects.requireNonNull(newPriority);
        this.dueDate = newDueDate;
    }

    public void start(Clock clock) {
        moveTo(TaskStatus.IN_PROGRESS, clock);
    }

    public void complete(Clock clock) {
        moveTo(TaskStatus.DONE, clock);
    }

    public void reopen(Clock clock) {
        moveTo(TaskStatus.TODO, clock);
    }

    public boolean isOverdue(LocalDate today) {
        return dueDate != null && status != TaskStatus.DONE && dueDate.isBefore(today);
    }

    public List<DomainEvent> pullEvents() {
        List<DomainEvent> pending = List.copyOf(events);
        events.clear();
        return pending;
    }

    private void moveTo(TaskStatus target, Clock clock) {
        if (!status.canMoveTo(target)) {
            throw new InvalidStatusTransitionException(status, target);
        }
        TaskStatus previous = status;
        status = target;
        events.add(new TaskStatusChanged(id, previous, target, clock.instant()));
    }

    public TaskId getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Task task && task.id.equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /** Builder used to rebuild a task from storage without firing creation events. */
    public static final class Builder {

        private TaskId id;
        private Title title;
        private String description;
        private Priority priority;
        private LocalDate dueDate;
        private TaskStatus status;
        private Instant createdAt;

        private Builder() {
        }

        public Builder id(TaskId id) {
            this.id = id;
            return this;
        }

        public Builder title(Title title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder priority(Priority priority) {
            this.priority = priority;
            return this;
        }

        public Builder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder status(TaskStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}
