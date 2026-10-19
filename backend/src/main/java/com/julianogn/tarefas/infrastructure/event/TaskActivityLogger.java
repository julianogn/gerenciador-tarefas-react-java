package com.julianogn.tarefas.infrastructure.event;

import com.julianogn.tarefas.domain.event.TaskCreated;
import com.julianogn.tarefas.domain.event.TaskStatusChanged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/** Observer pattern: reacts to domain events without the domain knowing about it. */
@Component
class TaskActivityLogger {

    private static final Logger log = LoggerFactory.getLogger(TaskActivityLogger.class);

    @EventListener
    void on(TaskCreated event) {
        log.info("Tarefa {} criada: {}", event.taskId(), event.title());
    }

    @EventListener
    void on(TaskStatusChanged event) {
        log.info("Tarefa {} mudou de {} para {}", event.taskId(), event.from(), event.to());
    }
}
