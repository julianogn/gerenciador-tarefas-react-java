package com.julianogn.tarefas.interfaces.rest;

import com.julianogn.tarefas.application.command.CreateTaskCommand;
import com.julianogn.tarefas.application.command.UpdateTaskCommand;
import com.julianogn.tarefas.domain.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record TaskRequest(
        @NotBlank(message = "O título é obrigatório.") @Size(max = 120) String title,
        @Size(max = 2000) String description,
        @NotNull(message = "A prioridade é obrigatória.") Priority priority,
        LocalDate dueDate) {

    CreateTaskCommand toCreateCommand() {
        return new CreateTaskCommand(title, description, priority, dueDate);
    }

    UpdateTaskCommand toUpdateCommand() {
        return new UpdateTaskCommand(title, description, priority, dueDate);
    }
}
