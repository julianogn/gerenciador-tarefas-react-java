package com.julianogn.tarefas.domain.event;

import java.time.Instant;

public interface DomainEvent {

    Instant occurredAt();
}
