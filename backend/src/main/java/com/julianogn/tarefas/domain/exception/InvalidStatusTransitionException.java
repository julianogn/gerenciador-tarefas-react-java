package com.julianogn.tarefas.domain.exception;

import com.julianogn.tarefas.domain.model.TaskStatus;

public class InvalidStatusTransitionException extends DomainException {

    public InvalidStatusTransitionException(TaskStatus from, TaskStatus to) {
        super("Não é possível mudar a tarefa de %s para %s.".formatted(from, to));
    }
}
