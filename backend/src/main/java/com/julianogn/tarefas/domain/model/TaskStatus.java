package com.julianogn.tarefas.domain.model;

import java.util.Set;

public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE;

    public boolean canMoveTo(TaskStatus target) {
        return allowedTargets().contains(target);
    }

    private Set<TaskStatus> allowedTargets() {
        return switch (this) {
            case TODO -> Set.of(IN_PROGRESS, DONE);
            case IN_PROGRESS -> Set.of(TODO, DONE);
            case DONE -> Set.of(TODO);
        };
    }
}
