package com.julianogn.tarefas.application.sorting;

import com.julianogn.tarefas.domain.model.Task;
import java.time.LocalDate;
import java.util.Comparator;

/** Strategy pattern: each constant carries its own ordering rule. */
public enum TaskSortOrder {
    CREATION(Comparator.comparing(Task::getCreatedAt)),
    PRIORITY(Comparator.comparing((Task task) -> task.getPriority().weight()).reversed()
            .thenComparing(Task::getCreatedAt)),
    DUE_DATE(Comparator.comparing((Task task) -> task.getDueDate() == null ? LocalDate.MAX : task.getDueDate())
            .thenComparing(Task::getCreatedAt));

    private final Comparator<Task> comparator;

    TaskSortOrder(Comparator<Task> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Task> comparator() {
        return comparator;
    }
}
