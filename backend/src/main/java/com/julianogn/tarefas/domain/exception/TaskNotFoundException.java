package com.julianogn.tarefas.domain.exception;

import com.julianogn.tarefas.domain.model.TaskId;

public class TaskNotFoundException extends DomainException {

    public TaskNotFoundException(TaskId id) {
        super("Tarefa '%s' não encontrada.".formatted(id));
    }
}
