package com.julianogn.tarefas.domain.model;

import com.julianogn.tarefas.domain.exception.DomainException;

public record Title(String value) {

    public static final int MAX_LENGTH = 120;

    public Title {
        if (value == null || value.isBlank()) {
            throw new DomainException("O título não pode ser vazio.");
        }
        value = value.strip();
        if (value.length() > MAX_LENGTH) {
            throw new DomainException("O título deve ter no máximo %d caracteres.".formatted(MAX_LENGTH));
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
