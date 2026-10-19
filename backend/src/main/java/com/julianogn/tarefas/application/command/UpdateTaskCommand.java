package com.julianogn.tarefas.application.command;

import com.julianogn.tarefas.domain.model.Priority;
import java.time.LocalDate;

public record UpdateTaskCommand(String title, String description, Priority priority, LocalDate dueDate) {
}
