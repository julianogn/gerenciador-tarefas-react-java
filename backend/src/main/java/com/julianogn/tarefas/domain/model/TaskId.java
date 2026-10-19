package com.julianogn.tarefas.domain.model;

import java.util.Objects;
import java.util.UUID;

public record TaskId(UUID value) {

    public TaskId {
        Objects.requireNonNull(value, "O id da tarefa é obrigatório.");
    }

    public static TaskId generate() {
        return new TaskId(UUID.randomUUID());
    }

    public static TaskId of(String value) {
        return new TaskId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
