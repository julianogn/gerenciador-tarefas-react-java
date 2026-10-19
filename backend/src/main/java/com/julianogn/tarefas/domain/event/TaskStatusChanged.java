package com.julianogn.tarefas.domain.event;

import com.julianogn.tarefas.domain.model.TaskId;
import com.julianogn.tarefas.domain.model.TaskStatus;
import java.time.Instant;

public record TaskStatusChanged(TaskId taskId, TaskStatus from, TaskStatus to, Instant occurredAt)
        implements DomainEvent {
}
