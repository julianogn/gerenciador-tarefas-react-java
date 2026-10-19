package com.julianogn.tarefas.domain.event;

import com.julianogn.tarefas.domain.model.TaskId;
import java.time.Instant;

public record TaskCreated(TaskId taskId, String title, Instant occurredAt) implements DomainEvent {
}
