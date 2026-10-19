package com.julianogn.tarefas.application.command;

import com.julianogn.tarefas.application.sorting.TaskSortOrder;
import com.julianogn.tarefas.domain.model.Priority;
import com.julianogn.tarefas.domain.model.Task;
import com.julianogn.tarefas.domain.model.TaskStatus;
import java.time.LocalDate;
import java.util.function.Predicate;

public record TaskFilter(TaskStatus status, Priority priority, boolean onlyOverdue, TaskSortOrder sortOrder) {

    public TaskFilter {
        if (sortOrder == null) {
            sortOrder = TaskSortOrder.CREATION;
        }
    }

    public static TaskFilter all() {
        return new TaskFilter(null, null, false, TaskSortOrder.CREATION);
    }

    public Predicate<Task> toPredicate(LocalDate today) {
        Predicate<Task> predicate = task -> true;
        if (status != null) {
            predicate = predicate.and(task -> task.getStatus() == status);
        }
        if (priority != null) {
            predicate = predicate.and(task -> task.getPriority() == priority);
        }
        if (onlyOverdue) {
            predicate = predicate.and(task -> task.isOverdue(today));
        }
        return predicate;
    }
}
